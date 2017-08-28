package ehb.instrumentation.codecoverage;

import soot.SootClass;

public class CoverageGlobals {

	public static String lastClassName = "";
	public static int classIndex = 0;
	public static int methodIndex = 1;

	/**
	 * If sc is a new class, plus the classIndex and reset methodIndex,
	 * otherwise, plus the methodIndex.
	 * 
	 * @param sc
	 */
	public static void controlClassAndMethodIndex(SootClass sc) {
		if (isNewClass(sc)) {
			resetMethodIndex();
		} else
			addMethodIndex();
	}

	public static void addClassIndex() {
		classIndex++;
	}

	private static void addMethodIndex() {
		methodIndex++;
	}

	private static void resetMethodIndex() {
		methodIndex = 1;
	}

	private static boolean isNewClass(SootClass sc) {
		if (lastClassName == sc.getName()) {
			return false;
		} else {
			lastClassName = sc.getName();
			return true;
		}
	}
}
