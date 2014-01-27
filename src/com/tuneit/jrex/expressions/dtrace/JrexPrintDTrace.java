package com.tuneit.jrex.expressions.dtrace;

import java.util.List;

import com.tuneit.jrex.expressions.JrexStatement;
import com.tuneit.jrex.expressions.PrintArgument;

public class JrexPrintDTrace implements JrexStatement {
	static final String FIELD_DELIMITER = " ";
	
	private List<PrintArgument> args;
	private String prefix;
	
	public JrexPrintDTrace(String prefix, List<PrintArgument> args) {
		this.prefix = prefix;
		this.args = args;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("printf(\"");
		
		sb.append(this.prefix);
		sb.append(JrexPrintDTrace.FIELD_DELIMITER);
		
		for(PrintArgument arg: args) {
			switch(arg.getFormat()) {
			case PrintArgument.FMT_STRING:
			case PrintArgument.FMT_XSTRING:
				sb.append("%s");
				break;
			case PrintArgument.FMT_LONG:
				sb.append("%d");
				break;
			case PrintArgument.FMT_POINTER:
				sb.append("%p");
				break;
			default:
				sb.append("%d");
				break;
			}
			sb.append(JrexPrintDTrace.FIELD_DELIMITER);
		}
		
		sb.append("\\n\"");
		
		for(PrintArgument arg: args) {
			sb.append(", ");
			sb.append(arg.getExpression().toString());
		}
		
		sb.append(");");
		
		return sb.toString();
	}
}
