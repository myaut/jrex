package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexVarLocalDTrace implements JrexExpression {
	private String varName;
	
	public JrexVarLocalDTrace(String varName) {
		this.varName = varName;
	}

	@Override
	public String toString() {
		return "this->" + this.varName;
	}

}
