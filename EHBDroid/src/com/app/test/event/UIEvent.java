package com.app.test.event;

/**
 * btn.setOnClickListener(listener);
 * 
 * ui = btn;
 * listener = listener;
 * callbackName = onClick;
 *
 * */
public class UIEvent {
	
	// ui element, including two type of ui elements: View, Dialog.
	public Object ui;
	
	//listener 
	public Object listener;
	
	//callback
	public String callbackName;

	public UIEvent(Object ui, Object listener, String rm) {
		this.ui = ui;
		this.listener = listener;
		this.callbackName = rm;
	}

	public Object getListener() {
		return listener;
	}

	public Object getUi() {
		return ui;
	}
	
	public String getCallback() {
		return callbackName;
	}

	@Override
	public String toString() {
		return "UIEvent{" +
				"ui=" + ui +
				", listener=" + listener +
				", callbackName='" + callbackName + '\'' +
				'}';
	}
}
