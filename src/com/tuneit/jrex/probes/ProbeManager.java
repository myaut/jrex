package com.tuneit.jrex.probes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.tuneit.jrex.expressions.*;

public class ProbeManager {
	public static final String LINUX_TASK_RUNNING = "0";
	public static final String SOLARIS_TASK_RUNNING = "SRUN";
	
	private HashMap<String, ProbeGroup> probeGroups;
	private HashMap<String, TracingState> tracingStates;
	private ExprFactory ef;

	public ProbeManager(ExprFactory ef) {
		this.probeGroups = new HashMap<String, ProbeGroup>();
		this.tracingStates = new HashMap<String, TracingState>();
		this.ef = ef;
		
		this.createGlassfishProbes(ef);
		this.createSchedulerProbes(ef);
	}
	
	public void addProbeGroup(ProbeGroup probeGroup) {
		this.probeGroups.put(probeGroup.getName(), probeGroup);
	}
	
	public void writeCode(PrintStream os, String... groups) {
		for(Entry<String, TracingState> tracingStateEntry: this.tracingStates.entrySet()) {
			String code = tracingStateEntry.getValue().getDeclaration().toString();
			os.println(code);
		}
		
		for(String probeGroupName : groups) {
			ProbeGroup probeGroup = this.probeGroups.get(probeGroupName);
			
			String startCode = probeGroup.getStartProbe().generateCode(this.ef);
			String endCode = probeGroup.getEndProbe().generateCode(this.ef);
			
			os.println(startCode);
			os.println(endCode);
		}
	}
	
	public List<Request> parseLog(Reader r) throws IOException {
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		List<Event> events = new LinkedList<Event>();
		List<Request> requests = new LinkedList<Request>();
		
		while(line != null) {
			/* Parse group/probeName */
			int pointIdx = line.indexOf('.');
			int spaceIdx = line.indexOf(' ');
			
			String groupName = line.substring(0, pointIdx);
			String probeName = line.substring(pointIdx + 1, spaceIdx);
			String probeParams = line.substring(spaceIdx + 1);
			
			/* Find probe */
			ProbeGroup probeGroup = this.probeGroups.get(groupName);			
			if(probeGroup == null) {
				throw new JrexParseException("Probe group '" + groupName + "' not known to J-REX");
			}
			
			Probe probe = probeGroup.findProbe(probeName);
			if(probe == null) {
				throw new JrexParseException("Probe '" + groupName + "." + probeName + 
											 "' not known to J-REX");
			}
			
			/* Read extended params */
			int xParamCount = probe.getXParamCount();
			String[] xParamLines = new String[xParamCount];
			for(int xi = 0; xi < xParamCount; ++xi) {
				xParamLines[xi] = br.readLine();
				
				if(xParamLines[xi] == null) {
					throw new JrexParseException("Couldn't read extended param line!");
				}
			}
			
			/* Now let probe to parse everything */
			Event event = probe.parseEvent(probeParams, xParamLines);
			event.setProbe(probe, probeGroup);
			
			if(probe.isStartProbe()) { 
				events.add(event);
			}
			else {
				/* Bind endEvent to startEvent */
				boolean bound = false;
				
				for (Iterator<Event> it = events.iterator(); it.hasNext(); ) {
					Event startEvent = it.next();
					
			        if (event.equals(startEvent)) {
			        	Request request = new Request(startEvent, event);
			        	
			        	if(probeGroup.isRoot()) {
			        		/* Try to bind previous requests to current */
			        		for (ListIterator<Request> rqIt = requests.listIterator(requests.size()); 
			        			 rqIt.hasPrevious(); ) {
			        			Request subRequest = rqIt.previous();
			        			
			        			if(subRequest.getStartTime() < request.getStartTime())
			        				break;
			        			
			        			if(request.tryToAddSubRequest(subRequest)) {
			        				rqIt.remove();
			        			}
			        		}
			        	}
			        	
			        	requests.add(request);			        	
			            it.remove();
			            bound = true;
			            break;
			        }
			    }
				
				if(!bound) {
					throw new JrexParseException("Unbound end event " + event.toString());
				}
			}
			
			line = br.readLine();
		}
		
		if(!events.isEmpty()) {
			throw new JrexParseException("There are unbound start events");
		}
		
		return requests;
	}
	
	private void createGlassfishProbes(ExprFactory ef) {
		JrexExpression argUri = ef.userString(ef.argument(0, false), 
											  ef.argument(1, false));
		JrexExpression argMethod = ef.userString(ef.argument(2, false), 
												 ef.argument(3, false));
		JrexExpression argStatus = ef.argument(4, false);
		
		ProbeAttribute attrUri = new ProbeAttribute("uri", argUri, PrintArgument.FMT_XSTRING);
		ProbeAttribute attrMethod = new ProbeAttribute("method", argMethod, PrintArgument.FMT_STRING);
		ProbeAttribute attrStatus = new ProbeAttribute("status", argStatus, PrintArgument.FMT_LONG);
		
		TracingState httpTidTrace = ef.tracingState("httpTid", ef.globalVar(JrexVarGlobal.GV_THREAD_ID));
		
		Probe probeRequest = new Probe("request", 
									   "glassfish.request", 
									   "glassfish$pid:::request");
		probeRequest.addAttribute(attrUri);
		probeRequest.addAttribute(attrMethod);
		probeRequest.addStatement(httpTidTrace.getEnableExpr());
		
		Probe probeResponse = new Probe("response", 
				   					    "glassfish.response", 
				   						"glassfish$pid:::response");
		probeResponse.addAttribute(attrUri);
		probeResponse.addAttribute(attrMethod);
		probeResponse.addAttribute(attrStatus);
		probeResponse.addStatement(httpTidTrace.getDisableExpr());
		
		this.tracingStates.put("httpTid", httpTidTrace);
		
		this.addProbeGroup(new ProbeGroup("glassfish", probeRequest, probeResponse));
	}
	
	private void createSchedulerProbes(ExprFactory ef) {
		final String stapCpuOff = "scheduler.cpu_off";
		final String dtraceCpuOff = "sched:::off-cpu";
		final String stapCpuOn = "scheduler.cpu_on";
		final String dtraceCpuOn = "sched:::on-cpu";
		
		TracingState threadState = ef.tracingState("threadState", ef.globalVar(JrexVarGlobal.GV_THREAD_ID));
		
		JrexExprCompare cswInvoluntaryCond = ef.equal(ef.globalVar(JrexVarGlobal.GV_THREAD_STATE), 
													  ef.pair(LINUX_TASK_RUNNING, SOLARIS_TASK_RUNNING));
		
		Probe probeVCSStart = this.createSchedProbeImpl("start", stapCpuOff, dtraceCpuOff, 
								  cswInvoluntaryCond.not(), threadState.getEnableExpr());
		Probe probeIVCSStart = this.createSchedProbeImpl("start", stapCpuOff, dtraceCpuOff, 
								  cswInvoluntaryCond, threadState.getDisableExpr());
		Probe probeVCSEnd = this.createSchedProbeImpl("end", stapCpuOn, dtraceCpuOn, 
								  threadState.getCheckExpr(), null);
		Probe probeIVCSEnd = this.createSchedProbeImpl("end", stapCpuOn, dtraceCpuOn, 
								  threadState.getCheckExpr().not(), null);		
		
		ProbeGroup groupVCS = new ProbeGroup("vcs", probeVCSStart, probeVCSEnd);
		ProbeGroup groupIVCS = new ProbeGroup("ivcs", probeIVCSStart, probeIVCSEnd);
		
		groupVCS.bindByTid("glassfish");
		groupIVCS.bindByTid("glassfish");
		
		this.addProbeGroup(groupVCS);
		this.addProbeGroup(groupIVCS);
		
		this.tracingStates.put("threadState", threadState);
	}
	
	private Probe createSchedProbeImpl(String name, String systemTapName, String dtraceName,
									  JrexExpression predCond, JrexStatement stateChange) {
		TracingState httpTidTrace = this.tracingStates.get("httpTid");		
		
		JrexExpression cond = ef.and(httpTidTrace.getCheckExpr(), predCond);
		Probe probe = new Probe(name, systemTapName, dtraceName);
		probe.setPredicate(cond);
		
		if(stateChange != null)
			probe.addStatement(stateChange);
		
		return probe;
	}
}
