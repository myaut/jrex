package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexVariable;

public class JrexVarLocalDTrace implements JrexVariable {
	private String varType;
	private String varName;
	
	public JrexVarLocalDTrace(String varType, String varName) {
		this.varType = varType;
		this.varName = varName;
	}

	@Override
	public String toString() {
		return "this->" + this.varName;
	}

	@Override
	public String getType() {
		return this.varType;
	}
}
