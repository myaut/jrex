package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexVariable;

public class JrexVarLocalStap implements JrexVariable {
	private String varType;
	private String varName;
	
	public JrexVarLocalStap(String varType, String varName) {
		this.varType = varType;
		this.varName = varName;
	}

	@Override
	public String toString() {
		return this.varName;
	}

	@Override
	public String getType() {
		return this.varType;
	}
}
