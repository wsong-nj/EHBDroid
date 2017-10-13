package ehb.xml.manifest;

import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.xml.resource.ResourceAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallBackGenerator implements GlobalHost{

	private List<ResourceAttributes> resources;
	private String file;
	private Map<Integer,String> idToCallBack = new HashMap<>();

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
