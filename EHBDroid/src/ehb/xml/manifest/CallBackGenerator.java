package ehb.xml.manifest;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.xml.resource.ResourceAttributes;

public class CallBackGenerator implements GlobalHost{

	private List<ResourceAttributes> resources;
	private String file;
	private Map<Integer,String> idToCallBack = new HashMap<>();
	
	/**
	 * read resources from file
	 * */
	public CallBackGenerator(String file){
		this.file = file;
		resources = readFile(file);
	}

	public CallBackGenerator(List<ResourceAttributes> resources) {
		this.resources = resources;
	}
	
	/**
	 * Generate idToCallBack, an map = {viewid, callback} 
	 * */
	public void generate() {
		genIdToCallBack();
	}

	private void genIdToCallBack() {
		idToCallBack = new HashMap<Integer, String>();
		for(ResourceAttributes rOutput:resources){
			int uIid = rOutput.getUIid();
			String callBack = rOutput.getCallBackValue();
			idToCallBack.put(uIid, callBack);
		}
	}
	
	private List<ResourceAttributes> readFile(String inputFile){
		List<ResourceAttributes> result = new ArrayList<ResourceAttributes>();
		FileInputStream fis;
		try {
			fis = new FileInputStream(inputFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (List<ResourceAttributes>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<ResourceAttributes> getResources() {
		return resources;
	}

	public String getFile() {
		return file;
	}

	public Map<Integer, String> getIdToCallBack() {
		return idToCallBack;
	}

	@Override
	public void addToGlobal() {
		Global.v().getIdToCallBack().putAll(idToCallBack);
	}
	
	
}
