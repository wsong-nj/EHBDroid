package com.app.test.event;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import soot.Unit;
import soot.jimple.InvokeStmt;
import soot.jimple.SpecialInvokeExpr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.app.test.AppDir;
import com.app.test.CallBack;
import com.app.test.Constants;
import com.app.test.Constants.EHBField;
import com.app.test.Util;
import com.app.test.data.AndroidIntentFilter;

/**
 * Trigger System events from Manifest.xml and code.
 * */
public class SystemEventHandler{

	public static final String itemName = Constants.sysTest; 
	public static String file = AppDir.file;
	
	/**
	 * @param activity used as onReceiver's context. 
	 * */
	public static void doSystemEventTest(Activity activity){
		try {
			
			//read mainActivity from file. 
			Class mainActivity = Class.forName(readMainActivity(file));
			Field serviceOrReceivers = mainActivity.getField(EHBField.SYSTEMEVENTLINKEDLIST);
			Object object = serviceOrReceivers.get(null);
			LinkedList<SystemEvent> systemEventLinkedList = (LinkedList<SystemEvent>)object;
			
			//1. invoke system events from linkedList
			for(SystemEvent systemEvent:systemEventLinkedList){
				Object listener = systemEvent.getListener();
				String registar = systemEvent.getRegistar();
				Object manager = systemEvent.getManager();
				//trigger receiver events from code
				if(systemEvent instanceof ReceiverEvent){
					ReceiverEvent receiverSyetemEvent = (ReceiverEvent)systemEvent;
					IntentFilter intentFilter = receiverSyetemEvent.getIntentFilter();
					BroadcastReceiver broadcastReceiver = (BroadcastReceiver)listener;
					Intent intent = initIntent(intentFilter);
					if(intent!=null){
						Util.LogReceiverEvent(broadcastReceiver, intent);
						broadcastReceiver.onReceive(activity, intent);
//						activity.sendBroadcast(intent);
					}
				}
				//trigger service events from code
				else{
					if(CallBack.serviceToCallBacks.containsKey(registar)){
						List<String> list = CallBack.serviceToCallBacks.get(registar);
						for(Method m:listener.getClass().getMethods()){
							String subsignature = Util.getSubsignature(m);
							if(list.contains(subsignature)){
								Util.LogServiceEvent(m, manager, listener);
								doServiceAnalysis(m, manager, listener);
							}
						}
					}
				}
			}
			
			//2. invoke system events from XML, only consider receiver events
			Map<String, List<AndroidIntentFilter>> receiverToFilters = readReceiverToFilters(file);
			for(String key:receiverToFilters.keySet()){
				for(AndroidIntentFilter value:receiverToFilters.get(key)){
					Class receiverClass = Class.forName(key);
					Object receiver = receiverClass.newInstance();
					BroadcastReceiver broadcastReceiver = (BroadcastReceiver)receiver;
					Intent intent = initIntent(value);
					Util.LogReceiverEvent(broadcastReceiver, intent);
					broadcastReceiver.onReceive(activity, intent);
//					activity.sendBroadcast(intent,Perm);
				}
			}
		} catch (Exception e) {
			Util.LogException(e);
		}finally{
			Log.v("EVENT", AppDir.visitedMethodCount+"");
		}
	}
	
	public static void addMenuItem(final Activity activity,Menu menu){
		MenuItem add = menu.add(itemName);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				doSystemEventTest(activity);
				return true;
			}
		});
	}
	
	//´«¸ÐÆ÷µ¥¶À¿¼ÂÇ
	/**
	 * trigger service events
	 * @param m Method to be Triggered.
	 * @param manager system managers used to get sensorList, providers, like LocationManager, SensorManager, AudioManager and TelephoneManager.
	 * @param listener m's declaring class.
	 * @return event triggered.
	  */
	public static void doServiceAnalysis(Method m, Object manager, Object listener){
		String mName = m.getName();
		SensorManager sManager = null;
		LocationManager lManager = null;
		if(manager instanceof SensorManager){
			 sManager =(SensorManager) manager;
		}
		else if(manager instanceof LocationManager){
		     lManager = (LocationManager)manager;
		}
		//6 listener, 10 methods
		try {
			//SensorEventListener: public void onSensorChanged(SensorEvent event);
			if(mName.equals("onSensorChanged")){
				//TODO
			}
			//public void onAccuracyChanged(Sensor sensor, int accuracy);  
			else if(mName.equals("onAccuracyChanged")){
				List<Sensor> sensorList = sManager.getSensorList(Sensor.TYPE_ALL);
				for(Sensor sensorEvent:sensorList){
//					public static final int SENSOR_STATUS_UNRELIABLE = 0;
//				    public static final int SENSOR_STATUS_ACCURACY_LOW = 1;
//				    public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;
//				    public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;
					for(int accuracy = 0;accuracy<3;accuracy++){
						Object[] args = {sensorEvent,accuracy};
						m.invoke(listener, args);
					}
				}
			}
			//OnAudioFocusChangeListener: public void onAudioFocusChange(int focusChange);
			else if(mName.equals("onAudioFocusChange")){
				int[] events = {AudioManager.AUDIOFOCUS_GAIN,AudioManager.AUDIOFOCUS_LOSS,AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK};
				for(int event:events){
					Object[] args = {event};
					m.invoke(listener, args);
				}
			}
			//Listener: void onGpsStatusChanged(int event);
			else if(mName.equals("onGpsStatusChanged")){
				int[] events = {GpsStatus.GPS_EVENT_STARTED,GpsStatus.GPS_EVENT_STOPPED,GpsStatus.GPS_EVENT_FIRST_FIX,GpsStatus.GPS_EVENT_SATELLITE_STATUS};
				for(int event:events){
					Object[] args = {event};
					m.invoke(listener, args);
				}
			}
			//NmeaListener: void onNmeaReceived(long timestamp, String nmea);
			else if(mName.equals("onNmeaReceived")){
				long timestamp = 1l;
				String nmea = "HelloWorld";
				Object[] args = {timestamp,nmea};
				m.invoke(listener, args);
			}
			//LocationListener: void onLocationChanged(Location location);
			else if(mName.equals("onLocationChanged")){
				List<String> allProviders = lManager.getAllProviders();
				if(allProviders==null)
					return ;
				for(String provider:allProviders){
					Location location = lManager.getLastKnownLocation(provider);
					Object[] args = {location};
					m.invoke(listener, args);
				}
			}
			//all onStatusChanged method are empty. we can ignore them 
			//void onStatusChanged(String provider, int status, Bundle extras);
			else if(mName.equals("onStatusChanged")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					int statuss[] = {LocationProvider.OUT_OF_SERVICE,LocationProvider.AVAILABLE,LocationProvider.TEMPORARILY_UNAVAILABLE};
					Bundle extras = null;
					for(int status:statuss){
						Object[] args = {provider,status,extras};
						m.invoke(listener, args);
					}
				}
			}
			//void onProviderEnabled(String provider);
			else if(mName.equals("onProviderEnabled")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					Object[] args = {provider};
					m.invoke(listener, args);
				}
			}
			//void onProviderDisabled(String provider);
			else if(mName.equals("onProviderDisabled")){
				List<String> allProviders = lManager.getAllProviders();
				for(String provider:allProviders){
					Object[] args = {provider};
					m.invoke(listener, args);
				}
			}
			//PhoneStateListener: TODO
//			else if(mName.equals("onGpsStatusChanged")){
//				int event;
//				Object[] args = {event};
//				m.invoke(listener, args);
//			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * according to intentFilter's data build an intent.
	 * @param intentFilter intent filter comes from manifest.xml.
	 * @return intent An intent can start receiver events.
	 * */
	private static Intent initIntent(AndroidIntentFilter intentFilter) {
		String action = null;
		
		if(intentFilter.countActions()==0){
			return null;
		}
		for(String mAction: intentFilter.getmActions()){
			if(!mAction.equals(Intent.ACTION_MAIN)){
				action = mAction;
				break;
			}
		}
		if(action==null) {
			Log.v("EVENT", " Intent Action is null");
			return null;
		};
		
		Intent intent = new Intent(action);
		if(intentFilter.countCategories()>0){
			intent.addCategory(intentFilter.getCategory(0));
		}
//		else 
//			intent.addCategory(Intent.CATEGORY_DEFAULT);
		if(intentFilter.countDataTypes()>0){
			intent.setType(intentFilter.getDataType(0));
		}
		return intent;
	}
	
	private static Intent initIntent(IntentFilter intentFilter) {
		String action;
		if(intentFilter.countActions()>1){
			action = intentFilter.getAction(1);
		}
		else if(intentFilter.countActions()>0){
			action = intentFilter.getAction(0);
		}
		else {
			Log.v("EVENT", " Intent Action is null");
			return null;
		}
		Intent intent = new Intent(action);
		if(intentFilter.countCategories()>0){
			intent.addCategory(intentFilter.getCategory(0));
		}
//		else 
//			intent.addCategory(Intent.CATEGORY_DEFAULT);
		if(intentFilter.countDataTypes()>0){
			intent.setType(intentFilter.getDataType(0));
		}
		return intent;
	}
	
	private static Map<String, List<AndroidIntentFilter>> readReceiverToFilters(String file){
		Map<String, List<AndroidIntentFilter>> receiverToFilters = new HashMap<String, List<AndroidIntentFilter>>();
		try{
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			//read the forth object. There are four objects in location: callback.class, activityToIntentFilters, serviceToIntentFilters and receiverToIntentFilters
			ois.readObject();
			ois.readObject();
			ois.readObject();
			Object readObject4 = ois.readObject();
			receiverToFilters = (Map<String, List<AndroidIntentFilter>>)readObject4;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return receiverToFilters;
	}
	
	private static String readMainActivity(String file){
		String mainActivity = "";
		try{
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ois.readObject();
			ois.readObject();
			ois.readObject();
			ois.readObject();
			Object readObject5 = ois.readObject();
			mainActivity = (String)readObject5;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mainActivity;
	}

//	public void doTest(Activity activity) {
//		doSystemEventTest(activity);
//	}
}
