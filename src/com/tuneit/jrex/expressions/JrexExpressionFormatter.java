package com.tuneit.jrex.expressions;

import java.util.Formatter;

abstract class JrexExpressionFormatter implements JrexExpression {
	abstract void formatStapCode(Formatter formatter);
	abstract void formatDTraceCode(Formatter formatter);
	
	@Override
	public String toStapCode() {
		Formatter formatter = new Formatter(); 
		
		try {
			this.formatDTraceCode(formatter);
		}
		finally {
			formatter.close();
		}
		
		return formatter.toString();
	}

	@Override
	public String toDTraceCode() {
		Formatter formatter = new Formatter(); 
		
		try {
			this.formatStapCode(formatter);
		}
		finally {
			formatter.close();
		}
		
		return formatter.toString();
	}

}
