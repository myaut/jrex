package com.tuneit.jrex.expressions;

public class JrexStmAssign implements JrexStatement {
	private JrexExpression var;
	private JrexExpression value;
	
	public JrexStmAssign(JrexExpression var, JrexExpression value) {
		this.var = var;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s = %s;", this.var.toString(), this.value);
	}
}
