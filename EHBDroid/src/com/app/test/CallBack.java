package com.app.test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CallBack implements Serializable{
	public static Map<String, List<String>> dialogToCallBacks = new HashMap<String, List<String>>();
	public static Map<String, List<String>> viewToCallBacks = new HashMap<String, List<String>>();
	public static Map<String, List<String>> serviceToCallBacks = new HashMap<String, List<String>>();
	
	public static Set<String> viewRegistars;
	public static Set<String> dialogRegistars;
	public static Set<String> serViceRegistars;
	//android.view.View: 1-10 
	//1. onKey
	public static final String SETONKEYLISTENER = "void setOnKeyListener(android.view.View$OnKeyListener)";
	public static final String ONKEYLISTENER = "android.view.View$OnKeyListener";
	public static final String ONKEY = "boolean onKey(android.view.View,int,android.view.KeyEvent)";
	public static final String[] array1 = {ONKEY};
	
	//2. onTouch
	public static final String SETONTOUCHLISTENER = "void setOnTouchListener(android.view.View$OnTouchListener)";
	public static final String ONTOUCHLISTENER = "android.view.View$OnTouchListener";
	public static final String ONTOUCH = "boolean onTouch(android.view.View,android.view.MotionEvent)";
	public static final String[] array2 = {ONTOUCH};
	
	//3. onHover
	public static final String SETONHOVERLISTENER = "void setOnHoverListener(android.view.View$OnHoverListener)";
	public static final String ONHOVERLISTENER = "android.view.View$OnHoverListener";
	public static final String ONHOVER = "boolean onHover(android.view.View,android.view.MotionEvent)";
	public static final String[] array3 = {ONHOVER};
	
	//4. onGenericMotion
	public static final String SETONGENERICMOTIONLISTENER = "void setOnGenericMotionListener(android.view.View$OnGenericMotionListener)";
	public static final String ONGENERICMOTIONLISTENER = "android.view.View$OnGenericMotionListener";
	public static final String ONGENERICMOTION = "boolean onGenericMotion(android.view.View,android.view.MotionEvent)";
	public static final String[] array4 = {ONGENERICMOTION};
	
	//5. onLongClick
	public static final String SETONLONGCLICKLISTENER = "void setOnLongClickListener(android.view.View$OnLongClickListener)";
	public static final String ONLONGCLICKLISTENER = "android.view.View$OnLongClickListener";
	public static final String ONLONGCLICK = "boolean onLongClick(android.view.View)";
	public static final String[] array5 = {ONLONGCLICK};
	
	//6. onDrag
	public static final String SETONDRAGLISTENER = "void setOnDragListener(android.view.View$OnDragListener)";
	public static final String ONDRAGLISTENER = "android.view.View$OnDragListener";
	public static final String ONDRAG = "boolean onDrag(android.view.View,android.view.DragEvent)";
	public static final String[] array6 = {ONDRAG};
	
	//7. onFocusChange
	public static final String SETONFOCUSCHANGELISTENER = "void setOnFocusChangeListener(android.view.View$OnFocusChangeListener)";
	public static final String ONFOCUSCHANGELISTENER = "android.view.View$OnFocusChangeListener";
	public static final String ONFOCUSCHANGE = "void onFocusChange(android.view.View,boolean)";
	public static final String[] array7 = {ONFOCUSCHANGE};
	
	//8. onClick
	public static final String SETONCLICKLISTENER = "void setOnClickListener(android.view.View$OnClickListener)";
	public static final String ONCLICKLISTENER = "android.view.View$OnClickListener";
	public static final String ONCLICK = "void onClick(android.view.View)";
	public static final String[] array8 = {ONCLICK};
	
	//9. onCreateContextMenuListener
	public static final String SETONCREATECONTEXTMENULISTENER = "void setOnCreateContextMenuListener(android.view.View$OnCreateContextMenuListener)";
	public static final String ONCREATECONTEXTMENULISTENER = "android.view.View$OnCreateContextMenuListener";
	public static final String ONCONTEXTITEMSELECTED = "boolean onContextItemSelected(android.view.MenuItem)";
	public static final String ONCREATECONTEXTMENU = "void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenu$ContextMenuInfo)";
	public static final String[] array9 = {ONCREATECONTEXTMENU};
	
	//10. onSystemUiVisibilityChangeListener
	public static final String SETONSYSTEMUIVISIBILITYCHANGELISTENER = "void setOnSystemUiVisibilityChangeListener(android.view.View$OnSystemUiVisibilityChangeListener)";
	public static final String ONSYSTEMUIVISIBILITYCHANGELISTENER = "android.view.View$OnSystemUiVisibilityChangeListener";
	public static final String ONSYSTEMUIVISIBILITYCHANGE = "void onSystemUiVisibilityChange(int)";
	public static final String[] array10 = {ONSYSTEMUIVISIBILITYCHANGE};
	
	//android.preference.PreferenceActivity:11
	//11. onPreferenceTreeClick
	public static final String SETONPREFERENCECLICKLISTENER = "void setOnPreferenceClickListener(android.preference.Preference$OnPreferenceClickListener)";
	public static final String ONPREFERENCECLICKLISTENER = "android.preference.Preference$OnPreferenceClickListener";
	public static final String ONPREFERENCETREECLICK = "boolean onPreferenceTreeClick(android.preference.PreferenceScreen,android.preference.Preference)";
	public static final String[] array11 = {ONPREFERENCETREECLICK};
	
	//android.widget.TextView:12
	//12. OnEditorActionListener
	public static final String SETONEDITORACTIONLISTENER = "void setOnEditorActionListener(android.widget.TextView$OnEditorActionListener)";
	public static final String ONEDITORACTIONLISTENER = "android.widget.TextView$OnEditorActionListener";
	public static final String ONEDITORACTION = "boolean onEditorAction(android.widget.TextView,int,android.view.KeyEvent)";
	public static final String[] array12 = {ONEDITORACTION};
	
	//android.view.ViewGroup:13
	//13. OnHierarchyChangeListener
	public static final String SETONHIERARCHYCHANGELISTENER = "void setOnHierarchyChangeListener(android.view.ViewGroup$OnHierarchyChangeListener)";
	public static final String ONHIERARCHYCHANGELISTENER = "android.view.ViewGroup$OnHierarchyChangeListener";
	public static final String ONCHILDVIEWADDED = "void onChildViewAdded(android.view.View,android.view.View)";
	public static final String ONCHILDVIEWREMOVED = "void onChildViewRemoved(android.view.View,android.view.View)";
	public static final String[] array13 = {ONCHILDVIEWADDED,ONCHILDVIEWREMOVED};
	
	//android.widget.AdapterView:14-16
	//14. OnItemClickListener
	public static final String SETONITEMCLICKLISTENER = "void setOnItemClickListener(android.widget.AdapterView$OnItemClickListener)";
	public static final String ONITEMCLICKLISTENER = "android.widget.AdapterView$OnItemClickListener";
	public static final String ONITEMCLICK = "void onItemClick(android.widget.AdapterView,android.view.View,int,long)";
	public static final String[] array14 = {ONITEMCLICK};
	
	//15. OnItemLongClickListener
	public static final String SETONITEMLONGCLICKLISTENER = "void setOnItemLongClickListener(android.widget.AdapterView$OnItemLongClickListener)";
	public static final String ONITEMLONGCLICKLISTENER = "android.widget.AdapterView$OnItemLongClickListener";
	public static final String ONITEMLONGCLICK = "boolean onItemLongClick(android.widget.AdapterView,android.view.View,int,long)";
	public static final String[] array15 = {ONITEMLONGCLICK};
	
	//16. OnItemSelectedListener
	public static final String SETONITEMSELECTEDLISTENER = "void setOnItemSelectedListener(android.widget.AdapterView$OnItemSelectedListener)";
	public static final String ONITEMSELECTEDLISTENER = "android.widget.AdapterView$OnItemSelectedListener";
	public static final String ONITEMSELECTED = "void onItemSelected(android.widget.AdapterView,android.view.View,int,long)";
	public static final String ONNOTHINGSELECTED = "void onNothingSelected(android.widget.AdapterView)";
	public static final String[] array16 = {ONITEMSELECTED,ONNOTHINGSELECTED};
	
	//android.widget.SeekBar:17
	//17. OnSeekBarChangeListener
	public static final String setOnSeekBarChangeListener = "void setOnSeekBarChangeListener(android.widget.SeekBar$OnSeekBarChangeListener)";
	public static final String OnSeekBarChangeListener = "android.widget.SeekBar$OnSeekBarChangeListener";
	public static final String onProgressChanged = "void onProgressChanged(android.widget.SeekBar,int,boolean)";
	public static final String onStartTrackingTouch = "void onStartTrackingTouch(android.widget.SeekBar)";
	public static final String onStopTrackingTouch = "void onStopTrackingTouch(android.widget.SeekBar)";
	public static final String[] array17 = {onProgressChanged,onStartTrackingTouch,onStopTrackingTouch};
	
	//android.widget.CompoundButton:18
	//18. OnCheckedChangeListener
	public static final String setOnCheckedChangeListener = "void setOnCheckedChangeListener(android.widget.CompoundButton$OnCheckedChangeListener)";
	public static final String OnCheckedChangeListener = "android.widget.CompoundButton$OnCheckedChangeListener";
	public static final String onCheckedChanged = "void onCheckedChanged(android.widget.CompoundButton,boolean)";
	public static final String[] array18 = {onCheckedChanged};
	
	//android.widget.RatingBar
	//19. OnRatingBarChangeListener
	public static final String setOnRatingBarChangeListener = "void setOnRatingBarChangeListener(android.widget.RatingBar$OnRatingBarChangeListener)";
	public static final String OnRatingBarChangeListener = "android.widget.RatingBar$OnRatingBarChangeListener";
	public static final String onRatingChanged = "void onRatingChanged(android.widget.RatingBar,float,boolean)";
	public static final String[] array19 = {onRatingChanged};
	
	//android.view.ViewStub
	//20. OnInflateListener 
	public static final String setOnInflateListener = "void setOnInflateListener(android.view.ViewStub$OnInflateListener)";
	public static final String OnInflateListener = "android.view.ViewStub$OnInflateListener";
	public static final String onInflate = "void onInflate(android.view.ViewStub,android.view.View)";
	public static final String[] array20 = {onInflate};
	
	//android.widget.NumberPicker
	//21. OnScrollListener
	public static final String NumberPicker_setOnScrollListener = "void setOnScrollListener(android.widget.NumberPicker$OnScrollListener)";
	public static final String NumberPicker_OnScrollListener = "android.widget.NumberPicker$OnScrollListener";
	public static final String NumberPicker_onScrollStateChange = "void onScrollStateChange(android.widget.NumberPicker,int)";
	public static final String[] array21 = {NumberPicker_onScrollStateChange};
	
	//22. OnValueChangeListener
	public static final String setOnValueChangeListener = "void setOnValueChangeListener(android.widget.NumberPicker$OnValueChangeListener)";
	public static final String OnValueChangeListener = "android.widget.NumberPicker$OnValueChangeListener";
	public static final String onValueChange = "void onValueChange(android.widget.NumberPicker,int,int)";
	public static final String[] array22 = {ONTOUCH};
	
	//android.widget.AbsListView
	//23. OnScrollListener
	public static final String AbsListView_setOnScrollListener = "void setOnScrollListener(android.widget.AbsListView$OnScrollListener)";
	public static final String AbsListView_OnScrollListener = "android.widget.AbsListView$OnScrollListener";
	public static final String onScrollStateChanged = "void onScrollStateChanged(android.widget.AbsListView,int)";
	public static final String onScroll = "void onScroll(android.widget.AbsListView,int,int,int)";
	public static final String[] array23 = {onScrollStateChanged,onScroll};
	
	//android.content.DialogInterface
	//24. OnCancelListener
	public static final String DialogInterface_setOnCancelListener = "void setOnCancelListener(android.content.DialogInterface$OnCancelListener)";
	public static final String DialogInterface_OnCancelListener = "android.content.DialogInterface$OnCancelListener";
	public static final String DialogInterface_onCancel = "void onCancel(android.content.DialogInterface)";
	public static final String[] array24 = {DialogInterface_onCancel};
	
	//25.OnDismissListener
	public static final String DialogInterface_setOnDismissListener = "void setOnDismissListener(android.content.DialogInterface$OnDismissListener)";
	public static final String DialogInterface_OnDismissListener = "android.content.DialogInterface$OnDismissListener";
	public static final String DialogInterface_onDismiss = "void onDismiss(android.content.DialogInterface)";
	public static final String[] array25 = {DialogInterface_onDismiss};
	
	//26.OnShowListener
	public static final String DialogInterface_setOnShowListener = "void setOnShowListener(android.content.DialogInterface$OnShowListener)";
	public static final String DialogInterface_OnShowListener = "android.content.DialogInterface$OnShowListener";
	public static final String DialogInterface_onShow = "void onShow(android.content.DialogInterface)";
	public static final String[] array26 = {DialogInterface_onShow};
	
	//27.OnClickListener
	public static final String DialogInterface_setOnClickListener = "void setOnClickListener(android.content.DialogInterface$OnClickListener)";
	public static final String DialogInterface_OnClickListener = "android.content.DialogInterface$OnClickListener";
	public static final String DialogInterface_onClick = "void onClick(android.content.DialogInterface,int)";
	public static final String[] array27 = {DialogInterface_onClick};
	
	//28. OnMultiChoiceClickListener
	public static final String DialogInterface_setOnMultiChoiceClickListener = "void setOnMultiChoiceClickListener(android.content.DialogInterface$OnMultiChoiceClickListener)";
	public static final String DialogInterface_OnMultiChoiceClickListener = "android.content.DialogInterface$OnMultiChoiceClickListener";
	public static final String DialogInterface_OnMultiChoiceClickListener_onClick = "void onClick(android.content.DialogInterface,int,boolean)";
	public static final String[] array28 = {DialogInterface_OnMultiChoiceClickListener_onClick};
	
	//29.OnKeyListener
	public static final String DialogInterface_setOnKeyListener = "void setOnKeyListener(android.content.DialogInterface$OnKeyListener)";
	public static final String DialogInterface_OnKeyListener = "android.content.DialogInterface$OnKeyListener";
	public static final String DialogInterface_onKey = "boolean onKey(android.content.DialogInterface,int,android.view.KeyEvent)";
	public static final String[] array29 = {DialogInterface_onKey};
	
	//android.support.v4.view.ViewPager
	//30. OnPageChangeListener
	public static final String setOnPageChangeListener = "void setOnPageChangeListener(android.support.v4.view.ViewPager$OnPageChangeListener)";
	public static final String OnPageChangeListener = "android.support.v4.view.ViewPager$OnPageChangeListener";
	public static final String onPageSelected = "void onPageSelected(int)";
	public static final String onPageScrolled = "void onPageScrolled(int,float,int)";
	public static final String onPageScrollStateChanged = "void onPageScrollStateChanged(int)";
	public static final String[] array30 = {onPageSelected,onPageScrolled,onPageScrollStateChanged};
	
	//android.widget.SearchView
	//31. OnQueryTextListener
	public static final String setOnQueryTextListener = "void setOnQueryTextListener(android.widget.SearchView$OnQueryTextListener)";
	public static final String OnQueryTextListener = "android.widget.SearchView$OnQueryTextListener";
	public static final String onQueryTextSubmit = "boolean onQueryTextSubmit(java.lang.String)";
	public static final String onQueryTextChange = "boolean onQueryTextChange(java.lang.String)";
	public static final String[] array31 = {onQueryTextSubmit,onQueryTextChange};
	
	//32. OnCloseListener
	public static final String setOnCloseListener = "void setOnCloseListener(android.widget.SearchView$OnCloseListener)";
	public static final String OnCloseListener = "android.widget.SearchView$OnCloseListener";
	public static final String onClose = "boolean onClose()";
	public static final String[] array32 = {onClose};
	
	//33.OnSuggestionListener
	public static final String setOnSuggestionListener = "void setOnSuggestionListener(android.widget.SearchView$OnSuggestionListener)";
	public static final String OnSuggestionListener = "android.widget.SearchView$OnSuggestionListener";
	public static final String onSuggestionSelect = "boolean onSuggestionSelect(int)";
	public static final String onSuggestionClick = "boolean onSuggestionClick(int)";
	public static final String[] array33 = {onSuggestionSelect,onSuggestionClick};

	//34. Receiver
	public static final String receiver_registerReceiver = "void registerReceiver(android.content.BroadcastReceiver,android.content.IntentFilter)";
	
	//35. android.hardware.SensorEventListener
	public static final String service_registerListener1 = "boolean registerListener(android.hardware.SensorEventListener,int)";
	public static final String service_registerListener2 = "boolean registerListener(android.hardware.SensorEventListener,int,int)";
	public static final String service_registerListener3 = "boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int)";
	public static final String service_registerListener4 = "boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,android.os.Handler)";
	public static final String SensorEventListener = "android.hardware.SensorEventListener";
	public static final String onSensorChanged = "void onSensorChanged(android.hardware.SensorEvent)";
	public static final String onAccuracyChanged = "void onAccuracyChanged(android.hardware.Sensor,int)";
	public static final String[] array35 = {onSensorChanged,onAccuracyChanged};
	
	//36. android.media.AudioManager$OnAudioFocusChangeListener
	public static final String service_requestAudioFocus = "int requestAudioFocus(android.media.AudioManager$OnAudioFocusChangeListener,int,int)";
	public static final String OnAudioFocusChangeListener = "android.media.AudioManager$OnAudioFocusChangeListener";
	public static final String onAudioFocusChange = "void onAudioFocusChange(int)";
	public static final String[] array36 = {onAudioFocusChange};
	
	//37. android.location.GpsStatus$Listener
	public static final String service_requestLocationUpdates = "void requestLocationUpdates(java.lang.String,long,float,android.location.GpsStatus$Listener)";
	public static final String service_addGpsStatusListener = "boolean addGpsStatusListener(android.location.GpsStatus$Listener)";
	public static final String Listener = "android.location.GpsStatus$Listener";
	public static final String onGpsStatusChanged = "void onGpsStatusChanged(int)";
	public static final String[] array37 = {onGpsStatusChanged};

	//38. android.location.GpsStatus$NmeaListener
	public static final String service_addNmeaListener = "boolean addNmeaListener(android.location.GpsStatus$NmeaListener)";
	public static final String NmeaListener = "android.location.GpsStatus$NmeaListener";
	public static final String onNmeaReceived = "void onNmeaReceived(long,java.lang.String)";
	public static final String[] array38 = {onNmeaReceived};

	//39. android.location.LocationListener
	public static final String service_requestSingleUpdate = "void requestSingleUpdate(java.lang.String,android.location.LocationListener,android.os.Looper)";
	public static final String LocationListener = "android.location.LocationListener";
	public static final String onLocationChanged = "void onLocationChanged(android.location.Location)";
	public static final String onStatusChanged = "void onStatusChanged(java.lang.String,int,android.os.Bundle)";
	public static final String onProviderEnabled = "void onProviderEnabled(java.lang.String)";
	public static final String onProviderDisabled = "void onProviderDisabled(java.lang.String)";
	public static final String[] array39 = {onLocationChanged,onStatusChanged,onProviderEnabled,onProviderDisabled};

	public static final String service_addProximityAlert = "void addProximityAlert(double,double,float,long,android.app.PendingIntent)";
	public static final String service_registerMediaButtonEventReceiver = "void registerMediaButtonEventReceiver(android.content.ComponentName)";
	public static final String service_listen = "void listen(android.telephony.PhoneStateListener,int)";
	
	static{
		viewToCallBacks.put(setOnCheckedChangeListener,Arrays.asList(array18));
		viewToCallBacks.put(setOnCloseListener,Arrays.asList(array32));
		viewToCallBacks.put(setOnInflateListener,Arrays.asList(array20));
		viewToCallBacks.put(SETONITEMSELECTEDLISTENER,Arrays.asList(array16));
		viewToCallBacks.put(setOnPageChangeListener,Arrays.asList(array30));
		viewToCallBacks.put(setOnQueryTextListener,Arrays.asList(array31));
		viewToCallBacks.put(setOnRatingBarChangeListener,Arrays.asList(array19));
		viewToCallBacks.put(setOnSeekBarChangeListener,Arrays.asList(array17));
		viewToCallBacks.put(setOnSuggestionListener,Arrays.asList(array33));
		viewToCallBacks.put(setOnValueChangeListener,Arrays.asList(array22));
		viewToCallBacks.put(SETONCLICKLISTENER,Arrays.asList(array8));
		viewToCallBacks.put(SETONCREATECONTEXTMENULISTENER,Arrays.asList(array9));
		viewToCallBacks.put(SETONDRAGLISTENER,Arrays.asList(array6));
		viewToCallBacks.put(SETONEDITORACTIONLISTENER,Arrays.asList(array12));
		viewToCallBacks.put(SETONFOCUSCHANGELISTENER,Arrays.asList(array7));
		viewToCallBacks.put(SETONGENERICMOTIONLISTENER,Arrays.asList(array4));
		viewToCallBacks.put(SETONHIERARCHYCHANGELISTENER,Arrays.asList(array13));
		viewToCallBacks.put(SETONHOVERLISTENER,Arrays.asList(array3));
		viewToCallBacks.put(SETONITEMCLICKLISTENER,Arrays.asList(array14));
		viewToCallBacks.put(SETONITEMLONGCLICKLISTENER,Arrays.asList(array15));
		viewToCallBacks.put(SETONKEYLISTENER,Arrays.asList(array1));
		viewToCallBacks.put(SETONLONGCLICKLISTENER,Arrays.asList(array5));
		viewToCallBacks.put(SETONPREFERENCECLICKLISTENER,Arrays.asList(array11));
		viewToCallBacks.put(SETONSYSTEMUIVISIBILITYCHANGELISTENER,Arrays.asList(array10));
		viewToCallBacks.put(SETONTOUCHLISTENER,Arrays.asList(array2));
		viewToCallBacks.put(NumberPicker_setOnScrollListener,Arrays.asList(array21));
		viewToCallBacks.put(AbsListView_setOnScrollListener,Arrays.asList(array23));
		
		serviceToCallBacks.put(service_registerListener1, Arrays.asList(array35));
		serviceToCallBacks.put(service_registerListener2, Arrays.asList(array35));
		serviceToCallBacks.put(service_registerListener3, Arrays.asList(array35));
		serviceToCallBacks.put(service_registerListener4, Arrays.asList(array35));
		serviceToCallBacks.put(service_requestAudioFocus, Arrays.asList(array36));
		serviceToCallBacks.put(service_requestLocationUpdates, Arrays.asList(array37));
		serviceToCallBacks.put(service_addGpsStatusListener, Arrays.asList(array37));
		serviceToCallBacks.put(service_addNmeaListener,Arrays.asList( array38));
		serviceToCallBacks.put(service_requestSingleUpdate, Arrays.asList(array39));
		
		dialogToCallBacks.put(DialogInterface_setOnCancelListener,Arrays.asList(array24));
		dialogToCallBacks.put(DialogInterface_setOnClickListener,Arrays.asList(array27));
		dialogToCallBacks.put(DialogInterface_setOnDismissListener,Arrays.asList(array25));
		dialogToCallBacks.put(DialogInterface_setOnKeyListener,Arrays.asList(array29));
		dialogToCallBacks.put(DialogInterface_setOnMultiChoiceClickListener,Arrays.asList(array28));
		dialogToCallBacks.put(DialogInterface_setOnShowListener,Arrays.asList(array26));
	}
	
	public static Set<String> getDialogRegistars() {
		return dialogToCallBacks.keySet();
	}

	public static Set<String> getViewRegistars() {
		return viewToCallBacks.keySet();
	}

	public static Set<String> getSerViceRegistars() {
		return serviceToCallBacks.keySet();
	}	
	
}
