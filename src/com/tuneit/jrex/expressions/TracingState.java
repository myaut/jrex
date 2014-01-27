package com.tuneit.jrex.expressions;

public class TracingState {
	/* Simple holder for expressions, actually implemented by ExprFactory */
	
	public TracingState(JrexGlobalStatement declaration,
			JrexStatement enableStm, JrexStatement disableStm, JrexExprCompare checkExpr) {
		this.declaration = declaration;
		this.enableStm = enableStm;
		this.disableStm = disableStm;
		this.checkExpr = checkExpr;
	}
	private JrexGlobalStatement declaration;
	private JrexStatement enableStm;
	private JrexStatement disableStm;
	private JrexExprCompare checkExpr;
	
	public JrexGlobalStatement getDeclaration() {
		return declaration;
	}
	
	public JrexStatement getEnableExpr() {
		return enableStm;
	}
	
	public JrexStatement getDisableExpr() {
		return disableStm;
	}
	
	public JrexExprCompare getCheckExpr() {
		return checkExpr;
	}
}
