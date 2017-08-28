package ehb.analysis;

import soot.SootMethod;

public class MethodAnalysis implements IAnalysis{

	SootMethod sm;
	
	boolean isJumpMethod;
	
	public MethodAnalysis(SootMethod sm) {
		super();
		this.sm = sm;
	}

	@Override
	public void analyze() {
		isJumpMethod = new EventDistinguisher(sm).isActivityJumpingEvent();
	}

	public boolean isJumpMethod() {
		return isJumpMethod;
	}
	
}
