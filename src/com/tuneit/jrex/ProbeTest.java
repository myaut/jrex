package com.tuneit.jrex;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.expressions.JrexInvalidTool;
import com.tuneit.jrex.probes.ProbeManager;

public class ProbeTest {

	/**
	 * @param args
	 * @throws JrexInvalidTool 
	 */
	public static void main(String[] args) throws JrexInvalidTool {
		ProbeManager pm;
		String groups[] = {"glassfish"};
		
		System.out.println("---------- SystemTap ------------");
		pm = new ProbeManager(new ExprFactory(ExprFactory.SYSTEMTAP)); 
		pm.writeCode(System.out, groups);
		
		System.out.println("------------ DTrace -------------");
		pm = new ProbeManager(new ExprFactory(ExprFactory.DTRACE)); 
		pm.writeCode(System.out, groups);
	}

}
