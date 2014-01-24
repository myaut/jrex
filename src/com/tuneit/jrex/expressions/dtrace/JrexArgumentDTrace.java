package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexArgumentDTrace implements JrexExpression {
	private int index;
	private boolean typed;
		
	/**
	 * Constructor for positional-only argument (i.e. $argX for stap user
	 * arguments)
	 * @param index - argument index (begins with 0). For stap arguments, it is incremented 
	 * 				  to be conformant with stap USDT implementation.
	 * @param typed - use typed args[x] in DTrace
	 */
	public JrexArgumentDTrace(int index, boolean typed) {
		this.index = index;
		this.typed = typed;
	}	

	@Override
	public String toString() {
		if(this.typed) {
			return String.format("args[%d]", this.index);
		}
		else {
			return String.format("arg%d", this.index);
		}
	}

}
