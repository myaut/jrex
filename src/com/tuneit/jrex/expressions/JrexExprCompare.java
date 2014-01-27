package com.tuneit.jrex.expressions;

public class JrexExprCompare implements JrexExpression {
	public static final String EQUAL = "==";
	public static final String NOT_EQUAL = "!=";
	public static final String LESS = "<";
	public static final String LESS_OR_EQUAL = "<=";
	public static final String MORE = ">";
	public static final String MORE_OR_EQUAL = ">=";
	
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
	
	
	public JrexExpression not() {
		String operatorNot = "???";
		
		switch(this.operator) {
		case EQUAL:
			operatorNot = NOT_EQUAL;
			break;
		case NOT_EQUAL:
			operatorNot = EQUAL;
			break;
		case LESS:
			operatorNot = MORE_OR_EQUAL;
			break;
		case LESS_OR_EQUAL:
			operatorNot = MORE;
			break;
		case MORE:
			operatorNot = LESS_OR_EQUAL;
			break;
		case MORE_OR_EQUAL:
			operatorNot = LESS;
			break;
		}
		
		return new JrexExprCompare(operatorNot, this.leftValue, this.rightValue);
	}

}
