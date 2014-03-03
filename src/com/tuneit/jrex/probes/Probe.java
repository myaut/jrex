package com.tuneit.jrex.probes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.tuneit.jrex.expressions.*;

public class Probe {
	private List<ProbeAttribute> attributes;
	private List<JrexStatement> statements;
	private JrexExpression predicate;
	
	private String systemTapName;
	private String dtraceName;
	private String name;
	private String group;
	private boolean isStart;
	
	public Probe(String name, String systemTapName, String dtraceName) {
		this.group = null;
		this.name = name;
		this.systemTapName = systemTapName;
		this.dtraceName = dtraceName;
		this.predicate = null;
		this.isStart = false;
		
		this.attributes = new ArrayList<ProbeAttribute>();
		this.statements = new ArrayList<JrexStatement>();
	}
	
	public List<ProbeAttribute> getAttributes() {
		return this.attributes;
	}
	
	public void addAttribute(ProbeAttribute attribute) {
		this.attributes.add(attribute);
	}
	
	public void addStatement(JrexStatement statement) {
		this.statements.add(statement);
	}
	
	public void setPredicate(JrexExpression predicate) {
		this.predicate = predicate;
	}
	
	public String generateCode(ExprFactory ef) {
		/* Generate attributes */
		JrexStmBody probeBody = ef.body();
		ArrayList<PrintArgument> printArgs = new ArrayList<PrintArgument>();
		
		/* We want to print current time and thread id */
		printArgs.add(new PrintArgument(ef.globalVar(JrexVarGlobal.GV_THREAD_ID), 
											PrintArgument.FMT_LONG));
		printArgs.add(new PrintArgument(ef.globalVar(JrexVarGlobal.GV_CLOCK), 
										    PrintArgument.FMT_LONG));
		
		for(ProbeAttribute attr : this.attributes) {
			JrexExpression var = ef.localVar(attr.getVarType(), attr.getName());
			probeBody.addStatement(ef.assign(var, attr.getExpression()));
			
			if(attr.getType() == PrintArgument.FMT_XSTRING) {
				/* Some strings could countain FIELD_DELIMITERS and they
				 * are not escaped, so this will break parser. 
				 * Print them separately. */
				ArrayList<PrintArgument> xprintArgs = new ArrayList<PrintArgument>();
				xprintArgs.add(new PrintArgument(var, attr.getType()));
				
				this.statements.add(ef.print(attr.getName(), xprintArgs));
			}
			else {
				printArgs.add(new PrintArgument(var, attr.getType()));
			}
		}
		
		/* Build probe body */
		String printPrefix = String.format("%s.%s", this.group, this.name);
		probeBody.addStatement(ef.print(printPrefix, printArgs));
		
		for(JrexStatement stm : this.statements) {
			probeBody.addStatement(stm);
		}
		
		JrexGlobalStatement probe = ef.probe(this.systemTapName, this.dtraceName, probeBody,
											 this.predicate);
		
		return probe.toString();
	}
	
	public int getXParamCount() {
		int count = 0;
		
		for(ProbeAttribute attr : this.attributes) {
			if(attr.getType() == PrintArgument.FMT_XSTRING)
				++count;
		}
		
		return count;
	}
	
	public Event parseEvent(String probeParams, String xParamLines[]) {
		Event event;
		String params[] = probeParams.split(" ");
		int xParamId = 0;
		int paramId = 2;
		
		long tid = Long.parseLong(params[0]);
		long time = Long.parseLong(params[1]);
		
		event = new Event(this.group, this.name, time, tid);
		
		for(ProbeAttribute attr : this.attributes) {
			if(attr.getType() == PrintArgument.FMT_XSTRING) {
				String xParam = xParamLines[xParamId];
				int spaceIdx = xParam.indexOf(' ');
				
				String paramName = xParam.substring(0, spaceIdx);
				String paramValue = xParam.substring(spaceIdx + 1);
						
				if(paramName == attr.getName()) {
					throw new JrexParseException("Invalid expected parameter name: " + paramName);
				}
				
				event.addStringParam(paramName, paramValue);
				
				++xParamId;
			}
			else {
				String param = params[paramId];
				
				switch(attr.getType()) {
				case PrintArgument.FMT_LONG:
					event.addLongParam(attr.getName(), Long.parseLong(param), param);
					break;
				case PrintArgument.FMT_POINTER:
					event.addLongParam(attr.getName(), Long.parseLong(param, 16), param);
					break;
				case PrintArgument.FMT_STRING:
					event.addStringParam(attr.getName(), param);
					break;
				}
				
				++paramId;
			}
		}
		
		return event;
	}
	
	public String getName() {
		return name;
	}
	
	/* Service methods */
	
	public void setGroup(String name) {
		this.group = name;
	}
	
	public void setStartProbe() {
		this.isStart = true;
	}
	
	public boolean isStartProbe() {
		return this.isStart;
	}
}
