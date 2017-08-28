package com.app.test.event;

import android.content.IntentFilter;

public class InterAppEvent {
	
	public Object component;
	public IntentFilter intentFilter;
	
	public InterAppEvent(Object component, IntentFilter intentFilter) {
		super();
		this.component = component;
		this.intentFilter = intentFilter;
	}
	
	public Object getComponent() {
		return component;
	}
	public IntentFilter getIntentFilter() {
		return intentFilter;
	}
	
}
