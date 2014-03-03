package com.tuneit.jrex.probes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.tuneit.jrex.expressions.PrintArgument;

public class Request {
	static boolean DEBUG = false;
	
	public Request(Event startEvent, Event endEvent) {
		this.parentRequest = null;
		this.startEvent = startEvent;
		this.endEvent = endEvent;
		
		subRequests = new LinkedList<Request>();
		
		this.group = startEvent.getGroup();
		this.startTime = startEvent.getTime();
		this.time = endEvent.getTime() - startEvent.getTime();
		this.tid = startEvent.getTid();
	}
	
	private Request parentRequest;
	private Event startEvent;
	private Event endEvent;
	
	private List<Request> subRequests;
	
	private String group;
	private long startTime;
	private long time;
	private long tid;
	
	public boolean tryToAddSubRequest(Request subRequest) {
		ProbeGroup subGroup = subRequest.startEvent.getProbeGroup();
		
		if(Request.DEBUG) {
			System.out.println("Trying to bind " + subRequest + " to " + this);
			System.out.println(" canBound: " + subGroup.canBound(this.startEvent.getProbeGroup()));
			System.out.println(" bindByTid: " + subGroup.getBindTid());
		}
		
		if(!subGroup.canBound(this.startEvent.getProbeGroup()))
			return false;
		
		if(subGroup.getBindTid()) {
			if(subRequest.getTid() == this.getTid()) {
				subRequest.parentRequest = this;
				this.subRequests.add(subRequest);
				return true;
			}
		}
		else {
			String paramName = subGroup.getBindParam();
			
			if(this.hasLongParam(paramName) && subRequest.hasLongParam(paramName) &&
					this.getLongParam(paramName) == subRequest.getLongParam(paramName)) {
				subRequest.parentRequest = this;
				this.subRequests.add(subRequest);
				return true;
			}
			
			if(this.hasStringParam(paramName) && subRequest.hasStringParam(paramName) &&
					this.getStringParam(paramName).equals(subRequest.getStringParam(paramName))) {
				subRequest.parentRequest = this;
				this.subRequests.add(subRequest);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		long normStartTime = getStartTime();
		String normStartTimeStr = Long.toString(normStartTime);
		
		/* For child requests - normalize time to root's time */
		if(parentRequest != null) {
			Request parent = parentRequest;
			while(parent.getParent() != null) {
				parent = parent.getParent();
			}
			
			normStartTime -= parent.getStartTime();
			normStartTimeStr = "+" + Long.toString(normStartTime);
		}
		
		return String.format("%s/%d/%s", getGroup(), getTid(), normStartTimeStr);
	}
	
	public boolean hasLongParam(String key) {
		return this.startEvent.hasLongParam(key) || this.endEvent.hasLongParam(key);
	}
	
	public long getLongParam(String key) {
		if(this.startEvent.hasLongParam(key)) {
			return this.startEvent.getLongParam(key);
		}
		
		return this.endEvent.getLongParam(key);
	}
	
	public boolean hasStringParam(String key) {
		return this.startEvent.hasStringParam(key) || this.endEvent.hasStringParam(key);
	}
	
	public String getStringParam(String key) {
		if(this.startEvent.hasStringParam(key)) {
			return this.startEvent.getStringParam(key);
		}
		
		return this.endEvent.getStringParam(key);
	}

	public String getGroup() {
		return group;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getTime() {
		return time;
	}

	public long getTid() {
		return tid;
	}
	
	public Request getParent() {
		return parentRequest;
	}

	public List<Request> getSubRequests() {
		return subRequests;
	}
	
	public List<String> getParamNames() {
		Probe startProbe = this.startEvent.getProbe();
		Probe endProbe = this.endEvent.getProbe();
		
		ArrayList<String> params = new ArrayList<String>();
		
		for(ProbeAttribute startParam : startProbe.getAttributes()) {
			params.add(startParam.getName());
		}
		
		for(ProbeAttribute endParam : endProbe.getAttributes()) {
			if(!params.contains(endParam.getName())) {
				params.add(endParam.getName());
			}
		}
		
		return params;
	}
}
