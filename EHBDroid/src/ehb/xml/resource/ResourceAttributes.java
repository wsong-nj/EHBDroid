package ehb.xml.resource;

import java.io.Serializable;

/**
 * An UI element Info contains six elements = {UIName, UIId, callback, callbackName, UI's belonging layoutName, layoutId},
 * like {"com.adobe.reader.framework.ui.FWFloatingActionButton","2131231039","onClick","getFileChooser","layout/split_pane.xml","0x7f03004f"};
 * */
public class ResourceAttributes implements Serializable{

	String layoutName;
	int layoutId;
	String callBack;
	String callBackValue;
	String UIName;
	int UIid;
	
	public ResourceAttributes(String layoutName, int layoutId, String callBack,
			String callBackValue, String elementName,int UIid) {
		super();
		this.layoutName = layoutName;
		this.layoutId = layoutId;
		this.callBack = callBack;
		this.callBackValue = callBackValue;
		this.UIName = elementName;
		this.UIid = UIid;
	}
	
	public String getLayoutName() {
		return layoutName;
	}
	public int getLayoutId() {
		return layoutId;
	}
	public String getCallBack() {
		return callBack;
	}
	public String getCallBackValue() {
		return callBackValue;
	}
	public String getElementName() {
		return UIName;
	}

	public int getUIid() {
		return UIid;
	}

	@Override
	public String toString() {
		return "ResourceOutput [layoutName=" + layoutName + ", layoutId="
				+ layoutId + ", callBack=" + callBack + ", callBackValue="
				+ callBackValue + ", elementName=" + UIName + ", UIid="
				+ UIid + "]";
	}


}
