package com.tuneit.jrex.expressions;

public class JrexVarLocal implements JrexExpression {
	private String varName;
	
	public JrexVarLocal(String varName) {
		this.varName = varName;
	}

	@Override
	public String toStapCode() {
		return this.varName;
	}

	@Override
	public String toDTraceCode() {
		return "this->" + this.varName;
	}

}
