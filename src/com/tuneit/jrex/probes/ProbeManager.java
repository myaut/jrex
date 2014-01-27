package com.tuneit.jrex.probes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.tuneit.jrex.expressions.*;

public class ProbeManager {
	public static final String LINUX_TASK_RUNNING = "0";
	public static final String SOLARIS_TASK_RUNNING = "SRUN";
	
	private List<Probe> allProbes;
	private HashMap<String, TracingState> tracingStates;
	private ExprFactory ef;

	public ProbeManager(ExprFactory ef) {
		this.allProbes = new ArrayList<Probe>();
		this.tracingStates = new HashMap<String, TracingState>();
		this.ef = ef;
		
		this.createGlassfishProbes(ef);
		this.createSchedulerProbes(ef);
	}
	
	public void writeCode(PrintStream os, String... groups) {
		List<String> groupsList = Arrays.asList(groups);
		
		for(Entry<String, TracingState> tracingStateEntry: this.tracingStates.entrySet()) {
			String code = tracingStateEntry.getValue().getDeclaration().toString();
			os.println(code);
		}
		
		for(Probe probe : this.allProbes) {
			if(!groupsList.contains(probe.getGroup()))
				continue;
			
			String code = probe.generateCode(this.ef);
			os.println(code);
		}
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
		
		Probe probeRequest = new Probe("glassfish", "request", 
									   "glassfish.request", 
									   "glassfish$pid:::request");
		probeRequest.addAttribute(attrUri);
		probeRequest.addAttribute(attrMethod);
		probeRequest.addStatement(httpTidTrace.getEnableExpr());
		
		this.allProbes.add(probeRequest);
		
		Probe probeResponse = new Probe("glassfish", "response", 
				   					    "glassfish.response", 
				   						"glassfish$pid:::response");
		probeResponse.addAttribute(attrUri);
		probeResponse.addAttribute(attrMethod);
		probeResponse.addAttribute(attrStatus);
		probeResponse.addStatement(httpTidTrace.getDisableExpr());
		
		this.allProbes.add(probeResponse);
		
		this.tracingStates.put("httpTid", httpTidTrace);
	}
	
	private void createSchedulerProbes(ExprFactory ef) {
		final String stapCpuOff = "scheduler.cpu_off";
		final String dtraceCpuOff = "sched:::off-cpu";
		final String stapCpuOn = "scheduler.cpu_on";
		final String dtraceCpuOn = "sched:::on-cpu";
		
		
		TracingState threadState = ef.tracingState("threadState", ef.globalVar(JrexVarGlobal.GV_THREAD_ID));
		
		JrexExprCompare cswInvoluntaryCond = ef.equal(ef.globalVar(JrexVarGlobal.GV_THREAD_STATE), 
													  ef.pair(LINUX_TASK_RUNNING, SOLARIS_TASK_RUNNING));
		
		this.createSchedProbeImpl("vcs", "start", stapCpuOff, dtraceCpuOff, 
								  cswInvoluntaryCond.not(), threadState.getEnableExpr());
		this.createSchedProbeImpl("ivcs", "start", stapCpuOff, dtraceCpuOff, 
								  cswInvoluntaryCond, threadState.getDisableExpr());
		this.createSchedProbeImpl("vcs", "end", stapCpuOn, dtraceCpuOn, 
								  threadState.getCheckExpr(), null);
		this.createSchedProbeImpl("ivcs", "end", stapCpuOn, dtraceCpuOn, 
								  threadState.getCheckExpr().not(), null);		
		
		this.tracingStates.put("threadState", threadState);
	}
	
	private void createSchedProbeImpl(String group, String name, String systemTapName, String dtraceName,
									  JrexExpression predCond, JrexStatement stateChange) {
		TracingState httpTidTrace = this.tracingStates.get("httpTid");		
		
		JrexExpression cond = ef.and(httpTidTrace.getCheckExpr(), predCond);
		Probe probe = new Probe(group, name, systemTapName, dtraceName);
		probe.setPredicate(cond);
		
		if(stateChange != null)
			probe.addStatement(stateChange);
		
		this.allProbes.add(probe);
	}
}
