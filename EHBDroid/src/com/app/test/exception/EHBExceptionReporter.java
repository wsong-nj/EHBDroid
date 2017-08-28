package com.app.test.exception;

import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

public class EHBExceptionReporter {

	public static void report(Class c, SootClass sc, Body body, SootMethod sm, Unit unit, String string) {
		String msg = "Class: " + c + "Exception happens\n " + "\n SootClass: " + sc + "\n SootMethod: " + "\n Unit:"
				+ unit + "\n Body: " + body + "\n ExtraMsg: " + string;
		throw new RuntimeException(msg);
	}
}
