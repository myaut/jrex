package com.tuneit.jrex.expressions;

public class JrexString implements JrexExpression {
	public JrexString(String s) {
		this.s = s;
	}

	private String s;

	@Override
	public String toString() {
		return this.s;
	}
}
