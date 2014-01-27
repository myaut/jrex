package com.tuneit.jrex.expressions;

public abstract class JrexVarGlobal implements JrexVariable {
	public static final int GV_THREAD_ID = 0; 
	public static final int GV_PROCESS_ID = 1;
	public static final int GV_EXEC_NAME = 2;
	public static final int GV_CLOCK = 3;
	public static final int GV_THREAD_STATE = 4; 
	
	private int var;
	
	public JrexVarGlobal(int var) {
		this.var = var;
	}
	
	public int getVar() {
		return this.var;
	}
}
