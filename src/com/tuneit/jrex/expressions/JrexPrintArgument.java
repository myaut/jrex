package com.tuneit.jrex.expressions;

public class JrexPrintArgument {
	public static final int FMT_STRING = 0;
	public static final int FMT_LONG = 1;
	public static final int FMT_POINTER = 2;
	
	private JrexExpression expr;
	private int format;
	
	public JrexPrintArgument(JrexExpression expr, int format) {
		this.expr = expr;
		this.format = format;
	}
	
	public JrexExpression getExpression() {
		return expr;
	}
	
	public int getFormat() {
		return this.format;
	}
}
