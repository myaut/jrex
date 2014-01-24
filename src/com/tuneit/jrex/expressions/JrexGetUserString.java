package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexGetUserString extends JrexExpressionFormatter {
	private JrexExpression argument;
	private JrexExpression maxLength;
	
	/**
	 * Get null-terminated string
	 */
	public JrexGetUserString(JrexExpression argument) {
		this.argument = argument;
		this.maxLength = null;
	}
	
	/**
	 * Get string without null-terminator
	 */
	public JrexGetUserString(JrexExpression argument, JrexExpression maxLength) {
		this.argument = argument;
		this.maxLength = maxLength;
	}
	
	@Override
	public void formatStapCode(Formatter formatter) {
		if(this.maxLength != null) {
			formatter.format("user_string_n(%s, %s)", 
			  		    	  this.argument.toStapCode(),
							  this.maxLength.toStapCode());
		}
		else {
			formatter.format("user_string(%s)", 
	  		    	  		 this.argument.toStapCode());
		}
	}

	@Override
	public void formatDTraceCode(Formatter formatter) {
		if(this.maxLength != null) {
			formatter.format("stringof(copyin(%s, %s))", 
			  		    	  this.argument.toDTraceCode(),
							  this.maxLength.toDTraceCode());
		}
		else {
			formatter.format("copyinstr(%s)", 
	  		    	  		 this.argument.toDTraceCode());
		}
	}
}
