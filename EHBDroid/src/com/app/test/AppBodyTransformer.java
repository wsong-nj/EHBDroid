package com.app.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ehb.analysis.CallGraphBuilder;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.instrumentation.ActivityInstrumenter;
import ehb.instrumentation.BodyInstrumenter;
import ehb.instrumentation.ApplicationClassInstrumenter;
import ehb.instrumentation.FieldInstrumenter;
import ehb.instrumentation.codecoverage.CoverageBodyInstrumenter;
import ehb.instrumentation.codecoverage.CoverageClassInstrumenter;
import ehb.sign.SignCheckRemover;

/**
 * There are three events in Android: UIEvent, SystemEvent, InterAppEvent.
 * 
 * UIEvent consists of: XML, Stmt and Method. SystemEvent consists of: XML and
 * stmt InterAppEvent consists of: XML.
 * 
 * 1. In event collecting and dispatching phase, Stmt of UIEvent and SystemEvent
 * are dispatched to belonging activity, and will be stored in every activity.
 * While the the belonging activity of event that defined in XML or method can
 * be easily found, there is no need to dispath them.
 * 
 * 2. In event triggerrig phase: UIEvent and SystemEventInterAppEvent are
 * triggerred seperately. UIEvent is controlled by "Test";
 * SystemEventInterAppEvent are controlled by "SysTest";
 * 
 */
public class AppBodyTransformer extends BodyTransformer implements GlobalHost {

	public static Set<String> activities = (Set<String>) (((HashSet<String>) Global.v().getActivities()).clone());
	public static boolean classInstrumented = false;
	private String previousClassName = ""; // previous class name

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		if (!classInstrumented) {
			instrumentApplicationClasses();
			instrumentFieldForMainActivity();
			classInstrumented = true;
		}

		SootClass sc = b.getMethod().getDeclaringClass();
		String name = sc.getName();
		if (name.startsWith("android") || name.startsWith("java") || name.startsWith("com.facebook")||name.startsWith("org.eclipse"))
			return;
		if (isActivity(sc)) {
			instrumentActivity(sc);
		}
		instrumentBody(b);
		countNumbers(b);
		instrumentCoverage(b);

		new SignCheckRemover(b).removeSignCheckingStmt();
	}

	private void instrumentApplicationClasses() {
		new ApplicationClassInstrumenter(CallGraphBuilder.getApplicationClasses()).instrument();
	}
	
	private void instrumentFieldForMainActivity() {
		new FieldInstrumenter(Global.v().getMainActivityClass()).instrument();
	}

	private boolean isActivity(SootClass sc) {
		String name = sc.getName();
		if (activities.contains(name)) {//remove visited activity.
			activities.remove(name);
			return true;
		}
		return false;
	}

	private void instrumentActivity(SootClass sc) {
		new ActivityInstrumenter(sc).instrument();
	}
	
	private void instrumentBody(Body b) {
		new BodyInstrumenter(b).instrument();
	}
	
	private void instrumentCoverage(Body body) {
		new CoverageClassInstrumenter(body.getMethod().getDeclaringClass()).instrument();
		new CoverageBodyInstrumenter(body).instrument();
	}
	
	private void countNumbers(Body b) {
		SootMethod method = b.getMethod();
		SootClass sc = method.getDeclaringClass();
		boolean isValidMethod = CoverageClassInstrumenter.ValidMethodAnalysis.isValidMethod(method);
		if (isValidMethod) {
			PatchingChain<Unit> units = b.getUnits();
			Main.totalLine = Main.totalLine + units.size();
			Main.totalMethod++;
			Main.methods.add(b.getMethod().getSignature());
			if (sc.getName() != previousClassName) {
				previousClassName = sc.getName();
				Main.totalClass++;
			}
		}
	}

	@Override
	public void addToGlobal() {

	}
}
