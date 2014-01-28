package com.tuneit.jrex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.probes.Event;
import com.tuneit.jrex.probes.ProbeManager;
import com.tuneit.jrex.probes.Request;

public class ParseTest {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ExprFactory ef = new ExprFactory(ExprFactory.NOTOOL);
		ProbeManager pm = new ProbeManager(ef);
		FileReader r = new FileReader(args[0]);
		
		List<Request> requests = pm.parseLog(r);
		
		ParseTest.dumpRequests(requests, 0);
	}
	
	public static void dumpRequests(List<Request> requests, int indent) {
		for(Request request: requests) {
			for(int i = 0; i < indent; ++i)
				System.out.print(' ');
			System.out.println(request.toString());
			ParseTest.dumpRequests(request.getSubRequests(), indent + 4);
		}
	}
}
