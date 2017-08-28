package ehb.xml.resource;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParserException;

import com.content.res.xmlprinter.AXmlResourceParser;

import ehb.xml.resource.ARSCFileParser.AbstractResource;
import ehb.xml.resource.ARSCFileParser.ResPackage;
import ehb.xml.resource.ARSCFileParser.ResType;

public class ProcessResource {

	private List<ResourceAttributes> resources = new ArrayList<ResourceAttributes>();
	
	public void loadResourceFile(String apk) {
		parseResourceLayout(apk);
	}
	
	/**
	 * @return write List<ResourceOutput> resources.
	 * */
	public void parseResourceLayout(final String apk){
		
		File apkFile = new File(apk);
		if(!apk.endsWith(".apk"))
			throw new RuntimeException("Input file is not a apk file.");
		
		try {
			ZipFile zipFile = new ZipFile(apkFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()){
				ZipEntry nextElement = entries.nextElement();
				final String layoutName = nextElement.getName();
				InputStream inputStream = zipFile.getInputStream(nextElement);
				final AXmlResourceParser parser = new AXmlResourceParser();
				if(layoutName.startsWith("res/layout")&&layoutName.endsWith(".xml")){
//				if(nextElement.getName().equals("AndroidManifest.xml")){
					parseXml(layoutName, inputStream, parser, new IElementHandle() {
						
						@Override
						public void handleElement() {
							String viewName = parser.getName();
							String callBack = "onClick";
							String callBackValue = parseAttributeValueString(parser, callBack);
							if(callBackValue!=null){
								String shortLayoutName = layoutName.substring(layoutName.lastIndexOf("/")+1, layoutName.indexOf(".xml"));
								int layoutId = parseARSC(shortLayoutName,apk);
								int viewId = parseAttributeValueData(parser, "id");
								resources.add(new ResourceAttributes(layoutName, layoutId, callBack, callBackValue, viewName,viewId));
							}
						}
					});
				}
			}
			zipFile.close();
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param resourceName
	 * @return resourceId. 
	 * */
	private int parseARSC(String resourceName, String apk){
		ARSCFileParser resParser = new ARSCFileParser();
		try {
			resParser.parse(apk);
			List<ResPackage> packages = resParser.getPackages();
			
			for(ResPackage rp:packages){
				List<ResType> declaredTypes = rp.getDeclaredTypes();
				for(ResType resType:declaredTypes){
					AbstractResource resource = resType.getResourceByName(resourceName);
					if(resource!=null){
						return resource.getResourceID();
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Cannot find resourceId");
	}
	
	private void parseXml(String layoutName, InputStream inputStream,
			AXmlResourceParser parser, IElementHandle em) throws XmlPullParserException,
			IOException {
		parser.open(inputStream);
		int next = 0;
		while((next = parser.next())!=AXmlResourceParser.END_DOCUMENT){
			switch (next) {
			case AXmlResourceParser.START_TAG:
				em.handleElement();
				break;
			case AXmlResourceParser.END_TAG:
				break;
			}
		}
	}
	
	private String parseAttributeValueString(AXmlResourceParser parser,String name){
		for(int i=0;i<parser.getAttributeCount();i++){
			String attributeName = parser.getAttributeName(i);
			if(name.equals(attributeName)){
				return parser.getAttributeValue(i);
			}
		}
		return null;
	}
	
	private int parseAttributeValueData(AXmlResourceParser parser,String name){
		for(int i=0;i<parser.getAttributeCount();i++){
			String attributeName = parser.getAttributeName(i);
			if(name.equals(attributeName)){
				return parser.getAttributeValueData(i);
			}
		}
		return -1;
	}
	
	public List<ResourceAttributes> getResources() {
		return resources;
	}
}
