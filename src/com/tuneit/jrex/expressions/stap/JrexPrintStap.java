package com.tuneit.jrex.expressions.stap;

import java.util.List;

import com.tuneit.jrex.expressions.JrexStatement;
import com.tuneit.jrex.expressions.JrexPrintArgument;
import com.tuneit.jrex.expressions.dtrace.JrexPrintDTrace;

public class JrexPrintStap implements JrexStatement {
	static final String FIELD_DELIMITER = " ";
	
	private List<JrexPrintArgument> args;
	private String prefix;
	
	public JrexPrintStap(String prefix, List<JrexPrintArgument> args) {
		this.prefix = prefix;
		this.args = args;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("printdln(\"");
		sb.append(JrexPrintStap.FIELD_DELIMITER);
		sb.append("\"");
		
		sb.append(", \"");
		sb.append(this.prefix);
		sb.append("\"");
		
		for(JrexPrintArgument arg: args) {
			sb.append(", ");
			sb.append(arg.getExpression().toString());
		}
		
		sb.append(");");
		
		return sb.toString();
	}
}
