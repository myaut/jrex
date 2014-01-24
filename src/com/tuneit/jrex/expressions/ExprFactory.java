package com.tuneit.jrex.expressions;

public class ExprFactory {
	static public JrexExpression argument(int index, boolean typed) {
		return new JrexArgument(index, typed);
	}
	
	static public JrexExpression argument(String name, int index, boolean typed) {
		return new JrexArgument(name, index, typed);
	}
	
	static public JrexExpression compare(String operator, JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(operator, leftValue, rightValue);
	}
	
	static public JrexExpression member(JrexExpression object, String member, boolean isPointer) {
		return new JrexExprMember(object, member, isPointer);
	}
	
	static public JrexExpression user(JrexExpression argument) {
		return new JrexGetUserString(argument);
	}
	
	static public JrexExpression userString(JrexExpression argument, JrexExpression maxLength) {
		return new JrexGetUserString(argument, maxLength);
	}
}
