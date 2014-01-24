package com.tuneit.jrex.expressions;

public class JrexPair implements JrexExpression {
	public JrexPair(String stapString, String dtraceString) {
		this.stapString = stapString;
		this.dtraceString = dtraceString;
	}

	private String stapString;
	private String dtraceString;

	@Override
	public String toStapCode() {
		return this.stapString;
	}

	@Override
	public String toDTraceCode() {
		return this.stapString;
	}

}
