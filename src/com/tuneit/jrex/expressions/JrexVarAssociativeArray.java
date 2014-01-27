package com.tuneit.jrex.expressions;

import java.util.Arrays;
import java.util.List;

public class JrexVarAssociativeArray implements JrexVariable {
	private String name;
	private List<JrexVariable> keys;
	
	public JrexVarAssociativeArray(String name, JrexVariable... keys) {
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
			
			sb.append(key.toString());
			first = false;
		}
		
		return String.format("%s[%s]", name, sb.toString());
	}
	
	public String getType() {
		return "<assoc-array>";
	}
}
