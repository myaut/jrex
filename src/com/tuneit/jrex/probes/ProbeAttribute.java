package com.tuneit.jrex.probes;

import com.tuneit.jrex.expressions.JrexExpression;
import com.tuneit.jrex.expressions.PrintArgument;

public class ProbeAttribute {
	public ProbeAttribute(String name, JrexExpression expression, int type) {
		this.name = name;
		this.expression = expression;
		this.type = type;
		
		/* deduce varType from type */
		switch(type) {
		case PrintArgument.FMT_STRING:
		case PrintArgument.FMT_XSTRING:
			this.varType = "string";
			break;
		default:
			this.varType = "long";
			break;
		}
	}

	private String name;
	private String varType;
	private JrexExpression expression;
	private int type;
	
	public String getName() {
		return name;
	}
	
	public String getVarType() {
		return this.varType;
	}
	
	public JrexExpression getExpression() {
		return expression;
	}
	
	public int getType() {
		return this.type;
	}
}
