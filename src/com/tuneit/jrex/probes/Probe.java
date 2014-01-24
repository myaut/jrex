package com.tuneit.jrex.probes;

import java.util.ArrayList;
import java.util.List;

import com.tuneit.jrex.expressions.*;

public class Probe {
	private List<ProbeAttribute> attributes;  
	
	private String systemTapName;
	private String dtraceName;
	private String group;
	private String name;
	
	public Probe(String group, String name, String systemTapName, String dtraceName) {
		this.group = group;
		this.name = name;
		this.systemTapName = systemTapName;
		this.dtraceName = dtraceName;
		
		this.attributes = new ArrayList<ProbeAttribute>();
	}
	
	public void addAttribute(ProbeAttribute attribute) {
		this.attributes.add(attribute);
	}
	
	public String generateCode(ExprFactory ef) throws JrexInvalidTool {
		/* Generate attributes */
		JrexStmBody probeBody = ef.body();
		ArrayList<JrexPrintArgument> printArgs = new ArrayList<JrexPrintArgument>();
		
		for(ProbeAttribute attr : this.attributes) {
			JrexExpression var = ef.localVar(attr.getName());
			probeBody.addStatement(ef.assign(var, attr.getExpression()));
			
			printArgs.add(new JrexPrintArgument(var, attr.getType()));
		}
		
		String printPrefix = String.format("%s.%s", this.group, this.name);
		probeBody.addStatement(ef.print(printPrefix, printArgs));
		
		JrexGlobalStatement probe = ef.probe(this.systemTapName, this.dtraceName, probeBody);
		
		return probe.toString();
	}

	public String getGroup() {
		return group;
	}
	
	public String getName() {
		return name;
	}
}
