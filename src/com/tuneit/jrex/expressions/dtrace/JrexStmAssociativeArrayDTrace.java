package com.tuneit.jrex.expressions.dtrace;

import java.util.Arrays;
import java.util.List;

import com.tuneit.jrex.expressions.JrexGlobalStatement;
import com.tuneit.jrex.expressions.JrexVariable;

public class JrexStmAssociativeArrayDTrace implements JrexGlobalStatement {
	private String type;
	private String name;
	private List<JrexVariable> keys;
	
	public JrexStmAssociativeArrayDTrace(String type, String name, JrexVariable... keys) {
		this.type = type;
		this.name = name;
		this.keys = Arrays.asList(keys);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		
		for(JrexVariable key: this.keys) {
			if(!first)
				sb.append(", ");
			
			sb.append(key.getType());
			first = false;
		}
		
		return String.format("%s %s[%s];", type, name, sb.toString());
	}
	
	
}
