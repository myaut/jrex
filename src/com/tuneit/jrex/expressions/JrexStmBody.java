package com.tuneit.jrex.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JrexStmBody implements JrexExpression {
	private List<JrexStatement> statements;
	
	public JrexStmBody(JrexStatement... statements) {
		this.statements = new ArrayList<JrexStatement>(Arrays.asList(statements));
	}

	@Override
	public String toStapCode() {
		StringBuilder sb = new StringBuilder();
		
		for(JrexStatement statement : this.statements) {
			sb.append(statement.toStapCode());
		}
		
		return sb.toString();
	}

	@Override
	public String toDTraceCode() {
		StringBuilder sb = new StringBuilder();
		
		for(JrexStatement statement : this.statements) {
			sb.append(statement.toStapCode());
		}
		
		return sb.toString();
	}

}
