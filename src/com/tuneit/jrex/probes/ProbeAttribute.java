package com.tuneit.jrex.probes;

import com.tuneit.jrex.expressions.JrexExpression;

public class ProbeAttribute {
	public ProbeAttribute(String name, JrexExpression expression, int type) {
		this.name = name;
		this.expression = expression;
		this.type = type;
	}

	private String name;
	private JrexExpression expression;
	private int type;
	
	public String getName() {
		return name;
	}
	
	public JrexExpression getExpression() {
		return expression;
	}
	
	public int getType() {
		return this.type;
	}
}
