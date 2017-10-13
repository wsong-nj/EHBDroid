package ehb.instrumentation;

import soot.Scene;
import soot.SootClass;
import ehb.analysis.CallGraphBuilder;

import java.util.Set;

/**
 * Application classes instrumenter.
 * */
public class ApplicationClassInstrumenter implements IInstrumenter {

	private Set<String> applicationClasses;

	public ApplicationClassInstrumenter(Set<String> applicationClasses) {
		this.applicationClasses = applicationClasses;
	}

	/**
	 * instrument application classes
	 * */
	@Override
	public void instrument() {
		addNewClasses();
	}

	private void addNewClasses() {
		for (String applicationClass : applicationClasses) {
			if(applicationClass.startsWith("android"))
				continue;
			SootClass sootClass = Scene.v().getSootClass(applicationClass);
			if(!Scene.v().getApplicationClasses().contains(sootClass)){
				sootClass.setApplicationClass();
			}
		}
	}
}
