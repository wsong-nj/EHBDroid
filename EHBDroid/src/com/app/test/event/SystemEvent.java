package com.app.test.event;

public class SystemEvent {
	public Object service;
	public Object listener;
	public String registar;
	
	public SystemEvent(Object service, Object listener, String registar) {
		super();
		this.service = service;
		this.listener = listener;
		this.registar = registar;
	}

	public Object getManager() {
		return service;
	}

	public Object getListener() {
		return listener;
	}

	public String getRegistar() {
		return registar;
	}
	
}
