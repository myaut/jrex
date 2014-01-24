package com.tuneit.jrex.expressions;

public class JrexExprLogical implements JrexExpression {
	public static final String AND = "&&";
	public static final String OR = "||";
	public static final String NOT = "!";
	
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
	public String toString() {
		if(this.operator.equals(JrexExprLogical.NOT)) {
			return String.format("%s(%s)", this.operator, this.leftValue.toString());
		}
		else {
			return String.format("(%s %s %s)", this.leftValue.toString(), 
								 this.operator, this.rightValue.toString());
		}
	}
}
