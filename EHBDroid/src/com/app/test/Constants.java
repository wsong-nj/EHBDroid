package com.app.test;

import java.util.HashSet;
import java.util.Set;

import soot.RefType;
import soot.Scene;
import soot.SootMethod;

import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import ehb.instrumentation.codecoverage.CoverageToolkit;

import com.app.test.event.InterAppEvent;
import com.app.test.event.ReceiverEvent;
import com.app.test.event.SystemEvent;
import com.app.test.event.UIEvent;


public class Constants {

	public static final String uiTest = "uTest";
	public static final String sysTest = "sTest";
	public static final String interTest = "iTest";
	
	public static class LogTag{
		public static final String menuTag = "MENU";
		public static final String exceptionTag = "EXCEPTION";
		public static final String eventTag = "EVENT";
		public static final String countTag = "COUNT";
		public static final String sysoutTag = "SYSOUT";
	}
	
	public static class EHBField{
		
		//the follow constants will be used as fields.
		public static Set<String> eventsLinkedList = new HashSet<>();
		public static Set<String> visited = new HashSet<>();

		//the following 6 field will be instrumented as fields.
		//Avoid to collect the same event. isVisited=1 means event has been collected. 
		public static final String ISVISITED = "isVisited";
		
		//UI, system, inter-App events linkedList, will be instrumented in activity
		public static final String UIEVENTLINKEDLIST = "uieventlinkedlist";
		public static final String SYSTEMEVENTLINKEDLIST = "systemeventlinkedlist";
		public static final String INTERAPPEVENTLINKEDLIST = "interappeventlinkedlist";
		public static final String ACTIVITYMENU = "activityMenu";
		public static final String CONTEXTMENU = "contextMenu";
		
		//method coverage
		public static final String METHODCOUNTLIST = "methodCountList";
		
		static {
			eventsLinkedList.add(UIEVENTLINKEDLIST);
			eventsLinkedList.add(SYSTEMEVENTLINKEDLIST);
			eventsLinkedList.add(INTERAPPEVENTLINKEDLIST);
			
			visited.add(ISVISITED);
		}
	}
	
	public static class EHBMethod{
		public static String onCreateOptionsMenu_activity = "boolean onCreateOptionsMenu(android.view.Menu)";
		public static String onMenuItemClick_Name = "boolean onMenuItemClick(android.view.MenuItem)";
		public static String onListItemClick_Name = "void onListItemClick(android.widget.ListView,android.view.View,int,long)";
		
		public static String onOptionsItemSelected_Name = "boolean onOptionsItemSelected(android.view.MenuItem)";
		public static String onContextItemSelected_Name = "boolean onContextItemSelected(android.view.MenuItem)";
	}
	
	public static class EHBClass{
		public static final String CallBack = "com.app.test.CallBack";
	}
	
	//Android Type
	public static RefType menu_Type = RefType.v("android.view.Menu");
	public static RefType contextMenu_Type = RefType.v("android.view.ContextMenu");
	public static RefType menuItem_Type = RefType.v("android.view.MenuItem");
	public static RefType listAdapter_Type = RefType.v("android.widget.ListAdapter");
	public static RefType listView_Type = RefType.v("android.widget.ListView");
	public static RefType context_Type = RefType.v("android.content.Context");
	public static RefType intent_Type = RefType.v("android.content.Intent");
	public static RefType bundle_Type = RefType.v("android.os.Bundle");
	public static RefType log_Type = RefType.v("android.util.Log");
	public static RefType view_Type = RefType.v("android.view.View");
	public static RefType toast_Type = RefType.v("android.widget.Toast");
	public static RefType dialog_Type = RefType.v("android.app.Dialog");
	
	//Java Type
	public static RefType file_Type = RefType.v("java.io.File");
	public static RefType iterator_Type = RefType.v("java.util.Iterator");
	public static RefType list_Type = RefType.v("java.util.List");
	public static RefType map_Type = RefType.v("java.util.Map");
	public static RefType class_Type = RefType.v("java.lang.Class");
	public static RefType exception_Type = RefType.v("java.lang.Exception");
	public static RefType reflectMethod_Type = RefType.v("java.lang.reflect.Method");
	public static RefType linkedList_Type = RefType.v("java.util.LinkedList");
	public static RefType object_Type = RefType.v("java.lang.Object");
	public static RefType Long_Type = RefType.v("java.lang.Long");
	public static RefType integer_Type = RefType.v("java.lang.Integer");
	public static RefType stringBuilder_Type = RefType.v("java.lang.StringBuilder");
	public static RefType string_Type = RefType.v("java.lang.String");
	public static RefType reflectField_Type = RefType.v("java.lang.reflect.Field");
	public static RefType Boolean_Type = RefType.v("java.lang.Boolean");
	
	//self defined type
	public static RefType uiEvent_Type = RefType.v(UIEvent.class.getName());
	public static RefType systemEvent_Type = RefType.v(SystemEvent.class.getName());
	public static RefType receiverEvent_Type = RefType.v(ReceiverEvent.class.getName());
	public static RefType InterAppEvent_Type = RefType.v(InterAppEvent.class.getName());
	
	//Android methods
	public static SootMethod onListItemClick_method = Scene.v().getMethod("<android.app.ListActivity: void onListItemClick(android.widget.ListView,android.view.View,int,long)>");
	public static SootMethod openOptionsMenu_method = Scene.v().getMethod("<android.app.Activity: void openOptionsMenu()>");
	public static SootMethod onCreateOptionsMenu_method = Scene.v().getMethod("<android.app.Activity: boolean onCreateOptionsMenu(android.view.Menu)>");
	public static SootMethod view_getContext_method = Scene.v().getMethod("<android.view.View: android.content.Context getContext()>");
	public static SootMethod dialog_getContext_method = Scene.v().getMethod("<android.app.Dialog: android.content.Context getContext()>");
	public static SootMethod onCreateContextMenu_method = Scene.v().getMethod("<android.app.Activity: void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenu$ContextMenuInfo)>");
	public static SootMethod onDestroy_method = Scene.v().getMethod("<android.app.Activity: void onDestroy()>");
	//Java methods
	public static SootMethod isEmpty_method = Scene.v().getMethod("<java.util.AbstractCollection: boolean isEmpty()>");
	public static SootMethod poll_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object poll()>");
	public static SootMethod getClass_method = Scene.v().getMethod("<java.lang.Object: java.lang.Class getClass()>");
	public static SootMethod getMethods_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Method[] getMethods()>");
	public static SootMethod reflectMethodGetName_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.String getName()>");
	public static SootMethod classGetName_method = Scene.v().getMethod("<java.lang.Class: java.lang.String getName()>");
	public static SootMethod startsWith_method = Scene.v().getMethod("<java.lang.String: boolean startsWith(java.lang.String)>");
	public static SootMethod invoke_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>");
	public static SootMethod set_method = Scene.v().getMethod("<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>");
	public static SootMethod getMethod_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>");
	public static SootMethod forName_method = Scene.v().getMethod("<java.lang.Class: java.lang.Class forName(java.lang.String)>");
	public static SootMethod getField_method = Scene.v().getMethod("<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>");
	public static SootMethod fieldGet_method = Scene.v().getMethod("<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>");
	public static SootMethod booleanValue_method = Scene.v().getMethod("<java.lang.Boolean: boolean booleanValue()>");
	public static SootMethod offer_method = Scene.v().getMethod("<java.util.LinkedList: boolean offer(java.lang.Object)>");
	public static SootMethod booleanValueOf_method = Scene.v().getMethod("<java.lang.Boolean: java.lang.Boolean valueOf(boolean)>");
	public static SootMethod peek_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object peek()>");
	public static SootMethod objectInit_method = Scene.v().getMethod("<java.lang.Object: void <init>()>");
	public static SootMethod objectEquals_method = Scene.v().getMethod("<java.lang.Object: boolean equals(java.lang.Object)>");
	public static SootMethod contains_method = Scene.v().getMethod("<java.util.LinkedList: boolean contains(java.lang.Object)>");
	public static SootMethod linkedListInit_method = Scene.v().getMethod("<java.util.LinkedList: void <init>()>");
	public static SootMethod stringEquals_method = Scene.v().getMethod("<java.lang.String: boolean equals(java.lang.Object)>");
	public static SootMethod linkedListSize_method = Scene.v().getMethod("<java.util.LinkedList: int size()>");
	public static SootMethod getParameterTypes_method = Scene.v().getMethod("<java.lang.reflect.Method: java.lang.Class[] getParameterTypes()>");
	public static SootMethod stringBuilderInit_method = Scene.v().getMethod("<java.lang.StringBuilder: void <init>()>");
	public static SootMethod stringBuilderAppendString_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>");
	public static SootMethod stringBuilderAppendInt_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.StringBuilder append(int)>");
	public static SootMethod stringBuilderToString_method = Scene.v().getMethod("<java.lang.StringBuilder: java.lang.String toString()>");
	public static SootMethod mapGet_method = Scene.v().getMethod("<java.util.Map: java.lang.Object get(java.lang.Object)>");
	public static SootMethod iterator_method = Scene.v().getMethod("<java.util.List: java.util.Iterator iterator()>");
	public static SootMethod hasNext_method = Scene.v().getMethod("<java.util.Iterator: boolean hasNext()>");
	public static SootMethod next_method = Scene.v().getMethod("<java.util.Iterator: java.lang.Object next()>");
	public static SootMethod integerInit_method = Scene.v().getMethod("<java.lang.Integer: void <init>(int)>");
	public static SootMethod integerValueOf_method = Scene.v().getMethod("<java.lang.Integer: java.lang.Integer valueOf(int)>");
	public static SootMethod stringValueOf_method = Scene.v().getMethod("<java.lang.String: java.lang.String valueOf(int)>");
	public static SootMethod longValueOf_method = Scene.v().getMethod("<java.lang.Long: java.lang.Long valueOf(java.lang.String)>");
	public static SootMethod longValue_method = Scene.v().getMethod("<java.lang.Long: long longValue()>");
	public static SootMethod integerIntValue_method = Scene.v().getMethod("<java.lang.Integer: int intValue()>");
	public static SootMethod linkedListClone_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object clone()>");
	public static SootMethod linkedListSet_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object set(int,java.lang.Object)>");
	public static SootMethod linkedListGet_method = Scene.v().getMethod("<java.util.LinkedList: java.lang.Object get(int)>");

	//self defined methods
	public static SootMethod utilLogException_method = Scene.v().getMethod("<com.app.test.Util: void LogException(java.lang.Exception)>");
	public static SootMethod uiEventinit_method = Scene.v().getMethod("<com.app.test.event.UIEvent: void <init>(java.lang.Object,java.lang.Object,java.lang.String,boolean)>");
	public static SootMethod systemEventinit_method = Scene.v().getMethod("<com.app.test.event.SystemEvent: void <init>(java.lang.Object,java.lang.Object,java.lang.String)>");
	public static SootMethod receiverEventinit_method = Scene.v().getMethod("<com.app.test.event.ReceiverEvent: void <init>(java.lang.Object,java.lang.Object,java.lang.String,android.content.IntentFilter)>");
	public static SootMethod interAppEventinit_method = Scene.v().getMethod("<com.app.test.event.InterAppEvent: void <init>(java.lang.Object,android.content.IntentFilter)>");
	public static SootMethod uiEventHandlerAddMenuItem_method = Scene.v().getMethod("<com.app.test.event.UIEventHandler: void addMenuItem(android.app.Activity,android.view.Menu)>");
	public static SootMethod systemEventHandlerAddMenuItem_method = Scene.v().getMethod("<com.app.test.event.SystemEventHandler: void addMenuItem(android.app.Activity,android.view.Menu)>");
	public static SootMethod interAppEventHandlerAddMenuItem_method = Scene.v().getMethod("<com.app.test.event.InterAppEventHandler: void addMenuItem(android.app.Activity,android.view.Menu)>");
	
	public static final String codeCoverageToolkitName = CoverageToolkit.class.getName();
	public static SootMethod codeCoverageToolkitCalculateLines = Scene.v().getMethod("<"+codeCoverageToolkitName+": int calculateLines()>");
	public static SootMethod codeCoverageToolkitInitbbblllij = Scene.v().getMethod("<"+codeCoverageToolkitName+": void initbbblllij(int,int)>");
	public static SootMethod codeCoverageToolkitInitbbblllijk = Scene.v().getMethod("<"+codeCoverageToolkitName+": void initbbblllijk(int,int,int)>");
	public static SootMethod codeCoverageToolkitInstrumentData = Scene.v().getMethod("<"+codeCoverageToolkitName+": void instrumentData(int,int,int,int)>");
	public static SootMethod codeCoverageToolkitPrintResult = Scene.v().getMethod("<"+codeCoverageToolkitName+": void printResult()>");
	
}
