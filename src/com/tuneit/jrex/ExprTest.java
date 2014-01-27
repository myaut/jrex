package com.tuneit.jrex;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.expressions.JrexExpression;
import com.tuneit.jrex.expressions.JrexStatement;

public class ExprTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("---------- SystemTap ------------");
		ExprTest.generateScript(new ExprFactory(ExprFactory.SYSTEMTAP));
		
		System.out.println("------------ DTrace -------------");
		ExprTest.generateScript(new ExprFactory(ExprFactory.DTRACE));
	}

	private static void generateScript(ExprFactory ef) {
		JrexExpression requestProbe;
		requestProbe = ef.probe("hotspot.request", "hotspot$pid:::request", 
								ef.body(ef.assign(ef.localVar("string", "uri"),
												  ef.userString(ef.argument(0, false), 
														  		ef.argument(1, false)))));
		
		System.out.print(requestProbe.toString());
	}
}
