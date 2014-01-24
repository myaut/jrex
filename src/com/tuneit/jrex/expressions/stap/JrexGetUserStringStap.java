package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexGetUserStringStap implements JrexExpression {
	private JrexExpression argument;
	private JrexExpression maxLength;
	
	/**
	 * Get null-terminated string
	 */
	public JrexGetUserStringStap(JrexExpression argument) {
		this.argument = argument;
		this.maxLength = null;
	}
	
	/**
	 * Get string without null-terminator
	 */
	public JrexGetUserStringStap(JrexExpression argument, JrexExpression maxLength) {
		this.argument = argument;
		this.maxLength = maxLength;
	}
	
	@Override
	public String toString() {
		if(this.maxLength != null) {
			return String.format("user_string_n(%s, %s)", 
			  		    	     this.argument.toString(),
							     this.maxLength.toString());
		}
		else {
			return String.format("user_string(%s)", 
	  		    	  		     this.argument.toString());
		}
	}
}
