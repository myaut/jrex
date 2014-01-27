package com.tuneit.jrex.expressions.stap;

import java.util.List;

import com.tuneit.jrex.expressions.JrexStatement;
import com.tuneit.jrex.expressions.PrintArgument;
import com.tuneit.jrex.expressions.dtrace.JrexPrintDTrace;

public class JrexPrintStap implements JrexStatement {
	static final String FIELD_DELIMITER = " ";
	
	private List<PrintArgument> args;
	private String prefix;
	
	public JrexPrintStap(String prefix, List<PrintArgument> args) {
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
		
		for(PrintArgument arg: args) {
			sb.append(", ");
			sb.append(arg.getExpression().toString());
		}
		
		sb.append(");");
		
		return sb.toString();
	}
}
