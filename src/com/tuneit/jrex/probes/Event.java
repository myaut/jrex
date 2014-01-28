package com.tuneit.jrex.probes;

import java.util.HashMap;

public class Event implements Comparable {
	public Event(String group, String name, long time, long tid) {
		this.group = group;
		this.name = name;
		this.time = time;
		this.tid = tid;
		
		this.probe = null;
		this.probeGroup = null;
		
		this.longParams = new HashMap<String, Long>();
		this.stringParams = new HashMap<String, String>();
	}
	
	private String group;
	private String name;
	private long time;
	private long tid;
	
	private Probe probe;
	private ProbeGroup probeGroup;
	
	private HashMap<String, Long> longParams;
	private HashMap<String, String> stringParams;
	
	public void addLongParam(String name, long value) {
		this.longParams.put(name, value);
	}
	
	public void addStringParam(String name, String value) {
		this.stringParams.put(name, value);
	}
	
	@Override
	public String toString() {
		return "Event [group=" + group + ", name=" + name + ", time=" + time
				+ ", tid=" + tid + "]";
	}

	public void setProbe(Probe probe, ProbeGroup probeGroup) {
		this.probe = probe;
		this.probeGroup = probeGroup;
	}
	
	public Probe getProbe() {
		return probe;
	}

	public ProbeGroup getProbeGroup() {
		return probeGroup;
	}

	@Override
	public int compareTo(Object o) {
		Event otherEvent = (Event) o;
		
		if(!otherEvent.group.equals(this.group))
			return 1;
		
		/* TODO: Check parameters? */
		
		return (int) (otherEvent.tid - this.tid);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.compareTo(o) == 0;
	}

	public String getGroup() {
		return group;
	}

	public long getTime() {
		return time;
	}

	public long getTid() {
		return tid;
	}
	
	public boolean hasLongParam(String key) {
		return this.longParams.containsKey(key);
	}
	
	public long getLongParam(String key) {
		return this.longParams.get(key);
	}
	
	public String getStringParam(String key) {
		return this.stringParams.get(key);
	}
	
	public boolean hasStringParam(String key) {
		return this.stringParams.containsKey(key);
	}
}
