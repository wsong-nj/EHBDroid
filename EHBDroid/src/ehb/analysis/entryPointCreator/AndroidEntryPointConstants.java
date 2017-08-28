package ehb.analysis.entryPointCreator;
/*******************************************************************************
 * Copyright (c) 2012 Secure Software Engineering Group at EC SPRIDE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors: Christian Fritz, Steven Arzt, Siegfried Rasthofer, Eric
 * Bodden, and others.
 ******************************************************************************/

import java.util.Arrays;
import java.util.List;

/**
 * Class containing constants for the well-known Android lifecycle methods
 */
public class AndroidEntryPointConstants {
	
	public static final String ONCLICKLISTENER = "android.view.View$OnClickListener";
	public static final String ONPREFERENCECLICKLISTENER = "android.preference.Preference$OnPreferenceClickListener";
	
	//class name
	public static final String ACTIVITYCLASS = "android.app.Activity";
	public static final String SERVICECLASS = "android.app.Service";
	public static final String BROADCASTRECEIVERCLASS = "android.content.BroadcastReceiver";
	public static final String CONTENTPROVIDERCLASS = "android.content.ContentProvider";
	public static final String APPLICATIONCLASS = "android.app.Application";	
	public static final String FRAGMENT = "android.support.v4.app.Fragment";
	
	//method name
	public static final String ACTIVITY_ONCREATE = "void onCreate(android.os.Bundle)";
	public static final String ACTIVITY_ONSTART = "void onStart()";
	public static final String ACTIVITY_ONRESTOREINSTANCESTATE = "void onRestoreInstanceState(android.os.Bundle)";
	public static final String ACTIVITY_ONPOSTCREATE = "void onPostCreate(android.os.Bundle)";
	public static final String ACTIVITY_ONRESUME = "void onResume()";
	public static final String ACTIVITY_ONPOSTRESUME = "void onPostResume()";
	public static final String ACTIVITY_ONCREATEDESCRIPTION = "java.lang.CharSequence onCreateDescription()";
	public static final String ACTIVITY_ONSAVEINSTANCESTATE = "void onSaveInstanceState(android.os.Bundle)";
	public static final String ACTIVITY_ONPAUSE = "void onPause()";
	public static final String ACTIVITY_ONSTOP = "void onStop()";
	public static final String ACTIVITY_ONRESTART = "void onRestart()";
	public static final String ACTIVITY_ONDESTROY = "void onDestroy()";
	
	//view
	public static final String VIEW_ONFINISHINFLATE = "void onFinishInflate()";
	
	//fragment
	public static final String FRAGMENT_ONCREATEVIEW = "android.view.View onCreateView(android.view.LayoutInflater," +
			"android.view.ViewGroup,android.os.Bundle)";
	public static final String FRAGMENT_ONATTACH = "void onAttach(android.app.Activity)";        
	public static final String FRAGMENT_ONVIEWCREATED = "void onViewCreated(android.view.View,android.os.Bundle)";
	public static final String FRAGMENT_ONACTIVITYCREATED = "void onActivityCreated(android.os.Bundle)";
	public static final String FRAGMENT_ONCREATEANIMATOR = "android.animation.Animator onCreateAnimator(int,boolean,int)";
	
	//callback
	public static final String[] CALLBACKS = {ONCLICKLISTENER,ONPREFERENCECLICKLISTENER}; 
	
	//onCreateDialog
	public static final String ACTIVITY_ONCREATEDIALOG ="android.app.Dialog onCreateDialog(int)";	
	
	//click
	public static final String ACTIVITY_ONLISTITEMCLICK = "void onListItemClick(android.widget.ListView,android.view.View,int,long)";
	public static final String ACTIVITY_ONCLICK = "void onClick(android.view.View)";
	public static final String ACTIVITY_ONITEMCLICK = "void onItemClick(android.widget.AdapterView,android.view.View,int,long)";
	public static final String ACTIVITY_ONCLICKDIALOG = "void onClick(android.content.DialogInterface,int)";
	public static final String ONPREFERENCETREECLICK = "boolean onPreferenceTreeClick(android.preference.PreferenceScreen,android.preference.Preference)";
	
	//ContextMenu and OptionMenu
	public static final String ACTIVITY_ONCREATEOPTIONSMENU = "boolean onCreateOptionsMenu(android.view.Menu)";
	public static final String ACTIVITY_ONOPTIONSITEMSELECTED = "boolean onOptionsItemSelected(android.view.MenuItem)";
	public static final String ACTIVITY_ONMENUITEMSELECTED = "boolean onMenuItemSelected(int,android.view.MenuItem)";
	public static final String ACTIVITY_ONCREATECONTEXTMENU = "void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenu$ContextMenuInfo)";																												
	public static final String ACTIVITY_ONCONTEXTITEMSELECTED = "boolean onContextItemSelected(android.view.MenuItem)";
	
	
	//
	public static final String SETONCLICKLISTENER = "void setOnClickListener(android.view.View$OnClickListener)";
	public static final String SETONCONTEXTLISTENER = "void setOnCreateContextMenuListener(android.view.View$OnCreateContextMenuListener)";
	public static final String SHOWDIALOG = "void showDialog(int)";

	
	
	
	
	//To Do Here
	public static final String SERVICE_ONCREATE = "void onCreate()";
	public static final String SERVICE_ONSTART1 = "void onStart(android.content.Intent,int)";
	public static final String SERVICE_ONSTART2 = "int onStartCommand(android.content.Intent,int,int)";
	public static final String SERVICE_ONBIND = "android.os.IBinder onBind(android.content.Intent)";
	public static final String SERVICE_ONREBIND = "void onRebind(android.content.Intent)";
	public static final String SERVICE_ONUNBIND = "boolean onUnbind(android.content.Intent)";
	public static final String SERVICE_ONDESTROY = "void onDestroy()";
	
	public static final String BROADCAST_ONRECEIVE = "void onReceive(android.content.Context,android.content.Intent)";
	
	public static final String CONTENTPROVIDER_ONCREATE = "boolean onCreate()";
	
	public static final String APPLICATION_ONCREATE = "void onCreate()";
	public static final String APPLICATION_ONTERMINATE = "void onTerminate()";

	public static final String APPLIFECYCLECALLBACK_ONACTIVITYSTARTED = "void onActivityStarted(android.app.Activity)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYSTOPPED = "void onActivityStopped(android.app.Activity)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYSAVEINSTANCESTATE = "void onActivitySaveInstanceState(android.app.Activity,android.os.Bundle)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYRESUMED = "void onActivityResumed(android.app.Activity)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYPAUSED = "void onActivityPaused(android.app.Activity)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYDESTROYED = "void onActivityDestroyed(android.app.Activity)";
	public static final String APPLIFECYCLECALLBACK_ONACTIVITYCREATED = "void onActivityCreated(android.app.Activity,android.os.Bundle)";
	
	private static final String[] activityMethods = {ACTIVITY_ONCREATE, ACTIVITY_ONDESTROY, ACTIVITY_ONPAUSE,
		ACTIVITY_ONRESTART, ACTIVITY_ONRESUME, ACTIVITY_ONSTART, ACTIVITY_ONSTOP,
		ACTIVITY_ONSAVEINSTANCESTATE, ACTIVITY_ONRESTOREINSTANCESTATE,
		ACTIVITY_ONCREATEDESCRIPTION, ACTIVITY_ONPOSTCREATE, ACTIVITY_ONPOSTRESUME,ACTIVITY_ONCREATEDIALOG,ACTIVITY_ONCREATEOPTIONSMENU,
		ACTIVITY_ONLISTITEMCLICK,ACTIVITY_ONCONTEXTITEMSELECTED,ACTIVITY_ONCREATECONTEXTMENU,ACTIVITY_ONOPTIONSITEMSELECTED,
		ACTIVITY_ONCLICK,ACTIVITY_ONITEMCLICK,ACTIVITY_ONCLICKDIALOG,ACTIVITY_ONMENUITEMSELECTED};
	
	
	
	private static final String[] serviceMethods = {SERVICE_ONCREATE, SERVICE_ONDESTROY, SERVICE_ONSTART1,
		SERVICE_ONSTART2, SERVICE_ONBIND, SERVICE_ONREBIND, SERVICE_ONUNBIND};
	private static final String[] broadcastMethods = {BROADCAST_ONRECEIVE};
	private static final String[] contentproviderMethods = {CONTENTPROVIDER_ONCREATE};
	private static final String[] applicationMethods = {APPLICATION_ONCREATE, APPLICATION_ONTERMINATE,
		APPLIFECYCLECALLBACK_ONACTIVITYSTARTED, APPLIFECYCLECALLBACK_ONACTIVITYSTOPPED,
		APPLIFECYCLECALLBACK_ONACTIVITYSAVEINSTANCESTATE, APPLIFECYCLECALLBACK_ONACTIVITYRESUMED,
		APPLIFECYCLECALLBACK_ONACTIVITYPAUSED, APPLIFECYCLECALLBACK_ONACTIVITYDESTROYED,
		APPLIFECYCLECALLBACK_ONACTIVITYCREATED};
	
	private static final String[] eventMethods = {ACTIVITY_ONCREATE,ACTIVITY_ONRESTART, ACTIVITY_ONRESUME, ACTIVITY_ONSTART,
		ACTIVITY_ONCREATEDIALOG,ACTIVITY_ONCREATEOPTIONSMENU,ACTIVITY_ONITEMCLICK,
		ACTIVITY_ONLISTITEMCLICK,ACTIVITY_ONCONTEXTITEMSELECTED,ACTIVITY_ONCREATECONTEXTMENU,ACTIVITY_ONOPTIONSITEMSELECTED};
	
	
	public static List<String> getActivityLifecycleMethods(){
		return Arrays.asList(activityMethods);
	}
	
	public static List<String> getServiceLifecycleMethods(){
		return Arrays.asList(serviceMethods);
	}
	
	public static List<String> getBroadcastLifecycleMethods(){
		return Arrays.asList(broadcastMethods);
	}
	
	public static List<String> getContentproviderLifecycleMethods(){
		return Arrays.asList(contentproviderMethods);
	}

	public static List<String> getApplicationLifecycleMethods(){
		return Arrays.asList(applicationMethods);
	}

	/**
	 * Gets whether the given class if one of Android's default lifecycle
	 * classes (android.app.Activity etc.)
	 * @param className The name of the class to check
	 * @return True if the given class is one of Android's default lifecycle
	 * classes, otherwise false
	 */
	public static boolean isLifecycleClass(String className) {
		return className.equals(ACTIVITYCLASS)
				|| className.equals(SERVICECLASS)
				|| className.equals(BROADCASTRECEIVERCLASS)
				|| className.equals(CONTENTPROVIDERCLASS)
				|| className.equals(APPLICATIONCLASS);
	}

	public static List<String> getEventMethods(){
		return Arrays.asList(eventMethods);
	}
}