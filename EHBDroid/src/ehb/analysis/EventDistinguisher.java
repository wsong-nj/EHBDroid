package ehb.analysis;

import java.util.List;

import soot.SootMethod;

public class EventDistinguisher {
	
	SootMethod method;

	public EventDistinguisher(SootMethod method) {
		this.method = method;
	}
	
	public boolean isActivityJumpingEvent(){
		List<SootMethod> successorMethods = TargetsAndSources.getAllTargetMethods(method);
		for(SootMethod m:successorMethods){
			if(m.getSubSignature().contains("startActivity")||m.getSubSignature().contains("startActivities")||
					m.getSubSignature().contains("startActivityForResult")){
//				if(method.getSignature().startsWith("<android")){
					return true;
//				}
			}
		}
		return false;
	}
	
}
