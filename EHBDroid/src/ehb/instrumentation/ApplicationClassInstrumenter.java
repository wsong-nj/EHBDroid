package ehb.instrumentation;

import soot.Scene;
import soot.SootClass;
import ehb.analysis.CallGraphBuilder;

/**
 * Application classes instrumenter.
 * */
public class ApplicationClassInstrumenter implements IInstrumenter {

	/**
	 * instrument application classes
	 * */
	@Override
	public void instrument() {
		addNewClasses();
	}

	private void addNewClasses() {
		for(String classAsApplication:CallGraphBuilder.getApplicationClasses()){
			if(!classAsApplication.startsWith("android")){
				SootClass sClass = Scene.v().getSootClass(classAsApplication);
				if(!Scene.v().getApplicationClasses().contains(sClass)){
					sClass.setApplicationClass();
				}
			}
		}
	}
}
