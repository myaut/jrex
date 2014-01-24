package com.tuneit.jrex.expressions;

import com.tuneit.jrex.expressions.stap.*;
import com.tuneit.jrex.expressions.dtrace.*;

public class ExprFactory {
	public static final int DTRACE = 0;
	public static final int SYSTEMTAP = 1;
	
	private int tool;
	
	public ExprFactory(int tool) {
		this.tool = tool;
	}
	
	public JrexExpression pair(String systemTapString, String dtraceString) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP) {
			return new JrexString(systemTapString);
		}
		else if(this.tool == ExprFactory.DTRACE) {
			return new JrexString(dtraceString);
		}
		
		throw new JrexInvalidTool();
	}
	
	/**
	 * @param predicate (optional)
	 * @throws JrexInvalidTool 
	 */
	public JrexExpression probe(String systemTapName, String dtraceName, JrexStmBody body, 
							    JrexExpression predicate) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP) {
			return new JrexProbeStap(systemTapName, predicate, body);
		}
		else if(this.tool == ExprFactory.DTRACE) {
			return new JrexProbeDTrace(dtraceName, predicate, body);
		}
		
		throw new JrexInvalidTool();
	}
	
	public JrexExpression probe(String systemTapName, String dtraceName, 
					JrexStmBody body)  throws JrexInvalidTool {
		return this.probe(systemTapName, dtraceName, body, null);
	}
	
	public JrexStmBody body(JrexStatement... statements) {
		return new JrexStmBody(statements);
	}
	
	public JrexStmBody body(JrexStatement statement) {
		JrexStatement statements[] = { statement };
		
		return new JrexStmBody(statements);
	}
	
	public JrexExpression compare(String operator, JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(operator, leftValue, rightValue);
	}
	
	public JrexExpression logical(String operator, JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprLogical(operator, leftValue, rightValue);
	}
	
	public JrexExpression localVar(String name) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexVarLocalStap(name);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexVarLocalDTrace(name);
		
		throw new JrexInvalidTool();
	}
	
	public JrexStatement assign(JrexExpression var, JrexExpression value) {
		return new JrexStmAssign(var, value);
	}
	
	public JrexExpression argument(String name, int index, boolean typed) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexArgumentStap(name, index);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexArgumentDTrace(index, typed);
		
		throw new JrexInvalidTool();
	}
	
	public JrexExpression argument(int index, boolean typed) throws JrexInvalidTool {
		return this.argument(null, index, typed);
	}
	
	/**
	 * Access member of structure/union/etc. SystemTap automatically 
	 * deduces if type of object is pointer, but DTrace needs to 
	 * specify -> for pointers and . if object is non-pointer.
	 * @param object
	 * @param isPointer
	 */
	public JrexExpression member(JrexExpression object, String member, boolean isPointer) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexExprMemberStap(object, member);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexExprMemberDTrace(object, member, isPointer);
		
		throw new JrexInvalidTool();
	}
	
	public JrexExpression userString(JrexExpression argument, JrexExpression maxLength) throws JrexInvalidTool {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexGetUserStringStap(argument, maxLength);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexGetUserStringDTrace(argument, maxLength);
		
		throw new JrexInvalidTool();
	}


}
