package com.app.test;

import soot.Scene;
import soot.SootClass;

public class AndroidConstants {
	public static final String AndroidView = "android.view.View";
	public static final String AndroidDialog = "android.app.Dialog";
	public static final String AndroidActivity = "android.app.Activity";
	public static final String AndroidListActivity = "android.app.ListActivity";
	
	public static final SootClass AndroidViewClass = Scene.v().getSootClass(AndroidView);
	public static final SootClass AndroidDialogClass = Scene.v().getSootClass(AndroidDialog);
	public static final SootClass AndroidActivityClass = Scene.v().getSootClass(AndroidActivity);
	public static final SootClass AndroidListActivityClass = Scene.v().getSootClass("android.app.ListActivity");
}
