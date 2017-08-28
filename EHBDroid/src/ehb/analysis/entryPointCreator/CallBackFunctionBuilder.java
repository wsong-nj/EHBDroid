package ehb.analysis.entryPointCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CallBackFunctionBuilder {
	Map<String, List<String>> callbackFunctions = new HashMap<String, List<String>>();
	public void build(){
		buildCallBackFunctions();
	}
	protected abstract void buildCallBackFunctions();
	
	public Map<String, List<String>> getCallbackFunctions() {
		return callbackFunctions;
	}
}
