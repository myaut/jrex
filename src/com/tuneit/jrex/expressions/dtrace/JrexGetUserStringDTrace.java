package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexGetUserStringDTrace implements JrexExpression  {
	private JrexExpression argument;
	private JrexExpression maxLength;
	
	/**
	 * Get null-terminated string
	 */
	public JrexGetUserStringDTrace(JrexExpression argument) {
		this.argument = argument;
		this.maxLength = null;
	}
	
	/**
	 * Get string without null-terminator
	 */
	public JrexGetUserStringDTrace(JrexExpression argument, JrexExpression maxLength) {
		this.argument = argument;
		this.maxLength = maxLength;
	}

	@Override
	public String toString()  {
		if(this.maxLength != null) {
			return String.format("stringof(copyin(%s, %s))", 
			  		    	  this.argument.toString(),
							  this.maxLength.toString());
		}
		else {
			return String.format("copyinstr(%s)", 
	  		    	  		 this.argument.toString());
		}
	}
}
