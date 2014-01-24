package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexExpression;
import com.tuneit.jrex.expressions.JrexGlobalStatement;
import com.tuneit.jrex.expressions.JrexStmBody;

public class JrexProbeDTrace implements JrexGlobalStatement {
	public JrexProbeDTrace(String name, JrexExpression predicate,
			JrexStmBody body) {
		this.name = name;
		this.predicate = predicate;
		this.body = body;
	}

	private String name;
	private JrexExpression predicate;
	private JrexStmBody body;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.name);
		
		if(this.predicate != null) {
			sb.append("\n/ ");
			sb.append(this.predicate.toString());
			sb.append(" /"); 
		}
		
		sb.append("\n{ \n");
		
		sb.append(this.body.toString());
		
		sb.append("\n} \n\n");
		
		return sb.toString();
	}

}
