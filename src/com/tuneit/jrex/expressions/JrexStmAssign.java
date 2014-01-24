package com.tuneit.jrex.expressions;

import java.util.Formatter;

public class JrexStmAssign extends JrexExpressionFormatter {
	private JrexVarLocal var;
	private JrexExpression value;
	
	public JrexStmAssign(JrexVarLocal var, JrexExpression value) {
		this.var = var;
		this.value = value;
	}

	@Override
	void formatStapCode(Formatter formatter) {
		formatter.format("%s = %s;", this.var.toStapCode(), this.value.toStapCode());
	}

	@Override
	void formatDTraceCode(Formatter formatter) {
		formatter.format("%s = %s;", this.var.toDTraceCode(), this.value.toDTraceCode());
	}

}
