package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexExprLogical extends JrexExpressionFormatter {
	static final String AND = "&&";
	static final String OR = "||";
	static final String NOT = "!";
	
	private String operator;
	private JrexExpression leftValue;
	private JrexExpression rightValue;
	
	public JrexExprLogical(String operator, JrexExpression leftValue,
			JrexExpression rightValue) {
		this.operator = operator;
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	@Override
	public void formatStapCode(Formatter formatter) {
		if(this.operator.equals(JrexExprLogical.NOT)) {
			formatter.format("%s(%s)", this.operator, this.leftValue.toStapCode());
		}
		else {
			formatter.format("(%s %s %s)", this.leftValue.toStapCode(), 
					this.operator, this.rightValue.toStapCode());
		}
	}

	@Override
	public void formatDTraceCode(Formatter formatter) {
		if(this.operator.equals(JrexExprLogical.NOT)) {
			formatter.format("%s(%s)", this.operator, this.leftValue.toStapCode());
		}
		else {
			formatter.format("(%s %s %s)", this.leftValue.toDTraceCode(), 
					this.operator, this.rightValue.toDTraceCode());
		}
	}
}
