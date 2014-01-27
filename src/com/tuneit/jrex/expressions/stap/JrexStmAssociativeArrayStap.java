package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexGlobalStatement;

public class JrexStmAssociativeArrayStap implements JrexGlobalStatement {
	private String name;
	
	public JrexStmAssociativeArrayStap(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("global %s;", this.name);
	}
}
