package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexArgument extends JrexExpressionFormatter {
	private String name;
	private int index;
	private boolean typed;
		
	/**
	 * Constructor for positional-only argument (i.e. $argX for stap user
	 * arguments)
	 * @param index - argument index (begins with 0). For stap arguments, it is incremented 
	 * 				  to be conformant with stap USDT implementation.
	 * @param typed - use typed args[x] in DTrace
	 */
	public JrexArgument(int index, boolean typed) {
		this.index = index;
		this.typed = typed;
		this.name = null;
	}	
	
	/**
	 * Constructor for named argument (in DTrace names are not supported, 
	 * so we fall-back to indexed implementation)
	 * @param name
	 * @param index - argument index (begins with 0)
	 * @param typed - use typed args[x] in DTrace
	 */
	public JrexArgument(String name, int index, boolean typed) {
		this.index = index;
		this.typed = typed;
		this.name = name;
	}
	
	@Override
	public void formatStapCode(Formatter formatter) {
		if(this.name != null) {
			formatter.format("$%s", this.name);
		}
		else {
			formatter.format("$arg%d", this.index + 1);
		}
	}

	@Override
	public void formatDTraceCode(Formatter formatter) {
		if(this.typed) {
			formatter.format("args[%d]", this.index);
		}
		else {
			formatter.format("arg%d", this.index);
		}
	}

}
