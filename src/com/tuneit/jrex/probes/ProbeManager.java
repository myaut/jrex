package com.tuneit.jrex.probes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.expressions.JrexExpression;
import com.tuneit.jrex.expressions.JrexInvalidTool;
import com.tuneit.jrex.expressions.JrexPrintArgument;

public class ProbeManager {
	private List<Probe> allProbes;
	private ExprFactory ef;

	public ProbeManager(ExprFactory ef) throws JrexInvalidTool {
		this.allProbes = new ArrayList<Probe>();
		this.ef = ef;
		
		this.createGlassfishProbes(ef);
	}
	
	public void writeCode(PrintStream os, String... groups) throws JrexInvalidTool {
		List<String> groupsList = Arrays.asList(groups);
		
		for(Probe probe : this.allProbes) {
			if(!groupsList.contains(probe.getGroup()))
				continue;
			
			String code = probe.generateCode(this.ef);
			os.println(code);
		}
	}
	
	private void createGlassfishProbes(ExprFactory ef) throws JrexInvalidTool {
		JrexExpression argUri = ef.userString(ef.argument(0, false), 
											  ef.argument(1, false));
		JrexExpression argMethod = ef.userString(ef.argument(2, false), 
												 ef.argument(3, false));
		JrexExpression argStatus = ef.argument(4, false);
		
		ProbeAttribute attrUri = new ProbeAttribute("uri", argUri, JrexPrintArgument.FMT_STRING);
		ProbeAttribute attrMethod = new ProbeAttribute("method", argMethod, JrexPrintArgument.FMT_STRING);
		ProbeAttribute attrStatus = new ProbeAttribute("status", argStatus, JrexPrintArgument.FMT_LONG);
		
		Probe probeRequest = new Probe("glassfish", "request", 
									   "glassfish.request", 
									   "glassfish$pid:::request");
		probeRequest.addAttribute(attrUri);
		probeRequest.addAttribute(attrMethod);
		
		this.allProbes.add(probeRequest);
		
		Probe probeResponse = new Probe("glassfish", "response", 
				   					    "glassfish.response", 
				   						"glassfish$pid:::response");
		probeResponse.addAttribute(attrUri);
		probeResponse.addAttribute(attrMethod);
		probeResponse.addAttribute(attrStatus);
		
		this.allProbes.add(probeResponse);
	}
}
