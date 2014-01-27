package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexVarGlobal;

public class JrexVarGlobalStap extends JrexVarGlobal {
	public JrexVarGlobalStap(int var) {
		super(var);
	}

	@Override
	public String toString() {
		switch(this.getVar()) {
		case JrexVarGlobal.GV_EXEC_NAME:
			return "execname()";
		case JrexVarGlobal.GV_THREAD_ID:
			return "tid()";
		case JrexVarGlobal.GV_PROCESS_ID:
			return "pid()";
		case JrexVarGlobal.GV_CLOCK:
			return "local_clock_ns()";
		case JrexVarGlobal.GV_THREAD_STATE:
			return "task_state(task_current())";
		}
		
		return "unknown";
	}
	

	@Override
	public String getType() {
		switch(this.getVar()) {
		case JrexVarGlobal.GV_EXEC_NAME:
			return "string";
		case JrexVarGlobal.GV_THREAD_ID:
		case JrexVarGlobal.GV_PROCESS_ID:
		case JrexVarGlobal.GV_CLOCK:
		case JrexVarGlobal.GV_THREAD_STATE:
			return "long";
		}
		
		return "unknown";
	}
}
