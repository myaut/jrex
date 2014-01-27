package com.tuneit.jrex.expressions.dtrace;

import com.tuneit.jrex.expressions.JrexVarGlobal;

public class JrexVarGlobalDTrace extends JrexVarGlobal {
	public JrexVarGlobalDTrace(int var) {
		super(var);
	}

	@Override
	public String toString() {
		switch(this.getVar()) {
		case JrexVarGlobal.GV_EXEC_NAME:
			return "execname";
		case JrexVarGlobal.GV_THREAD_ID:
			return "tid";
		case JrexVarGlobal.GV_PROCESS_ID:
			return "pid";
		case JrexVarGlobal.GV_CLOCK:
			return "timestamp";
		case JrexVarGlobal.GV_THREAD_STATE:
			return "curlwpsinfo->pr_state";
		}
		
		return "unknown";
	}

	@Override
	public String getType() {
		switch(this.getVar()) {
		case JrexVarGlobal.GV_EXEC_NAME:
			return "string";
		case JrexVarGlobal.GV_THREAD_ID:
			return "id_t";
		case JrexVarGlobal.GV_PROCESS_ID:
			return "pid_t";
		case JrexVarGlobal.GV_CLOCK:
			return "uint64_t";
		case JrexVarGlobal.GV_THREAD_STATE:
			return "int";
		}
		
		return "unknown";
	}
}
