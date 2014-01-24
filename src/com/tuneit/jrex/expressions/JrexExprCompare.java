package com.tuneit.jrex.expressions;

public class JrexExprCompare implements JrexExpression {
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
	public String toString() {
		return String.format("(%s %s %s)", this.leftValue.toString(), 
							 this.operator, this.rightValue.toString());
	}

}
