package com.tuneit.jrex.probes;

public class ProbeAttribute {
	private String name;
	private String expression;
	
	public ProbeAttribute(String name) {
		this.expression = null;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
}
