package com.app.test.event;

import android.content.IntentFilter;

public class ReceiverEvent extends SystemEvent{

	IntentFilter intentFilter;
	public ReceiverEvent(Object service, Object listener, String registar) {
		super(service, listener, registar);
	}

	public ReceiverEvent(Object service, Object listener, String registar,IntentFilter intentFilter) {
		super(service, listener, registar);
		this.intentFilter = intentFilter;
	}

	public IntentFilter getIntentFilter() {
		return intentFilter;
	}
	
}
