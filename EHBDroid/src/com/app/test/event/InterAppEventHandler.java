package com.app.test.event;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.app.test.AppDir;
import com.app.test.Constants;
import com.app.test.Util;
import com.app.test.data.AndroidIntentFilter;
import com.app.test.data.AndroidIntentFilter.AuthorityEntry;
import com.app.test.data.PatternMatcher;

public class InterAppEventHandler {

//	protected static String menuName = Constants.interTest;
	public static final String itemName = Constants.interTest; 

	public static String file = AppDir.file;	
	
	public static void addMenuItem(final Activity activity,Menu menu){
		MenuItem add = menu.add(itemName);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				doInterAppEventTest(activity);
				return true;
			}
		});
	}
	
//	public void doTest(Activity activity) {
//		doInterAppEventTest(activity);
//	}
	
	public static void doInterAppEventTest(Activity activity){
		try {
			Map<String, List<AndroidIntentFilter>> activityToFilters = readActivityToFilters(file);
			for(String key:activityToFilters.keySet()){
				for(AndroidIntentFilter intentFilter:activityToFilters.get(key)){
					Class receiverClass = Class.forName(key);
					Object receiver = receiverClass.newInstance();
					Activity act = (Activity)receiver;
					Intent intent = buildIntent(intentFilter);
					Util.LogInterAppEvent(act, intent);
					activity.startActivity(intent);
				}
			}
		} catch (Exception e) {
			Util.LogException(e);
		}finally{
			Log.v("EVENT", AppDir.visitedMethodCount+"");
		}
	}

	/**
	 * read activitytofilter from file.
	 * */
	private static Map<String, List<AndroidIntentFilter>> readActivityToFilters(String file){
		Map<String, List<AndroidIntentFilter>> activityToFilters = new HashMap<String, List<AndroidIntentFilter>>();
		try{
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ois.readObject();
			// activitytofilter was in the second position.
			Object readObject = ois.readObject();
			activityToFilters = (Map<String, List<AndroidIntentFilter>>)readObject;
			ois.close();
			fis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return activityToFilters;
	}
	
	
	/**
	 * build intent according to intentFilter from xml
	 * @param intentFilter intent filter from AndroidManifest.xml.
	 * @return intent An intent used to trigger inter-app events.
	 * */
//	private static Intent initIntent(AndroidIntentFilter intentFilter) {
//		String action = null;
//		
//		if(intentFilter.countActions()==0){
//			return null;
//		}
//		for(String mAction: intentFilter.getmActions()){
//			if(!mAction.equals(Intent.ACTION_MAIN)){
//				action = mAction;
//				break;
//			}
//		}
//		if(action==null) {
//			Log.v("EVENT", " Intent Action is null");
//			return null;
//		};
//		
//		Intent intent = new Intent(action);
//		if(intentFilter.countCategories()>0){
//			intent.addCategory(intentFilter.getCategory(0));
//		}
//		else 
//			intent.addCategory(Intent.CATEGORY_DEFAULT);
//		if(intentFilter.countDataTypes()>0){
//			intent.setType(intentFilter.getDataType(0));
//		}
//		return intent;
//	}
	
	
	/**
	 * build intent object according to androidIntentFilter.
	 * buliding method relys on mapping mechnisam between intent and intentfilter.
	 * @param androidIntentFilter intentfilter retrieved from xml.
	 * */
	private static Intent buildIntent(AndroidIntentFilter androidIntentFilter){
		
		String action = androidIntentFilter.getAction(0);
		if(Intent.ACTION_MAIN.equals(action)){
			if(androidIntentFilter.countActions()>1){
				action = androidIntentFilter.getAction(1);
			}	
			else return null;
		}
		String cat = androidIntentFilter.countCategories()>0?androidIntentFilter.getCategory(0):null;
		String mimeType = androidIntentFilter.countDataTypes()>0?androidIntentFilter.getDataType(0):null;
		
		String scheme = androidIntentFilter.countDataSchemes()>0?androidIntentFilter.getDataScheme(0):null;
		if(scheme==null){
			return buildIntent(action, cat, mimeType, null);
		}
		
		AuthorityEntry aEntry = androidIntentFilter.countDataAuthorities()>0?androidIntentFilter.getDataAuthority(0):null;
		if(aEntry==null){
			return  buildIntent(action, cat, mimeType, Uri.parse(scheme+"://"));
		}
		
		String host = aEntry.getHost();
		int port = aEntry.getPort();
		String portString = port==-1?"":(":"+port);
		
		PatternMatcher pMatcher = androidIntentFilter.countDataPaths()>0?androidIntentFilter.getDataPath(0):null;
		if(pMatcher==null){
			return  buildIntent(action, cat, mimeType, Uri.parse(scheme+"://"+host+portString));
		}
		String path = pMatcher.getPath();
		int type = pMatcher.getType();
		String uriString;
		if(type==PatternMatcher.PATTERN_SIMPLE_GLOB){
			if(path.startsWith(".*\\.")){
				String replace = path.replace(".*\\.", "qian.");
				uriString = scheme+"://"+host+portString+"/"+replace;
			}
			else 
				uriString = scheme+"://"+host+portString+path;
		}
		else{
			uriString = scheme+"://"+host+portString+path;
		}
		return buildIntent(action, cat, mimeType, Uri.parse(uriString));
	}
	
	/**
	 * select the right data&Type method to intent.
	 * */
	private static Intent buildIntent(String action,String category, String mimeType, Uri uri){
		Intent intent = new Intent(action);
		if(category!=null){
			intent.addCategory(category);
		}
		if(mimeType!=null&&uri!=null)
			intent.setDataAndType(uri, mimeType);
		else if(mimeType!=null)
			intent.setType(mimeType);
		else if(uri!=null)
			intent.setData(uri);
		return intent;
	}
}
