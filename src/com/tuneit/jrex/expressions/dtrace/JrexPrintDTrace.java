package com.tuneit.jrex.expressions.dtrace;

import java.util.List;

import com.tuneit.jrex.expressions.JrexStatement;
import com.tuneit.jrex.expressions.JrexPrintArgument;

public class JrexPrintDTrace implements JrexStatement {
	static final String FIELD_DELIMITER = " ";
	
	private List<JrexPrintArgument> args;
	private String prefix;
	
	public JrexPrintDTrace(String prefix, List<JrexPrintArgument> args) {
		this.prefix = prefix;
		this.args = args;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("printf(\"");
		
		sb.append(this.prefix);
		sb.append(JrexPrintDTrace.FIELD_DELIMITER);
		
		for(JrexPrintArgument arg: args) {
			switch(arg.getFormat()) {
			case JrexPrintArgument.FMT_STRING:
				sb.append("%s");
				break;
			case JrexPrintArgument.FMT_LONG:
				sb.append("%d");
				break;
			case JrexPrintArgument.FMT_POINTER:
				sb.append("%p");
				break;
			default:
				sb.append("%d");
				break;
			}
			sb.append(JrexPrintDTrace.FIELD_DELIMITER);
		}
		
		sb.append("\\n\"");
		
		for(JrexPrintArgument arg: args) {
			sb.append(", ");
			sb.append(arg.getExpression().toString());
		}
		
		sb.append(");");
		
		return sb.toString();
	}
}
