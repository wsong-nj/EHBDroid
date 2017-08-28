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
package ehb.xml.manifest;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;


import com.app.test.data.AndroidIntentFilter;
import com.app.test.data.PatternMatcher;
import com.content.res.xmlprinter.AXmlResourceParser;

import ehb.global.Global;
import ehb.global.GlobalHost;

public class ProcessManifest implements GlobalHost{
	
	private final HashSet<String> entryPointsClasses = new HashSet<String>();
	private String applicationName = "";
	
	private int versionCode = -1;
	private String versionName = "";

	private String packageName = "";
	private int minSdkVersion = -1;
	private int targetSdkVersion = -1;
	
	public String mainActivity = null;
	
	private final Set<String> permissions = new HashSet<String>();
	
	private Map<String, List<AndroidIntentFilter>> activityToFilters = new HashMap<String, List<AndroidIntentFilter>>();
	private Map<String, List<AndroidIntentFilter>> receiverToFilters = new HashMap<String, List<AndroidIntentFilter>>();
	private Map<String, List<AndroidIntentFilter>> serviceToFilters = new HashMap<String, List<AndroidIntentFilter>>();
	
//	List<ActivityAndFilter> aFilters = new ArrayList<ActivityAndFilter>();
	/**
	 * Opens the given apk file and provides the given handler with a stream for
	 * accessing the contained android manifest file
	 * @param apk The apk file to process
	 * @param handler The handler for processing the apk file
	 * 
	 * @author Steven Arzt
	 */
	private void handleAndroidManifestFile(String apk, IManifestHandler handler) {
		File apkF = new File(apk);
		if (!apkF.exists())
			throw new RuntimeException("file '" + apk + "' does not exist!");

		boolean found = false;
		try {
			ZipFile archive = null;
			try {
				archive = new ZipFile(apkF);
				Enumeration<?> entries = archive.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					String entryName = entry.getName();
					// We are dealing with the Android manifest
					if (entryName.equals("AndroidManifest.xml")) {			
						found = true;
						handler.handleManifest(archive.getInputStream(entry));
						break;
					}
				}
			}
			finally {
				if (archive != null)
					archive.close();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(
					"Error when looking for manifest in apk: " + e);
		}
		if (!found)
			throw new RuntimeException("No manifest file found in apk");
	}
	
	public void loadManifestFile(String apk) {
		handleAndroidManifestFile(apk, new IManifestHandler() {
			
			@Override
			public void handleManifest(InputStream stream) { 
				loadClassesFromBinaryManifest(stream);
				
			}
			
		});
	}
	
	protected void loadClassesFromBinaryManifest(InputStream manifestIS) {
		try {
			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(manifestIS);
			int type = -1;
			AndroidIntentFilter intentFilter = null;
			String serviceName = null;
			String activityName = null;
			String aliasActivityName = null;
			String receiverName = null;
			boolean applicationEnabled = true;
			while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (type) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						String tagName = parser.getName();
						if (tagName.equals("manifest")) {
							this.packageName = getAttributeValue(parser, "package");
							String versionCode = getAttributeValue(parser, "versionCode");
							if (versionCode != null && versionCode.length() > 0)
								this.versionCode = Integer.valueOf(versionCode);
							this.versionName = getAttributeValue(parser, "versionName");
						}
						else if (tagName.equals("provider")) {
							// We ignore disabled activities
							if (!applicationEnabled)
								continue;
							String attrValue = getAttributeValue(parser, "enabled");
							if (attrValue != null && attrValue.equals("false"))
								continue;							
							// Get the class name
							attrValue = getAttributeValue(parser, "name");
							//×¢ÊÍ5.13
							//entryPointsClasses.add(expandClassName(attrValue));
						}
						else if (tagName.equals("receiver")) {
															
							// We ignore disabled activities
							if (!applicationEnabled)
								continue;
							String attrValue = getAttributeValue(parser, "enabled");
							if (attrValue != null && attrValue.equals("false"))
								continue;							
							// Get the class name
							attrValue = getAttributeValue(parser, "name");
							receiverName = expandClassName(attrValue);
							receiverToFilters.put(receiverName, null);
							//×¢ÊÍ5.13
							//entryPointsClasses.add(expandClassName(attrValue));
						}
						else if (tagName.equals("service")) {
							// We ignore disabled activities
							if (!applicationEnabled)
								continue;
							String attrValue = getAttributeValue(parser, "enabled");
							if (attrValue != null && attrValue.equals("false"))
								continue;							
							// Get the class name
							attrValue = getAttributeValue(parser, "name");
							serviceName = expandClassName(attrValue);
							serviceToFilters.put(serviceName, null);
							//entryPointsClasses.add(expandClassName(attrValue));
						}
						else if("activity".equals(tagName)){
							String name = getAttributeValue(parser, "name");
							activityName = expandClassName(name);
							entryPointsClasses.add(activityName);
							
							String flag = getAttributeValue(parser, "exported");
							if(flag==null||flag=="true")
								activityToFilters.put(activityName, null);
						}
						else if("activity-alias".equals(tagName)){
							String name = getAttributeValue(parser, "targetActivity");
							aliasActivityName = expandClassName(name);
						}
						else if (tagName.equals("uses-permission")) {
							String permissionName = getAttributeValue(parser, "name");
							// We probably don't want to do this in some cases, so leave it
							// to the user
							// permissionName = permissionName.substring(permissionName.lastIndexOf(".") + 1);
							this.permissions.add(permissionName);
						}
						else if (tagName.equals("uses-sdk")) {
							String minVersion = getAttributeValue(parser, "minSdkVersion");
							if (minVersion != null && minVersion.length() > 0)
								this.minSdkVersion = Integer.valueOf(minVersion);
							String targetVersion = getAttributeValue(parser, "targetSdkVersion");
							if (targetVersion != null && targetVersion.length() > 0)
								this.targetSdkVersion = Integer.valueOf(targetVersion);
						}
						else if (tagName.equals("application")) {
							// Check whether the application is disabled
							String attrValue = getAttributeValue(parser, "enabled");
							applicationEnabled = (attrValue == null || !attrValue.equals("false"));
							
							// Get the application name which is also the fully-qualified
							// name of the custom application object
							this.applicationName = getAttributeValue(parser, "name");
						}
						
						//handle intent-filter
						if("intent-filter".equals(tagName)){
							intentFilter = new AndroidIntentFilter();
							if(serviceName!=null){
								if(serviceToFilters.get(serviceName)==null)
									serviceToFilters.put(serviceName, ArrayUtil.toList(intentFilter));
								else
									serviceToFilters.get(serviceName).add(intentFilter);
							}
							else if(activityName!=null){
								if(activityToFilters.get(activityName)==null)
									activityToFilters.put(activityName, ArrayUtil.toList(intentFilter));
								else
									activityToFilters.get(activityName).add(intentFilter);
							}
							else if(receiverName!=null){
								if(receiverToFilters.get(receiverName)==null)
									receiverToFilters.put(receiverName, ArrayUtil.toList(intentFilter));
								else
									receiverToFilters.get(receiverName).add(intentFilter);
							}
						}
						else if("action".equals(tagName)){
							String name = getAttributeValue(parser, "name");
							if("android.intent.action.MAIN".equals(name)){
								if(mainActivity==null){
									mainActivity = activityName==null?aliasActivityName:activityName;
								}
							}
							intentFilter.addAction(name);
						}
						else if("category".equals(tagName)){
							String name = getAttributeValue(parser, "name");
							if(name!=null)
								intentFilter.addCategory(name);
						}
						else if("data".equals(tagName)){
							String types = getAttributeValue(parser, "mimeType");
							if(types!=null)
								intentFilter.addDataType(types);
							
							String scheme = getAttributeValue(parser, "scheme");
							if(scheme!=null)
								intentFilter.addDataScheme(scheme);
							
							String host = getAttributeValue(parser, "host");
							String port = getAttributeValue(parser, "port");
			                if (host != null) 
			                	intentFilter.addDataAuthority(host, port);
			                
			                String path = getAttributeValue(parser, "pathPrefix") ;
			                String pathPrefix = getAttributeValue(parser, "path");
			                String pathPattern = getAttributeValue(parser, "pathPattern");
			                if (path!= null) 
			                	intentFilter.addDataPath(path, PatternMatcher.PATTERN_LITERAL);
			                else if(pathPattern!=null)
			                	intentFilter.addDataPath(pathPattern, PatternMatcher.PATTERN_SIMPLE_GLOB);
			                else if(pathPrefix!=null)
			                	intentFilter.addDataPath(pathPrefix, PatternMatcher.PATTERN_PREFIX);
						}
						break;
					case XmlPullParser.END_TAG:
						String endName = parser.getName();
						if("activity".equals(endName)){	
							if(activityToFilters.get(activityName)==null)
								activityToFilters.remove(activityName);
							activityName = null;
						}
						else if("service".equals(endName)){	
							if(serviceToFilters.get(serviceName)==null)
								serviceToFilters.remove(serviceName);
							serviceName = null;					
						}
						else if("receiver".equals(endName)){
							if(receiverToFilters.get(receiverName)==null)
								receiverToFilters.remove(receiverName);
							receiverName = null;					
						}
						else if("intent-filter".equals(endName)){							
							intentFilter = null;					
						}
						break;
					case XmlPullParser.TEXT:
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a full class name from a short class name by appending the
	 * globally-defined package when necessary
	 * @param className The class name to expand
	 * @return The expanded class name for the given short name
	 */
	private String expandClassName(String className) {
		if (className.startsWith("."))
			return this.packageName + className;
		else if (className.substring(0, 1).equals(className.substring(0, 1).toUpperCase()))
			return this.packageName + "." + className;
		else
			return className;
	}

	private String getAttributeValue(AXmlResourceParser parser, String attributeName) {
		for (int i = 0; i < parser.getAttributeCount(); i++)
			if (parser.getAttributeName(i).equals(attributeName))
				return parser.getAttributeValue(i);
//				return AXMLPrinter.getAttributeValue(parser, i);
		
		return null;
	}

	protected void loadClassesFromTextManifest(InputStream manifestIS) {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(manifestIS);
			
			Element rootElement = doc.getDocumentElement();
			this.packageName = rootElement.getAttribute("package");
			String versionCode = rootElement.getAttribute("android:versionCode");
			if (versionCode != null && versionCode.length() > 0)
				this.versionCode = Integer.valueOf(versionCode);
			this.versionName = rootElement.getAttribute("android:versionName");
			
			NodeList appsElement = rootElement.getElementsByTagName("application");
			if (appsElement.getLength() > 1)
				throw new RuntimeException("More than one application tag in manifest");
			for (int appIdx = 0; appIdx < appsElement.getLength(); appIdx++) {
				Element appElement = (Element) appsElement.item(appIdx);

				this.applicationName = appElement.getAttribute("android:name");
				if (this.applicationName != null && !this.applicationName.isEmpty())
					this.entryPointsClasses.add(expandClassName(this.applicationName));

				NodeList activities = appElement.getElementsByTagName("activity");
				NodeList receivers = appElement.getElementsByTagName("receiver");
				NodeList services  = appElement.getElementsByTagName("service");
				
				for (int i = 0; i < activities.getLength(); i++) {
					Element activity = (Element) activities.item(i);
					loadManifestEntry(activity, "android.app.Activity", this.packageName);
				}
				for (int i = 0; i < receivers.getLength(); i++) {
					Element receiver = (Element) receivers.item(i);
					loadManifestEntry(receiver, "android.content.BroadcastReceiver", this.packageName);
				}
				for (int i = 0; i < services.getLength(); i++) {
					Element service = (Element) services.item(i);
					loadManifestEntry(service, "android.app.Service", this.packageName);
				}
				
				NodeList permissions = appElement.getElementsByTagName("uses-permission");
				for (int i = 0; i < permissions.getLength(); i++) {
					Element permission = (Element) permissions.item(i);
					this.permissions.add(permission.getAttribute("android:name"));
				}

				NodeList usesSdkList = appElement.getElementsByTagName("uses-sdk");
				for (int i = 0; i < usesSdkList.getLength(); i++) {
					Element usesSdk = (Element) usesSdkList.item(i);
					String minVersion = usesSdk.getAttribute("android:minSdkVersion");
					if (minVersion != null && minVersion.length() > 0)
						this.minSdkVersion = Integer.valueOf(minVersion);
					String targetVersion = usesSdk.getAttribute("android:targetSdkVersion");
					if (targetVersion != null && targetVersion.length() > 0)
						this.targetSdkVersion = Integer.valueOf(targetVersion);
				}
			}			
		}
		catch (IOException ex) {
			System.err.println("Could not parse manifest: " + ex.getMessage());
			ex.printStackTrace();
		} catch (ParserConfigurationException ex) {
			System.err.println("Could not parse manifest: " + ex.getMessage());
			ex.printStackTrace();
		} catch (SAXException ex) {
			System.err.println("Could not parse manifest: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private void loadManifestEntry(Element activity, String baseClass, String packageName) {
		if (activity.getAttribute("android:enabled").equals("false"))
			return;
		
		String className = activity.getAttribute("android:name");		
		entryPointsClasses.add(expandClassName(className));
	}

    public void setApplicationName(String name) {
        this.applicationName = name;
    }

    public void setPackageName(String name) {
        this.packageName = name;
    }

	public HashSet<String> getEntryPointClasses() {
		return this.entryPointsClasses;
	}
	
	public Set<String> entryPointClone(){
		Set<String> set = new HashSet<String>();
		for(String s:entryPointsClasses){
			set.add(s);
		}
		return set;
	}
 	
	public String getApplicationName() {
		return this.applicationName;
	}
	
	public Set<String> getPermissions() {
		return this.permissions;
	}
	
	public int getVersionCode() {
		return this.versionCode;
	}
	
	public String getVersionName() {
		return this.versionName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public int getMinSdkVersion() {
		return this.minSdkVersion;
	}
	
	public int targetSdkVersion() {
		return this.targetSdkVersion;
	}
	
	public Map<String, List<AndroidIntentFilter>> getActivityToFilters() {
		return activityToFilters;
	}

	public Map<String, List<AndroidIntentFilter>> getReceiverToFilters() {
		return receiverToFilters;
	}

	public Map<String, List<AndroidIntentFilter>> getServiceToFilters() {
		return serviceToFilters;
	}

	public String getMainActivity(){
		return mainActivity;
	}

	@Override
	public void addToGlobal() {
		Global.v().setPkg(packageName);
		Global.v().setMainActivity(mainActivity);
		Global.v().setActivities(entryPointsClasses);
		Global.v().setActivityToFilters(activityToFilters);
		Global.v().setServiceToFilters(serviceToFilters);
		Global.v().setReceiverToFilters(receiverToFilters);
		
	}
	
	static class ArrayUtil {
		public static <E> List<E> toList(E t){
			List<E> list = new ArrayList<E>();
			list.add(t);
			return list;
		}
	}
	
}
