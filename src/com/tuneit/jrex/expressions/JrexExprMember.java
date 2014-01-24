package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexExprMember extends JrexExpressionFormatter {
	/**
	 * Access member of structure/union/etc. SystemTap automatically 
	 * deduces if type of object is pointer, but DTrace needs to 
	 * specify -> for pointers and . if object is non-pointer.
	 * @param object
	 * @param isPointer
	 */
	public JrexExprMember(JrexExpression object, String member, boolean isPointer) {
		super();
		this.object = object;
		this.member = member;
		this.isPointer = isPointer;
	}

	private JrexExpression object;
	private String member;
	private boolean isPointer;
	
	@Override
	void formatStapCode(Formatter formatter) {
		formatter.format("(%s)->%s", this.object.toStapCode(), this.member);
	}

	@Override
	void formatDTraceCode(Formatter formatter) {
		if(this.isPointer) {
			formatter.format("(%s)->%s", this.object.toDTraceCode(), this.member);
		}
		else {
			formatter.format("(%s).%s", this.object.toDTraceCode(), this.member);
		}
	}
	
}
