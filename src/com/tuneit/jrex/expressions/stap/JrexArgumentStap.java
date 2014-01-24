package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexArgumentStap  implements JrexExpression  {
	private String name;
	private int index;

	/**
	 * Named argument (in DTrace names are not supported, so we fall-back to indexed implementation)
	 * @param name
	 * @param index - argument index (begins with 0)
	 * @param typed - use typed args[x] in DTrace
	 */
	public JrexArgumentStap(String name, int index) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public String toString() {
		if(this.name != null) {
			return String.format("$%s", this.name);
		}
		
		return String.format("$arg%d", this.index + 1);
	}
}
