package com.tuneit.jrex.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JrexStmBody implements JrexExpression {
	private List<JrexStatement> statements;
	
	public JrexStmBody() {
		this.statements = new ArrayList<JrexStatement>();
	}
	
	public JrexStmBody(JrexStatement... statements) {
		this.statements = new ArrayList<JrexStatement>(Arrays.asList(statements));
	}
	
	public void addStatement(JrexStatement statement) {
		this.statements.add(statement);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(JrexStatement statement : this.statements) {
			sb.append('\t');
			sb.append(statement.toString());
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
