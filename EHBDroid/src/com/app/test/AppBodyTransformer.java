package com.app.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.util.Chain;

import com.app.test.methodCoverage.MethodCoverageFieldInstrumenter;
import com.app.test.methodCoverage.MethodCoverageStmtsInstrumenter;

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
 * 2. In event triggerrig phase: UIEvent and SystemEvent¡¢InterAppEvent are
 * triggerred seperately. UIEvent is controlled by "Test";
 * SystemEvent¡¢InterAppEvent are controlled by "SysTest";
 * 
 */
public class AppBodyTransformer extends BodyTransformer implements GlobalHost {

	public static Set<String> activities = (Set<String>) (((HashSet<String>) Global.v().getActivities()).clone());
	public static boolean classIntrumented = false;

	private String lastClass = ""; // recording the last class name
	private String mainActivity = Global.v().getMainActivity();

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		SootClass sc = b.getMethod().getDeclaringClass();
		String name = sc.getName();

		prepare();

		if (name.startsWith("android") || name.startsWith("java") || name.startsWith("com.facebook")||name.startsWith("org.eclipse"))
			return;

		if (isActivity(sc)) {
			instrumentActivity(sc);
		}
		instrumentBody(b);
		instrumentCoverage(b);
		countNumbers(b);

		new SignCheckRemover(b).removeSignCheckingStmt();
	}

	/**
	 * prepare to instrument classes and fields
	 */
	private void prepare() {
		if (!classIntrumented) {
			instrumentApplicationClasses();
			instrumentFieldForMainActivity();
			classIntrumented = true;
		}
	}

	private void instrumentApplicationClasses() {
		new ApplicationClassInstrumenter().instrument();
	}
	
	private void instrumentFieldForMainActivity() {
		new FieldInstrumenter(Global.v().getmActivity()).instrument();
	}

	private boolean isActivity(SootClass sc) {
		String name = sc.getName();
		if (activities.contains(name)) {//remove visited activity.
			activities.remove(sc.getName());
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
			if (sc.getName() != lastClass) {
				lastClass = sc.getName();
				Main.totalClass++;
			}
		}
	}

	@Override
	public void addToGlobal() {

	}
}
