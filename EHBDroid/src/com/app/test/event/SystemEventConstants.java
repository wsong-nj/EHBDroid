package com.app.test.event;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.app.test.CallBack;

public class SystemEventConstants {

	public static final String[] serviceType1 = {
			CallBack.service_addGpsStatusListener,
			CallBack.service_addNmeaListener,
			CallBack.service_registerListener1,
			CallBack.service_registerListener2,
			CallBack.service_registerListener3,
			CallBack.service_registerListener4,
			CallBack.service_requestAudioFocus, 
			//ÔÝ²»¿¼ÂÇ
			CallBack.service_listen};
	public static final String[] serviceType2 = {CallBack.service_requestSingleUpdate};
	public static final String[] serviceType4 = {CallBack.service_requestLocationUpdates};
	
	public static final Set<String> extenalServicesList = CallBack.getSerViceRegistars();
	
	//listener in paramter 1
	public static final List<String> serviceType1List = Arrays.asList(serviceType1);
	//listener in paramter 2
	public static final List<String> serviceType2List = Arrays.asList(serviceType2);
	//listener in paramter 4
	public static final List<String> serviceType4List = Arrays.asList(serviceType4);
	
}
