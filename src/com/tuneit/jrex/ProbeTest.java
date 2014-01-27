package com.tuneit.jrex;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.probes.ProbeManager;

public class ProbeTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ProbeManager pm;
		String groups[] = {"glassfish", "vcs", "ivcs"};
		PrintStream stapFile = new PrintStream("jrex.stp");
		PrintStream dtraceFile = new PrintStream("jrex.d");
		
		System.out.println("---------- SystemTap ------------");
		pm = new ProbeManager(new ExprFactory(ExprFactory.SYSTEMTAP)); 
		pm.writeCode(stapFile, groups);
		
		System.out.println("------------ DTrace -------------");
		pm = new ProbeManager(new ExprFactory(ExprFactory.DTRACE)); 
		pm.writeCode(dtraceFile, groups);
	}

}
