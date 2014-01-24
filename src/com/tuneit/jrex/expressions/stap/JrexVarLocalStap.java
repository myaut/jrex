package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexVarLocalStap implements JrexExpression {
	private String varName;
	
	public JrexVarLocalStap(String varName) {
		this.varName = varName;
	}

	@Override
	public String toString() {
		return this.varName;
	}
}
