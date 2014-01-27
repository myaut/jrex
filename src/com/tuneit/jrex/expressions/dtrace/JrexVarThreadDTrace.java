package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexVariable;

public class JrexVarThreadDTrace implements JrexVariable {
	private String varName;
	private String varType;
	
	public JrexVarThreadDTrace(String varType, String varName) {
		this.varName = varName;
		this.varType = varType;
	}

	@Override
	public String toString() {
		return "self->" + this.varName;
	}

	@Override
	public String getType() {
		return this.varType;
	}
	

}
