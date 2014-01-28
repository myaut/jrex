package com.tuneit.jrex.probes;

public class ProbeGroup {
	public ProbeGroup(String name, Probe startProbe, Probe endProbe) {
		this.name = name;
		this.startProbe = startProbe;
		this.endProbe = endProbe;
		
		this.parentGroupName = null;
		this.bindTid = false;
		this.bindParam = null;
		
		startProbe.setGroup(name);
		startProbe.setStartProbe();
		endProbe.setGroup(name);
	}
	
	private String name;
	private Probe startProbe;
	private Probe endProbe;
	
	private String parentGroupName;
	private boolean bindTid;
	private String bindParam;
	
	public String getName() {
		return name;
	}
	
	public Probe getStartProbe() {
		return startProbe;
	}
	
	public Probe getEndProbe() {
		return endProbe;
	}
	
	public void bindByTid(String parentGroup) {
		this.parentGroupName = parentGroup;
		this.bindTid = true;
	}
	
	public void bindByParam(String parentGroup, String param) {
		this.parentGroupName = parentGroup;
		this.bindParam= param;
	}
	
	public boolean isRoot() {
		return this.parentGroupName == null;
	}
	
	public boolean canBound(ProbeGroup parentGroup) {
		return parentGroup.getName().equals(this.parentGroupName);
	}
	
	public boolean getBindTid() {
		return this.bindTid;
	}
	
	public String getBindParam() {
		return this.bindParam;
	}
	
	public Probe findProbe(String name) {
		if(name.equals(startProbe.getName())) {
			return startProbe;
		}
		
		if(name.equals(endProbe.getName())) {
			return endProbe;
		}
		
		return null;
	}
}
