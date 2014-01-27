package com.tuneit.jrex.probes;

import java.util.ArrayList;
import java.util.List;

import com.tuneit.jrex.expressions.*;

public class Probe {
	private List<ProbeAttribute> attributes;
	private List<JrexStatement> statements;
	private JrexExpression predicate;
	
	private String systemTapName;
	private String dtraceName;
	private String group;
	private String name;
	
	public Probe(String group, String name, String systemTapName, String dtraceName) {
		this.group = group;
		this.name = name;
		this.systemTapName = systemTapName;
		this.dtraceName = dtraceName;
		this.predicate = null;
		
		this.attributes = new ArrayList<ProbeAttribute>();
		this.statements = new ArrayList<JrexStatement>();
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

	public String getGroup() {
		return group;
	}
	
	public String getName() {
		return name;
	}
}
