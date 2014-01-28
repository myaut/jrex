package com.tuneit.jrex.expressions;

import java.util.List;

import com.tuneit.jrex.expressions.notool.*;
import com.tuneit.jrex.expressions.stap.*;
import com.tuneit.jrex.expressions.dtrace.*;

public class ExprFactory {
	public static final int NOTOOL = 0;
	public static final int DTRACE = 1;
	public static final int SYSTEMTAP = 2;
	
	private int tool;
	
	public ExprFactory(int tool) {
		this.tool = tool;
	}
	
	public JrexExpression pair(String systemTapString, String dtraceString) {
		if(this.tool == ExprFactory.SYSTEMTAP) {
			return new JrexString(systemTapString);
		}
		else if(this.tool == ExprFactory.DTRACE) {
			return new JrexString(dtraceString);
		}
		
		return new JrexExprNull();
	}
	
	/**
	 * @param predicate (optional)
	 */
	public JrexGlobalStatement probe(String systemTapName, String dtraceName, JrexStmBody body, 
							    JrexExpression predicate)  {
		if(this.tool == ExprFactory.SYSTEMTAP) {
			return new JrexProbeStap(systemTapName, predicate, body);
		}
		else if(this.tool == ExprFactory.DTRACE) {
			return new JrexProbeDTrace(dtraceName, predicate, body);
		}
		
		return new JrexGlobalStatementNull();
	}
	
	public JrexGlobalStatement probe(String systemTapName, String dtraceName, 
					JrexStmBody body)  {
		return this.probe(systemTapName, dtraceName, body, null);
	}
	
	public JrexStmBody body(JrexStatement... statements) {
		return new JrexStmBody(statements);
	}
	
	public JrexStmBody body(JrexStatement statement) {
		JrexStatement statements[] = { statement };
		
		return new JrexStmBody(statements);
	}
	
	public JrexStmBody body() {
		return new JrexStmBody();
	}
	
	/* Compare expressions */
	
	public JrexExprCompare equal(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.EQUAL, leftValue, rightValue);
	}
	
	public JrexExprCompare notEqual(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.NOT_EQUAL, leftValue, rightValue);
	}
	
	public JrexExprCompare less(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.LESS, leftValue, rightValue);
	}
	
	public JrexExprCompare lessOrEqual(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.LESS_OR_EQUAL, leftValue, rightValue);
	}
	
	public JrexExprCompare more(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.MORE, leftValue, rightValue);
	}
	
	public JrexExprCompare moreOrEqual(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprCompare(JrexExprCompare.MORE_OR_EQUAL, leftValue, rightValue);
	}
	
	/* Logical expressions */
	
	public JrexExpression and(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprLogical(JrexExprLogical.AND, leftValue, rightValue);
	}
	
	public JrexExpression or(JrexExpression leftValue, JrexExpression rightValue) {
		return new JrexExprLogical(JrexExprLogical.OR, leftValue, rightValue);
	}
	
	public JrexExpression not(JrexExpression leftValue) {
		return new JrexExprLogical(JrexExprLogical.NOT, leftValue, null);
	}
	
	public JrexExpression localVar(String type, String name) {
		if(this.tool == ExprFactory.SYSTEMTAP) {
			if(type != "string")
				type = "long";
			return new JrexVarLocalStap(type, name);
		}
		else if(this.tool == ExprFactory.DTRACE) {
			return new JrexVarLocalDTrace(type, name);
		}
		
		return new JrexExprNull();
	}
	
	public JrexVariable globalVar(int id) {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexVarGlobalStap(id);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexVarGlobalDTrace(id);
		
		return new JrexVarNull();
	}
	
	public JrexStatement assign(JrexExpression var, JrexExpression value) {
		return new JrexStmAssign(var, value);
	}
	
	public JrexExpression argument(String name, int index, boolean typed) {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexArgumentStap(name, index);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexArgumentDTrace(index, typed);
		
		return new JrexExprNull();
	}
	
	public JrexExpression argument(int index, boolean typed) {
		return this.argument(null, index, typed);
	}
	
	/**
	 * Access member of structure/union/etc. SystemTap automatically 
	 * deduces if type of object is pointer, but DTrace needs to 
	 * specify -> for pointers and . if object is non-pointer.
	 * @param object
	 * @param isPointer
	 */
	public JrexExpression member(JrexExpression object, String member, boolean isPointer) {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexExprMemberStap(object, member);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexExprMemberDTrace(object, member, isPointer);
		
		return new JrexExprNull();
	}
	
	public JrexExpression userString(JrexExpression argument, JrexExpression maxLength) {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexGetUserStringStap(argument, maxLength);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexGetUserStringDTrace(argument, maxLength);
		
		return new JrexExprNull();
	}

	public JrexStatement print(String prefix, List<PrintArgument> args) {
		if(this.tool == ExprFactory.SYSTEMTAP)
			return new JrexPrintStap(prefix, args);
		else if(this.tool == ExprFactory.DTRACE)
			return new JrexPrintDTrace(prefix, args);
		
		return new JrexStmNull();
	}
	
	public TracingState tracingState(String name, JrexVariable... keys) {
		JrexVariable stateVar;
		JrexGlobalStatement stateDecl;
		
		JrexStatement enableStm, disableStm;
		JrexExprCompare checkExpr;
		
		if(this.tool == ExprFactory.SYSTEMTAP) {
			stateVar = new JrexVarAssociativeArray(name, keys);
			stateDecl = new JrexStmAssociativeArrayStap(name); 
		}
		else if(this.tool == ExprFactory.DTRACE) {
			if(keys.length == 1 && keys[0] instanceof JrexVarGlobal &&
					((JrexVarGlobal) keys[0]).getVar() == JrexVarGlobal.GV_THREAD_ID) {
				/* Use thread-clause variable here */
				stateVar = new JrexVarThreadDTrace("int", name);
				stateDecl = new JrexGlobalStatementNull();
			}
			else {
				stateVar = new JrexVarAssociativeArray(name, keys);
				stateDecl = new JrexStmAssociativeArrayDTrace("int", name, keys);
			}
		}
		else {
			return new TracingState(new JrexGlobalStatementNull(), 
									new JrexStmNull(), new JrexStmNull(), 
									this.equal(new JrexExprNull(), new JrexExprNull()));
		}
		
		enableStm = new JrexStmAssign(stateVar, new JrexString("1"));
		disableStm = new JrexStmAssign(stateVar, new JrexString("0"));
		checkExpr = this.equal(stateVar, new JrexString("1"));
		
		return new TracingState(stateDecl, enableStm, disableStm, checkExpr);
	}
}
