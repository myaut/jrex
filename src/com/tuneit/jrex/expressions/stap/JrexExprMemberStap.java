package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexExpression;

public class JrexExprMemberStap  implements JrexExpression {
	public JrexExprMemberStap(JrexExpression object, String member) {
		this.object = object;
		this.member = member;
	}

	private JrexExpression object;
	private String member;
	
	@Override
	public String toString() {
		return String.format("(%s)->%s", this.object.toString(), this.member);
	}	
}
