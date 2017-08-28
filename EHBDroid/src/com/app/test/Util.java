package com.app.test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * A class inserted into App.
 * */
public class Util {
	public static String[] unAnalyzeMethod = { CallBack.ONDRAG,
			CallBack.ONPREFERENCETREECLICK, CallBack.onInflate,
			CallBack.ONEDITORACTION, CallBack.ONKEY, CallBack.ONCHILDVIEWADDED,
			CallBack.ONCHILDVIEWREMOVED, CallBack.DialogInterface_onKey,
			CallBack.ONCREATECONTEXTMENU};
	
	public static List<String> unAnalyzeList = Arrays.asList(unAnalyzeMethod);
	
	public final static int viewId = 0;
	public final static int dialogId = 1;
	/**
	 * log list events, default callback is "onListItemClicked".
	 * @param eventHandler listActivity, invoker of onListItemClicked, 
	 * @return log list events.
	 * */
	public static void LogList(Object eventHandler){
		String info = "List Event in Activity: "+eventHandler.getClass().getName()+" callBack: onListItemClick";
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	public static void LogPreference(Object eventHandler){
		String info = "Preference Event in PreferenceActivity: "+eventHandler.getClass().getName();
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	/**
	 * log view, dialog and menuItem's events.
	 * @param uiElement UI elements: view, dialog or menuItem
	 * @param eventHandler Event handler of o;
	 * @param callback name of method invoking o;
	 * @return log events. Tag is EVENT.
	 * */
	public static void Log(Object uiElement, Object eventHandler, String callBack){
		String info = "";
		if(uiElement==null||eventHandler==null||callBack==null){
			throw new RuntimeException("Input parameters are invalid.");
		}
		if(uiElement instanceof View){
			View v = (View)uiElement;
			int id = v.getId();
			Context context = v.getContext();
			String activityName = context.getClass().getName();
			info = info+" View Event in Activity: "+activityName+" id: "+id;
		}
		else if(uiElement instanceof Dialog){
			Dialog dialog = (Dialog)uiElement;
			String activityName = dialog.getContext().getClass().getName();
			info = info+" Dialog Event in Activity: "+activityName;
		}
		else if(uiElement instanceof MenuItem){
			MenuItem menuItem = (MenuItem)uiElement;
			info = info+" Menu Event MenuItem: "+menuItem.getTitle();
		}
		String eventHandlerName = eventHandler.getClass().getName();
		info = info+" eventHandler: "+eventHandlerName+" callBack: "+callBack+" Happens!!!";
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	/**
	 * Log BroadCast Receiver event.
	 * @param broadcastReceiver first parameter of onReceiver(BroadcastReceiver, Intent);
	 * @param intent second parameter of onReceiver(BroadcastReceiver, Intent);
	 * */
	public static void LogReceiverEvent(BroadcastReceiver broadcastReceiver,Intent intent){
		String receiverName = broadcastReceiver.getClass().getName();
		String action = intent.getAction();
		String info = "BroadCastReceiver Event Receiver Name: "+receiverName+" Action: "+action+" Happens!!!!!!";
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	public static void LogInterAppEvent(Activity activity,Intent intent){
		String receiverName = activity.getClass().getName();
		String action = intent.getAction();
		String info = "InterApp Event target name: "+receiverName+" Action: "+action+" Happens!!!!!!";
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	/**
	 * Log service event.
	 * @param m callback of event handler.
	 * @param manager Manager manage service event.
	 * @param listener event handler of m.
	 * @return log the three info.
	 * */
	public static void LogServiceEvent(Method m,Object manager,Object listener){
		String method = m.getName();
		String managerName = manager.getClass().getName();
		String listenerName = listener.getClass().getName();
		String info = "Service Event Manager: "+managerName+" Listener: "+listenerName+" Method: "+method+" Happens!!!!!!";
		android.util.Log.v(Constants.LogTag.eventTag, info);
	}
	
	public static void doPreferenceTest(Activity activity){
		PreferenceActivity pActivity = (PreferenceActivity)activity;
		PreferenceScreen pScreen = pActivity.getPreferenceScreen();
//		int cou = pScreen.getPreferenceCount();
		ListView listView = pActivity.getListView();
		int count = listView.getCount();
		Log.v(Constants.LogTag.eventTag, "listView count: "+count+" listView child size: "+listView.getChildCount());
		for(int i=0;i<count;i++){
			pScreen.onItemClick(listView, null, i, 0);
		}
	}
	
	/**
	 * When an exception occurs, out print it.
	 * */
	public static void LogException(Exception e){
		if(e instanceof InvocationTargetException){
			InvocationTargetException ite = (InvocationTargetException)e;
			Throwable targetException = ite.getTargetException();
			if(targetException instanceof NoSuchFieldException){
				return;
			}
			android.util.Log.v(Constants.LogTag.eventTag,"Bug Detected, InvocationTargetException!!! Message: "+e.getMessage()+" Caused by: "+e.getCause());
			targetException.printStackTrace();
		}
		else if(e instanceof NoSuchFieldException){
			//NoSuchFieldException is introduced by us, so ignore it.
		}
		else {
			android.util.Log.v(Constants.LogTag.eventTag,"Bug Detected!!! Message: "+e.getMessage()+" Caused by "+e.getCause());
			e.printStackTrace();
		}
	}
	
	public static String getSubsignature(Method m){
		Class<?>[] parameterTypes = m.getParameterTypes();
		String returnValue = m.getReturnType().getName();
		String sig = returnValue+" "+m.getName()+"(";
		StringBuilder sb = new StringBuilder(sig);
		for(int i=0;i<parameterTypes.length;i++){
			sb.append(parameterTypes[i].getName());
			if(i+1<parameterTypes.length){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * return an method whose sub signature equals to the given subSignature in class.
	 * */
	public static Method getMethod(Class declaringClass,String subSignature){
		Method[] methods = declaringClass.getMethods();
		for(Method m:methods){
			if(subSignature.equals(getSubsignature(m))){
				return m;
			}
		}
		throw new RuntimeException("Class "+declaringClass+" does not contain method: "+subSignature);
	}
	
	public static void outPrint(String s){
		android.util.Log.v(Constants.LogTag.sysoutTag, s);
	}
	
	/**
	 * read CallBack.idToCallBack from AdobeReader.txt
	 * */
	private static Map<String, List<String>> readCallBack(String location) {
		try{
			FileInputStream fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			Map<String, List<String>> viewToCallBacks = (Map<String, List<String>>) o;
			ois.close();
			fis.close();
			return viewToCallBacks;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
