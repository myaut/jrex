package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexExprCompare extends JrexExpressionFormatter {
	static final String EQUAL = "==";
	static final String NOT_EQUAL = "!=";
	static final String LESS = "<";
	static final String LESS_OR_EQUAL = "<=";
	static final String MORE = ">";
	static final String MORE_OR_EQUAL = ">=";
	
	private String operator;
	private JrexExpression leftValue;
	private JrexExpression rightValue;
	
	public JrexExprCompare(String operator, JrexExpression leftValue,
			JrexExpression rightValue) {
		this.operator = operator;
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	@Override
	public void formatStapCode(Formatter formatter) {
		formatter.format("(%s %s %s)", this.leftValue.toStapCode(), 
				this.operator, this.rightValue.toStapCode());
	}

	@Override
	public void formatDTraceCode(Formatter formatter) {
		formatter.format("(%s %s %s)", this.leftValue.toDTraceCode(), 
				this.operator, this.rightValue.toDTraceCode());
	}
}
