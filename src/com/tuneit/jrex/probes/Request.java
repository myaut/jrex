package com.tuneit.jrex.probes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Request {
	static boolean DEBUG = true;
	
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
		return "Request [group=" + group + ", startTime=" + startTime
				+ ", time=" + time + ", tid=" + tid + "]";
	}
	
	public boolean hasLongParam(String key) {
		return this.startEvent.hasLongParam(key);
	}
	
	public long getLongParam(String key) {
		return this.startEvent.getLongParam(key);
	}
	
	public boolean hasStringParam(String key) {
		return this.startEvent.hasStringParam(key);
	}
	
	public String getStringParam(String key) {
		return this.startEvent.getStringParam(key);
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

	public List<Request> getSubRequests() {
		return subRequests;
	}
}
