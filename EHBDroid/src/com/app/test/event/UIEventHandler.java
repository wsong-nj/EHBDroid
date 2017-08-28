package com.app.test.event;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.objectweb.asm.tree.IntInsnNode;

import soot.SootMethod;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.test.AppDir;
import com.app.test.CallBack;
import com.app.test.Constants;
import com.app.test.MathUtil;
import com.app.test.Util;

public class UIEventHandler{

	public static final String ONEEVENT = "oneevent";
	public static final String ONESEQ = "onesequence";
	public static final String MANYSEQ= "manysequence";
	
	public static final int fixSeq = 1;
	public static String file = AppDir.file;
	public static final String itemName = Constants.uiTest; 
	
	public static Activity act;
	
	/**
	 * <p> test activity UI events.
	 * The UI events can be divided into four categories according to where events from:
	 * <li>activity linkedlist 
	 * <li>menu 
	 * <li>listActivity
	 * <li>preferenceActivity.
	 * 
	 * <p> There are two strategies  to trig those events: trig one event per time or trig a random senquence 
	 * of all events per time. 
	 *   
	 * @param activity Activity contains those events. 
	 * @return trig the events in a preset way. 
	 * */
	public static void doUIEventTest(Activity activity) {
		
		act = activity;
//		String stgy = readStgyFrom(file);
		try {
			
			//Attention: XML UI event has been integreted into linkedList
			//Read UI events from uieventlinkedlist in activity.
			Field uiEvent_field = activity.getClass().getField(Constants.EHBField.UIEVENTLINKEDLIST);
			Object object = uiEvent_field.get(null);
			LinkedList<UIEvent> uiEvents = (LinkedList<UIEvent>) object;
			String stgy = AppDir.STGY;
			if(stgy.equals(ONEEVENT)){
				trigOEPT(activity, uiEvents);
			}
			else if(stgy.equals(ONESEQ)){
				trigOSPT(activity, uiEvents);
			}

		} catch (Exception e) {
			Util.LogException(e);
		} finally {
			Log.v("EVENT", AppDir.visitedMethodCount + "");
		}
	}

	/**
	 * Stgy 1: trigger one event per time. 
	 * 
	 * eventSourceType represents the number of event sources. 
	 * if activity is a listActivity, the number is 3(list+menu+listActivity),
	 * if activity is a preferenceActibity, number is 3(list+menu+preferenceActivity).
	 * else activity is a preferenceActibity, number is 2(list+menu).
	 * */
	private static void trigOEPT(Activity activity,
			LinkedList<UIEvent> uiEvents) {
		double random = Math.random();
		double totalevents; 
		int list_size = uiEvents.size();
		List<MenuItem> menuItems = UIEventTesterForSingleEvent.getMenuItems(activity);
		int menu_size = menuItems.size();
		
		if(isListAct(activity)||isPreferenceAct(activity)){
			int childCount = 0;
			boolean isPreference = isPreferenceAct(activity);
			
			if(isPreference){
				PreferenceActivity pActivity = (PreferenceActivity)activity;
				childCount = pActivity.getListView().getChildCount();
			}
			else{
				ListActivity listActivity = (ListActivity)activity;
				childCount = listActivity.getListView().getChildCount();
			}
			totalevents = Double.valueOf(list_size+menu_size+childCount);
			double first = list_size/totalevents;
			double third = 1-childCount/totalevents;
			
			if(random<first){
				UIEventTesterForSingleEvent.testUI(uiEvents);
			}
			else if (random>third){
				if(isPreference)
					UIEventTesterForSingleEvent.testPreAct((PreferenceActivity)activity);
				else
					UIEventTesterForSingleEvent.testListAct((ListActivity)activity);
			}
			else{
				UIEventTesterForSingleEvent.testMenu(activity, menuItems);
			}
		}
		else {
			totalevents = Double.valueOf(list_size+menu_size);
			if(totalevents==0) return;
			double first = list_size/totalevents;
			if(random<first){
				UIEventTesterForSingleEvent.testUI(uiEvents);
			}
			else{
				UIEventTesterForSingleEvent.testMenu(activity,menuItems);
			}
		}
	}

	/**
	 * Stgy 2: trigger one sequence per time. 
	 * */
	private static void trigOSPT(Activity activity, 
			LinkedList<UIEvent> uiEvents) {
		String stgy = ONESEQ;
		UIEventTesterForSeq.testUI(uiEvents,stgy);
		UIEventTesterForSeq.testOptionMenu(activity);
//		UIEventTesterForSeq.testContextMenu(activity);
		if (isPreferenceAct(activity))
			UIEventTesterForSeq.testPreAct(activity);
		else if (isListAct(activity))
			UIEventTesterForSeq.testListAct((ListActivity) activity);
	}
	
	/**
	 * add a menu name itemName to activity
	 * @param activity added activity
	 * @param menu added menu
	 * */
	public static void addMenuItem(final Activity activity, Menu menu) {
		MenuItem add = menu.add(itemName);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				doUIEventTest(activity);
				return true;
			}
		});
	}

	// is ListActivity
	private static boolean isListAct(Activity activity) {
		return activity instanceof ListActivity;
	}

	// is PreferenceActivity
	private static boolean isPreferenceAct(Activity activity) {
		return activity instanceof PreferenceActivity;
	}
	
	private static String readStgyFrom(String file){
		String ehbStgy = "";
		try{
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ois.readObject();ois.readObject();ois.readObject();ois.readObject();ois.readObject();
			Object readObject6 = ois.readObject();
			ehbStgy = (String)readObject6;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ehbStgy;
	}
		
	
	/**
	 * An UI event tester class specially used to test UI, menu, list and preAct.
	 * */
	public static class UIEventTesterForSeq {
		/**
		 * 1. test UI events. 
		 * @param uiEvents uiEvents collected by EventRecognizer. 
		 * @param stgy A strategy to test events.
		 * */
		public static void testUI(LinkedList<UIEvent> uiEvents, String stgy) {
			if (stgy.equals(ONEEVENT)) {
				testOneEvent(uiEvents);
			} else if (stgy.equals(ONESEQ)) {
				testOneSeq(uiEvents);
			} else if (stgy.equals(MANYSEQ)) {
				testManySeq(uiEvents);
			}
		}

		// 1.1 oneEventStgy @see EHBOptions.ONEEVENT
		private static void testOneEvent(LinkedList<UIEvent> uiEvents) {
			double d = Math.random();
			int size = uiEvents.size();
			int ramdonValue = (int) (d * size);
			UIEvent uiEvent = uiEvents.get(ramdonValue);
			handleUIEvent(uiEvent);
		}

		// 1.2 oneSeqStgy @see EHBOptions.ONESEQ
		private static void testOneSeq(LinkedList<UIEvent> uiEvents) {
			for (UIEvent uiEvent : uiEvents) {
				handleUIEvent(uiEvent);
			}
		}

		// 1.3 mangSeqStgy @see EHBOptions.MANYSEQ
		private static void testManySeq(LinkedList<UIEvent> uiEvents) {
			// set the upbound for events permutation.
			int fixNumber = fixSeq;
			List<List<Integer>> eventspermutation = MathUtil.getNPermutation(fixNumber,uiEvents.size());
			for (List<Integer> list : eventspermutation) {
				for (int i : list) {
					Log.v(Constants.LogTag.eventTag, i+"XX");
					UIEvent uiEvent = uiEvents.get(i);
					handleUIEvent(uiEvent);
				}
			}
		}

		/**
		 * UI event can be divided into View and Dialog. 
		 * */
		public static void handleUIEvent(UIEvent uiEvent) {
			String callback = uiEvent.getCallback();
			Object listener = uiEvent.getListener();
			Object ui = uiEvent.getUi();
			Method method = Util.getMethod(listener.getClass(), callback);
			if (ui instanceof View) {
				testView(method, (View) ui, listener);
			} else if (ui instanceof Dialog) {
				testDialog(method, (Dialog) ui, listener);
			}
		}
	
		/**
		 * According the callback, this method  construct corresponding callback invocation. 
		 * We consider 55 callback in ehbdroid. 
		 * <p>
		 * To avoid false positive, before constructing the invocation, we first check 
		 * the visibility of the view. The invocation is built only if the view is visible.
		 * <p> 
		 * Usually, a callback may have more than one parameter. For those callbacks with
		 * multiple parameters, the paramters can be set a group of args. 
		 * Therefore, there are two ways to invoke the callbacks: set the paramters with one args 
		 * or with a group of args.    
		 * */
		static void testView(Method method, View view, Object object) {
			//check the visibility of view£¬ if not visible, do nothing.
			if(!(view.getVisibility()==View.VISIBLE)){
				return;
			}
			Util.Log(view, object, method.getName());
			String subSig = Util.getSubsignature(method);
			try {
				// void onStartTrackingTouch(android.widget.SeekBar)
				// void onStopTrackingTouch(android.widget.SeekBar)
				// void onClick(android.view.View)
				// boolean onLongClick(android.view.View)
				// void onNothingSelected(android.widget.AdapterView)
				if ((CallBack.ONLONGCLICK.equals(subSig))
						|| (CallBack.ONCLICK.equals(subSig))
						|| (CallBack.ONNOTHINGSELECTED.equals(subSig))
						|| (CallBack.onStartTrackingTouch.equals(subSig))
						|| (CallBack.onStopTrackingTouch.equals(subSig))) {
					method.invoke(object, new Object[] { view });
				}
				// void onCheckedChanged(android.widget.CompoundButton,boolean)
				// void onFocusChange(android.view.View,boolean)
				else if ((CallBack.ONFOCUSCHANGE.equals(subSig))
						|| (CallBack.onCheckedChanged.equals(subSig))) {
					Object[] params = new Object[2];
					params[0] = view;
					params[1] = true;
					method.invoke(object, params);
					params[1] = false;
					method.invoke(object, params);
				}
				// void onSystemUiVisibilityChange(int)
				else if (CallBack.ONSYSTEMUIVISIBILITYCHANGE.equals(subSig)) {
					method.invoke(object, new Object[] { 0 });
					method.invoke(object, new Object[] { 8 });
				}
				// void onItemClick(android.widget.AdapterView,android.view.View,int,long)
				// boolean onItemLongClick(android.widget.AdapterView,android.view.View,int,long)
				// void onItemSelected(android.widget.AdapterView,android.view.View,int,long)
				else if ((CallBack.ONITEMCLICK.equals(subSig))
						|| (CallBack.ONITEMLONGCLICK.equals(subSig))
						|| (CallBack.ONITEMSELECTED.equals(subSig))) {
					AdapterView adapterView = (AdapterView) view;
					int childCount = adapterView.getChildCount()>5?5:adapterView.getChildCount();
					for (int i = 0; i<childCount; i++) {
						View childView = adapterView.getChildAt(i);
						Adapter adapter = adapterView.getAdapter();
						long itemId = adapter.getItemId(i);
						method.invoke(object, new Object[]{ view, childView, i, itemId });
					}
				}
				// void onProgressChanged(android.widget.SeekBar,int,boolean)
				else if (CallBack.onProgressChanged.equals(subSig)) {
					Object[] params = new Object[3];
					SeekBar seekBar = (SeekBar) view;
					params[0] = view;
					int max = seekBar.getMax(); // set process = max/2
					params[1] = max / 2;
					params[2] = true;
					method.invoke(object, params);
					params[2] = false;
					method.invoke(object, params);
				}
				// void onScrollStateChange(android.widget.AbsListView,int)
				else if (CallBack.onScrollStateChanged.equals(subSig)) {
					for (int i = 0; i < 3; i++) {
						method.invoke(object, new Object[] { view, i });
					}
				}
				// void onScroll(AbsListView view, int firstVisibleItem, int
				// visibleItemCount,int totalItemCount);
				else if (CallBack.onScroll.equals(subSig)) {
					AbsListView listView = (AbsListView) view;
					int childCount = listView.getCount();
					int start = listView.getFirstVisiblePosition();
					int stop = listView.getLastVisiblePosition();
					method.invoke(object, new Object[]{view, start, stop, childCount});
				}
				// void onPageScrollStateChanged(int)
				else if (CallBack.onPageScrollStateChanged.equals(subSig)) {
					for (int i = 0; i < 3; i++) {
						method.invoke(object, new Object[] { i });
					}
				}
				// void onPageScrolled(int,float,int)
				else if (CallBack.onPageScrolled.equals(subSig)) {
					method.invoke(object, new Object[] { 0, 0.2f, 0 });
					method.invoke(object, new Object[] { 0, 0.8f, 0 });
				}
				// void onPageSelected(int)
				else if (CallBack.onPageSelected.equals(subSig)) {
					method.invoke(object, new Object[] { 1 });
				}
				// boolean onClose()
				else if (CallBack.onClose.equals(subSig)) {
					method.invoke(object);
				}
				// void onScrollStateChange(android.widget.NumberPicker,int)
				else if (CallBack.NumberPicker_onScrollStateChange
						.equals(subSig)) {
					for (int i = 0; i < 3; i++) {
						method.invoke(object, new Object[] { view, i });
					}
				}
				// boolean onQueryTextSubmit(java.lang.String)
				else if (CallBack.onQueryTextSubmit.equals(subSig)) {
					method.invoke(object, "android");
				}
				// boolean onQueryTextChange(java.lang.String)
				else if (CallBack.onQueryTextChange.equals(subSig)) {
					method.invoke(object, "java");
				}
				// boolean onGenericMotion(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONGENERICMOTION.equals(subSig)) {
					MotionEvent downEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_SCROLL, view.getX(),
							view.getY(), 0);
					method.invoke(object, new Object[] { view, downEvent });
				}
				// boolean onTouch(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONTOUCH.equals(subSig)) {
					MotionEvent downEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_DOWN, view.getX(), view.getY(),
							0);
					method.invoke(object, new Object[] { view, downEvent });
					MotionEvent upEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
							view.getX(), view.getY(), 0);
					method.invoke(object, new Object[] { view, upEvent });
				}
				// void onRatingChanged(android.widget.RatingBar,float,boolean)
				else if (CallBack.onRatingChanged.equals(subSig)) {
					RatingBar ratingBar = (RatingBar) view;
					int numStars = ratingBar.getNumStars();
					float rating = numStars / 2;
					method.invoke(object, new Object[] { view, rating, true });
				}
				// boolean onHover(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONHOVER.equals(subSig)) {
					MotionEvent centerEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_HOVER_ENTER, view.getX(),
							view.getY(), 0);
					method.invoke(object, new Object[] { view, centerEvent });
					MotionEvent exitEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_HOVER_EXIT, view.getX(),
							view.getY(), 0);
					method.invoke(object, new Object[] { view, exitEvent });
				}
				// boolean onSuggestionSelect(int)
				else if (CallBack.onSuggestionSelect.equals(subSig)) {
					SearchView sView = (SearchView) view;
					CursorAdapter suggestionsAdapter = sView
							.getSuggestionsAdapter();
					int count = suggestionsAdapter.getCount();
					if (count != 0)
						method.invoke(object, count - 1);
				}
				// boolean onSuggestionClick(int)
				else if (CallBack.onSuggestionClick.equals(subSig)) {
					SearchView sView = (SearchView) view;
					CursorAdapter suggestionsAdapter = sView
							.getSuggestionsAdapter();
					int count = suggestionsAdapter.getCount();
					if (count != 0)
						method.invoke(object, count - 1);
				}
				// boolean onDrag(android.view.View,android.view.DragEvent)
				// init(int action, float x, float y, ClipDescription description, ClipData data,Object localState, boolean result) 
				else if (CallBack.ONDRAG.equals(subSig)) {
					//TODO Please set the approprivate arguments for init method
					Object[] objs = new Object[]{DragEvent.ACTION_DRAG_STARTED,1.0f,
							1.0f, null,null,null,true};
					DragEvent genDragEvent = genDragEvent(objs);
					method.invoke(object, new Object[]{view, genDragEvent});
				}
				// boolean onPreferenceTreeClick(android.preference.PreferenceScreen,android.preference.Preference)
				else if (CallBack.ONPREFERENCETREECLICK.equals(subSig)) {
					// Done. We consider preference activity seperately @UIEventHandler.
				}
				// void onInflate(android.view.ViewStub,android.view.View)
				else if (CallBack.onInflate.equals(subSig)) {
					ViewStub stub = (ViewStub)view;
					View inflate = stub.inflate();
					method.invoke(object, new Object[]{stub,inflate});
				}
				// boolean onEditorAction(android.widget.TextView,int,android.view.KeyEvent)
				else if (CallBack.ONEDITORACTION.equals(subSig)) {
					TextView tView = (TextView)view;
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{tView, EditorInfo.IME_NULL, keyEvent});
				}
				// boolean onKey(android.view.View,int,android.view.KeyEvent)
				else if (CallBack.ONKEY.equals(subSig)) {
					TextView tView = (TextView)view;
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{tView, KeyEvent.KEYCODE_ENTER, keyEvent});
				}
				// void onChildViewAdded(android.view.View,android.view.View)
				// void onChildViewRemoved(android.view.View,android.view.View)
				else if (CallBack.ONCHILDVIEWADDED.equals(subSig)
						|| CallBack.ONCHILDVIEWREMOVED.equals(subSig)) {
					// do nothing
				}
				//void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenuInfo)
				else if (CallBack.ONCREATECONTEXTMENU.equals(subSig)) {
					//TODO noting to do but as a mark.
					if(view instanceof ListView){
						ListView listView = (ListView)view;
						for(int index = 0; index<listView.getChildCount();index++){
							View child = listView.getChildAt(index);
							child.performLongClick();
							List<MenuItem> contextMenuItem = getContextMenuItem(act);
							for(MenuItem menuItem:contextMenuItem){
								child.performLongClick();
								act.onContextItemSelected(menuItem);
							}
						}
					}
					else {
						view.performLongClick();
						List<MenuItem> contextMenuItem = getContextMenuItem(act);
						for(MenuItem menuItem:contextMenuItem){
							view.performLongClick();
							act.onContextItemSelected(menuItem);
						}
						
					}
				}
				// handle event from layout event, like click(view);
				else {
					method.invoke(object, new Object[] { view });
				}
			} catch (Exception localException) {
				Util.LogException(localException);
			}
		}

		private static void testDialog(Method method, Dialog dialog, Object object) {
			Util.Log(dialog, object, method.getName());
			if(!dialog.isShowing())
				dialog.show();
			//TODO add dialog.show();
			String subSig = Util.getSubsignature(method);
			try {
				// void onCancel(android.content.DialogInterface)
				// void onDismiss(android.content.DialogInterface)
				// void onShow(android.content.DialogInterface)
				if ((CallBack.DialogInterface_onDismiss.equals(subSig))
						|| (CallBack.DialogInterface_onShow.equals(subSig))
						|| (CallBack.DialogInterface_onCancel.equals(subSig))) {
					method.invoke(object, new Object[] { dialog });
				}
				// void onClick(android.content.DialogInterface,int)
				else if (CallBack.DialogInterface_onClick.equals(subSig)) {
					method.invoke(object, new Object[] { dialog, -1 });
					method.invoke(object, new Object[] { dialog, -2 });
					method.invoke(object, new Object[] { dialog, -3 });
				}
				// void onClick(android.content.DialogInterface,int,boolean)
				else if (CallBack.DialogInterface_OnMultiChoiceClickListener_onClick
						.equals(subSig)) {
					method.invoke(object, new Object[] { dialog, -1, true });
					method.invoke(object, new Object[] { dialog, -2, true });
					method.invoke(object, new Object[] { dialog, -3, true });
				}
				// boolean onKey(android.content.DialogInterface,int,android.view.KeyEvent)
				else if (CallBack.DialogInterface_onKey.equals(subSig)) {
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{dialog, KeyEvent.KEYCODE_BACK, keyEvent});
				}
			} catch (Exception localException) {
				Util.LogException(localException);
			}
		}
		
		
		/**
		 * 2. Menu UI event, invoke onOptionsItemSelected(item)
		 * */
		public static void testOptionMenu(Activity activity) {
			Field field;
			try {
				field = activity.getClass().getField(
						Constants.EHBField.ACTIVITYMENU);
				Object object = field.get(null);
				
				if(object==null){
					Log.v(Constants.LogTag.menuTag, "Menu is not used in "+activity.getClass().getName());
					return;
				}
				Menu menu = (Menu) object;
				for (int i = 0; i < menu.size(); i++) {
					MenuItem item = menu.getItem(i);
					CharSequence title = item.getTitle();
					// ignore self defined menuItem
					if (!(Constants.uiTest.equals(title)||Constants.sysTest.equals(title)||
							Constants.interTest.equals(title))){
						Log.v(Constants.LogTag.eventTag, "Activity: "+activity.getClass().getName()+" MenuItem: "+item.getTitle());
						activity.onOptionsItemSelected(item);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 3. UI event from listActivity, invoke onListItemClick of ListActivity
		 * */
		public static void testListAct(ListActivity activity) {
			Class<? extends ListActivity> class1 = activity.getClass();
			
			// void onListItemClick(ListView l, View v, int position, long id);
			ListView listView = activity.getListView();
			try {
				Method method = class1.getDeclaredMethod("onListItemClick",
						new Class[] { ListView.class, View.class, int.class,
								long.class });
				method.setAccessible(true);
				for (int i = 0; i < listView.getChildCount(); i++) {
					View childView = listView.getChildAt(i);
					Adapter adapter = listView.getAdapter();
					long itemId = adapter.getItemId(i);
					try {
						method.invoke(activity, new Object[]{listView,
								childView, i, itemId });
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 4. UI event from preference activity. invoke onItemClick of
		 * PreferenceActivity
		 * */
		public static void testPreAct(Activity activity) {
			PreferenceActivity pActivity = (PreferenceActivity) activity;
			PreferenceScreen pScreen = pActivity.getPreferenceScreen();
			// int cou = pScreen.getPreferenceCount();
			ListView listView = pActivity.getListView();
			int count = listView.getCount();
			Log.v("EVENT", "listView count: " + count
					+ " listView child size: " + listView.getChildCount());
			for (int i = 0; i < count; i++) {
				pScreen.onItemClick(listView, null, i, 0);
			}
		}
		
		/**
		 * 5. Context Menu Event
		 * */
		public static List<MenuItem> getContextMenuItem(Activity activity) {
			List<MenuItem> menuItems = new ArrayList<>();
			Field field = null;
			
			field = getContextMenuField(activity);
			if(field==null)
				return menuItems;
			Object object = null;
			try {
				object = field.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(object==null){
				Log.v(Constants.LogTag.eventTag, "Context Menu is null.");
			}
			else{
				ContextMenu contextMenu = (ContextMenu)object;
				for(int i = 0; i<contextMenu.size(); i++){
					menuItems.add(contextMenu.getItem(i));
				}
			}
			
			return menuItems;
		}

		private static Field getContextMenuField(Activity activity) {
			Field[] declaredFields = activity.getClass().getDeclaredFields();
			for(Field f:declaredFields){
				if(f.getName().equals(Constants.EHBField.CONTEXTMENU)){
					return f;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * An UI event tester class specially used to test UI, menu, list and preAct.
	 * <p>
	 * Different from For UIEventTesterForSeq, every callback will only invoked one time.
	 * */
	public static class UIEventTesterForSingleEvent {
		/**
		 * 1. test UI events. 
		 * @param uiEvents uiEvents collected by EventRecognizer. 
		 * @param stgy A strategy to test events.
		 * */
		public static void testUI(LinkedList<UIEvent> uiEvents) {
			testOneEvent(uiEvents);
		}

		private static void testOneEvent(LinkedList<UIEvent> uiEvents) {
			double d = Math.random();
			int size = uiEvents.size();
			int ramdonValue = (int) (d * size);
			UIEvent uiEvent = uiEvents.get(ramdonValue);
			handleUIEvent(uiEvent);
		}

		/**
		 * UI event can be divided into View and Dialog. 
		 * */
		public static void handleUIEvent(UIEvent uiEvent) {
			String callback = uiEvent.getCallback();
			Object listener = uiEvent.getListener();
			Object ui = uiEvent.getUi();
			Method method = Util.getMethod(listener.getClass(), callback);
			if (ui instanceof View) {
				testView(method, (View) ui, listener);
			} else if (ui instanceof Dialog) {
				testDialog(method, (Dialog) ui, listener);
			}
		}
	
		/**
		 * According the callback, this method will construct corresponding callback invocation. 
		 * We consider 55 callback in ehbdroid. 
		 * <p>
		 * During the invocation construction, we will check the visibility of the view. 
		 * If view is not visible, return.
		 * <p> 
		 * Usually, a callback may have more than one parameter. For those callbacks with
		 * multiple parameters, the paramters can be set a group of args(parameter tuple). 
		 * Therefore, there are two ways to invoke the callbacks: set the paramters with one args 
		 * or with a group of args.    
		 * */
		static void testView(Method method, View view, Object object) {
			
			if(!(view.getVisibility()==View.VISIBLE)){
				return;
			}
			Util.Log(view, object, method.getName());
			
			String subSig = Util.getSubsignature(method);
			try {
				// void onStartTrackingTouch(android.widget.SeekBar)
				// void onStopTrackingTouch(android.widget.SeekBar)
				// void onClick(android.view.View)
				// boolean onLongClick(android.view.View)
				// void onNothingSelected(android.widget.AdapterView)
				if ((CallBack.ONLONGCLICK.equals(subSig))
						|| (CallBack.ONCLICK.equals(subSig))
						|| (CallBack.ONNOTHINGSELECTED.equals(subSig))
						|| (CallBack.onStartTrackingTouch.equals(subSig))
						|| (CallBack.onStopTrackingTouch.equals(subSig))) {
					method.invoke(object, new Object[] { view });
				}
				// void onCheckedChanged(android.widget.CompoundButton,boolean)
				// void onFocusChange(android.view.View,boolean)
				else if ((CallBack.ONFOCUSCHANGE.equals(subSig))
						|| (CallBack.onCheckedChanged.equals(subSig))) {
					Object[] params = new Object[2];
					params[0] = view;
					int random = random(2);
					if(random==0){
						params[1] = true;
					}
					else if(random==1){
						params[1] = false;
					}
					method.invoke(object, params);
				}
				// void onSystemUiVisibilityChange(int)
				else if (CallBack.ONSYSTEMUIVISIBILITYCHANGE.equals(subSig)) {
					int random = random(3);
					if(random==View.INVISIBLE)
						method.invoke(object, new Object[] {View.INVISIBLE});
					else if(random==View.GONE)
						method.invoke(object, new Object[] {View.GONE});
					else if(random==View.VISIBLE)
						method.invoke(object, new Object[] {View.VISIBLE});
				}
				// void onItemClick(android.widget.AdapterView,android.view.View,int,long)
				// boolean onItemLongClick(android.widget.AdapterView,android.view.View,int,long)
				// void onItemSelected(android.widget.AdapterView,android.view.View,int,long)
				else if ((CallBack.ONITEMCLICK.equals(subSig))
						|| (CallBack.ONITEMLONGCLICK.equals(subSig))
						|| (CallBack.ONITEMSELECTED.equals(subSig))) {
					AdapterView adapterView = (AdapterView) view;
					int count = adapterView.getChildCount();
					int random = random(count);
					View childView = adapterView.getChildAt(random);
					Adapter adapter = adapterView.getAdapter();
					long itemId = adapter.getItemId(random);
					method.invoke(object, new Object[]{view, childView, random, itemId});
				}
				// void onProgressChanged(android.widget.SeekBar,int,boolean)
				else if (CallBack.onProgressChanged.equals(subSig)) {
					Object[] params = new Object[3];
					SeekBar seekBar = (SeekBar) view;
					params[0] = view;
					int max = seekBar.getMax(); // set process = max/2
					params[1] = max / 2;
					
					int random = random(2);
					if(random==0){
						params[2] = true;
					}
					else if(random==1){
						params[2] = false;
					}
					method.invoke(object, params);
				}
				// void onScrollStateChange(android.widget.AbsListView,int)
				else if (CallBack.onScrollStateChanged.equals(subSig)) {
					int random = random(3);
					method.invoke(object, new Object[]{view, random});
				}
				// void onScroll(AbsListView view, int firstVisibleItem, int
				// visibleItemCount,int totalItemCount);
				else if (CallBack.onScroll.equals(subSig)) {
					AbsListView listView = (AbsListView) view;
					int childCount = listView.getCount();
					int start = listView.getFirstVisiblePosition();
					int stop = listView.getLastVisiblePosition();
					method.invoke(object, new Object[] {view,start,stop,
							childCount });
				}
				// void onPageScrollStateChanged(int)
				else if (CallBack.onPageScrollStateChanged.equals(subSig)) {
					int random = random(3);
					method.invoke(object, new Object[]{view, random});
				}
				// void onPageScrolled(int,float,int)
				else if (CallBack.onPageScrolled.equals(subSig)) {
					int random = random(2);
					float f = 0;
					if(random==0)
						f = 0.2f;
					else
						f = 0.8f;
					method.invoke(object, new Object[] {0,f,0});
				}
				// void onPageSelected(int)
				else if (CallBack.onPageSelected.equals(subSig)) {
					method.invoke(object, new Object[] {1});
				}
				// boolean onClose()
				else if (CallBack.onClose.equals(subSig)) {
					method.invoke(object);
				}
				// void onScrollStateChange(android.widget.NumberPicker,int)
				else if (CallBack.NumberPicker_onScrollStateChange
						.equals(subSig)) {
					int random = random(3);
					method.invoke(object, new Object[]{view, random});
				}
				// boolean onQueryTextSubmit(java.lang.String)
				else if (CallBack.onQueryTextSubmit.equals(subSig)) {
					method.invoke(object, "android");
				}
				// boolean onQueryTextChange(java.lang.String)
				else if (CallBack.onQueryTextChange.equals(subSig)) {
					method.invoke(object, "java");
				}
				// boolean onGenericMotion(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONGENERICMOTION.equals(subSig)) {
					MotionEvent downEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_SCROLL, view.getX(),
							view.getY(), 0);
					method.invoke(object, new Object[] { view, downEvent });
				}
				// boolean onTouch(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONTOUCH.equals(subSig)) {
					MotionEvent downEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_DOWN, view.getX(), view.getY(),
							0);
					method.invoke(object, new Object[] { view, downEvent });
				}
				// void onRatingChanged(android.widget.RatingBar,float,boolean)
				else if (CallBack.onRatingChanged.equals(subSig)) {
					RatingBar ratingBar = (RatingBar) view;
					int numStars = ratingBar.getNumStars();
					float rating = numStars / 2;
					method.invoke(object, new Object[] { view, rating, true });
				}
				// boolean onHover(android.view.View,android.view.MotionEvent)
				else if (CallBack.ONHOVER.equals(subSig)) {
					MotionEvent centerEvent = MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_HOVER_ENTER, view.getX(),
							view.getY(), 0);
					method.invoke(object, new Object[] { view, centerEvent });
				}
				// boolean onSuggestionSelect(int)
				else if (CallBack.onSuggestionSelect.equals(subSig)) {
					SearchView sView = (SearchView) view;
					CursorAdapter suggestionsAdapter = sView
							.getSuggestionsAdapter();
					int count = suggestionsAdapter.getCount();
					if (count != 0)
						method.invoke(object, count-1);
				}
				// boolean onSuggestionClick(int)
				else if (CallBack.onSuggestionClick.equals(subSig)) {
					SearchView sView = (SearchView) view;
					CursorAdapter suggestionsAdapter = sView
							.getSuggestionsAdapter();
					int count = suggestionsAdapter.getCount();
					if (count != 0)
						method.invoke(object, count-1);
				}
				// boolean onDrag(android.view.View,android.view.DragEvent)
				else if (CallBack.ONDRAG.equals(subSig)) {
				}
				// void onInflate(android.view.ViewStub,android.view.View)
				else if (CallBack.onInflate.equals(subSig)) {
					ViewStub stub = (ViewStub)view;
					View inflate = stub.inflate();
					method.invoke(object, new Object[]{stub,inflate});
				}
				// boolean
				// onEditorAction(android.widget.TextView,int,android.view.KeyEvent)
				else if (CallBack.ONEDITORACTION.equals(subSig)) {
					TextView tView = (TextView)view;
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{tView, EditorInfo.IME_NULL, keyEvent});
				}
				// boolean onKey(android.view.View,int,android.view.KeyEvent)
				else if (CallBack.ONKEY.equals(subSig)) {
					TextView tView = (TextView)view;
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{tView, KeyEvent.KEYCODE_ENTER, keyEvent});
				}
				//it seems that onCreateContextMenu was hardly used in the real-world app. 
				//void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenuInfo)
				else if (CallBack.ONCREATECONTEXTMENU.equals(subSig)) {
					if(view instanceof AdapterView){
						AdapterView adapterView = (AdapterView)view;
						int random = random(adapterView.getChildCount());
						View child = adapterView.getChildAt(random);
						child.performLongClick();
						testContextMenu(act);
					}
					else {
						view.performLongClick();
						testContextMenu(act);
					}
					
				}
				// handle event from layout event, like click(view);
				else {
					method.invoke(object, new Object[] { view });
				}
			} catch (Exception localException) {
				Util.LogException(localException);
			}
		}

		private static void testDialog(Method method, Dialog dialog, Object object) {
			Util.Log(dialog, object, method.getName());
			if(!dialog.isShowing())
				dialog.show();
			String subSig = Util.getSubsignature(method);
			try {
				// void onCancel(android.content.DialogInterface)
				// void onDismiss(android.content.DialogInterface)
				// void onShow(android.content.DialogInterface)
				if ((CallBack.DialogInterface_onDismiss.equals(subSig))
						|| (CallBack.DialogInterface_onShow.equals(subSig))
						|| (CallBack.DialogInterface_onCancel.equals(subSig))) {
					method.invoke(object, new Object[] { dialog });
				}
				// void onClick(android.content.DialogInterface,int)
				else if (CallBack.DialogInterface_onClick.equals(subSig)) {
					int random = random(3);
					if(random==0)
						method.invoke(object, new Object[] { dialog, -1 });
					else if(random==1)
						method.invoke(object, new Object[] { dialog, -2 });
					else if(random==2)
						method.invoke(object, new Object[] { dialog, -3 });
				}
				// void onClick(android.content.DialogInterface,int,boolean)
				else if (CallBack.DialogInterface_OnMultiChoiceClickListener_onClick
						.equals(subSig)) {
					int random = random(3);
					if(random==0)
						method.invoke(object, new Object[] { dialog, -1, true });
					else if(random==1)
						method.invoke(object, new Object[] { dialog, -2, true });
					else if(random==2)
						method.invoke(object, new Object[] { dialog, -3, true });
				}
				// boolean onKey(android.content.DialogInterface,int,android.view.KeyEvent)
				else if (CallBack.DialogInterface_onKey.equals(subSig)) {
					KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER);
					method.invoke(object, new Object[]{dialog, KeyEvent.KEYCODE_BACK, keyEvent});
				}
			} catch (Exception localException) {
				Util.LogException(localException);
			}
		}
		
		public static List<MenuItem> getMenuItems(Activity activity){
			List<MenuItem> menuItems = new ArrayList<>();
			try {
				Field field = activity.getClass().getField(
						Constants.EHBField.ACTIVITYMENU);
				Object object = field.get(null);
				
				if(object==null){
					Log.v(Constants.LogTag.eventTag, "Menu is not used in "+activity.getClass().getName());
					return menuItems;
				}
				Menu menu = (Menu) object;
				
				for (int i = 0; i < menu.size(); i++) {
					MenuItem item = menu.getItem(i);
					CharSequence title = item.getTitle();
					// ignore self defined menuItem
					if (!(Constants.uiTest.equals(title)|| Constants.sysTest.equals(title) 
							|| Constants.interTest.equals(title))){
						menuItems.add(item);
					}
				}
				return menuItems;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return menuItems;
		}
		
		/**
		 * 2. UI event from Menu, invoke onOptionsItemSelected(item)
		 * */
		public static void testMenu(Activity activity, List<MenuItem> menuItems) {
			int random = random(menuItems.size());
			MenuItem menuItem = menuItems.get(random);
			Log.v(Constants.LogTag.eventTag, "Menu Event: "+menuItem.getTitle());
			activity.onOptionsItemSelected(menuItem);
		}

		/**
		 * 3. UI event from listActivity, invoke onListItemClick of ListActivity
		 * */
		public static void testListAct(ListActivity activity) {
			Class<? extends ListActivity> class1 = activity.getClass();
			
			// void onListItemClick(ListView l, View v, int position, long id);
			// method onListItemClick is not accessible outside, use reflection to invoke it.
 			ListView listView = activity.getListView();
			try {
				Method method = class1.getDeclaredMethod("onListItemClick",
						new Class[] { ListView.class, View.class, int.class,
								long.class });
				method.setAccessible(true);
				int i = random(listView.getChildCount());
				View childView = listView.getChildAt(i);
				Adapter adapter = listView.getAdapter();
				long itemId = adapter.getItemId(i);
				try {
					String msg = "In "+activity.getClass().getName()+" The "+i+"th item in ListActivity is invoked!";
					Log.v(Constants.LogTag.eventTag, msg);
					method.invoke(activity, new Object[]{listView, childView, i, itemId });
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 4. UI event from preference activity. invoke onItemClick of
		 * PreferenceActivity
		 * */
		public static void testPreAct(PreferenceActivity pActivity) {
//			PreferenceActivity pActivity = (PreferenceActivity) activity;
			PreferenceScreen pScreen = pActivity.getPreferenceScreen();
			// int cou = pScreen.getPreferenceCount();
			ListView listView = pActivity.getListView();
			int count = listView.getCount();
			int i = random(count);
			String msg = "In "+pActivity.getClass().getName()+" The "+i+"th item in PreActivity is invoked!";
			Log.v(Constants.LogTag.eventTag, msg);
			View view = listView.getChildAt(i);
			long itemId = listView.getAdapter().getItemId(i);
			pScreen.onItemClick(listView, view, i, itemId);
		}
		
		/**
		 * 5. Context Menu Event
		 * */
		public static void testContextMenu(Activity activity) {
			Field field;
			try {
				field = activity.getClass().getField(
						Constants.EHBField.CONTEXTMENU);
				Object object = field.get(null);
				ContextMenu contextMenu = (ContextMenu)object;
				int random = random(contextMenu.size());
				activity.onContextItemSelected(contextMenu.getItem(random));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Due to the modifier of construct DragEvent() is private, we cannot 
	 * obtain its instance directly, therefore, we use Java Reflection to obtain the 
	 * private construct and call the init() method to initialize. 
	 * <p>
	 * init method signature: 
	 * void init(int action, float x, float y, ClipDescription description, ClipData data,Object localState, boolean result) 
	* @param objects Objects set as init arguments.
	* @return dragEvent A drag instance.	
	*/
	public static DragEvent genDragEvent(Object[] objects){
		Class class1 = DragEvent.class;
		Constructor[] cons = class1.getDeclaredConstructors();
		Constructor con = cons[0];
		con.setAccessible(true);
		try {
			Object instance = con.newInstance(null);
			DragEvent dragEvent = (DragEvent)instance;
			try {
				Method[] methods = class1.getMethods();
				Method method = class1.getDeclaredMethod("init", new Class[]{int.class, float.class, float.class, 
						ClipDescription.class, ClipData.class,Object.class, boolean.class});
				method.setAccessible(true);
				method.invoke(instance, objects);
				return dragEvent;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	//
//	public static DragEvent genContextMenu(Object[] objects){
//		
//	}
	
	public static int random(int size){
		Random rand = new Random();
		int randNum = rand.nextInt(size);
		return randNum;
//		double random = Math.random();
//		int randomInt = (int)(random*size);
//		return randomInt;
	}
	
}
