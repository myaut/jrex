package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexExprMemberDTrace implements JrexExpression {
	public JrexExprMemberDTrace(JrexExpression object, String member, boolean isPointer) {
		this.object = object;
		this.member = member;
		this.isPointer = isPointer;
	}

	private JrexExpression object;
	private String member;
	private boolean isPointer;
	
	@Override
	public String toString() {
		if(this.isPointer) {
			return String.format("(%s)->%s", this.object.toString(), this.member);
		}
		else {
			return String.format("(%s).%s", this.object.toString(), this.member);
		}
	}
	
}
