package com.app.test.methodBuilder;

import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.Jimple;

import com.app.test.Constants;

//simple clinit for any class, used by method coverage
public class SimpleClinit extends MethodBuilder{
	public static final String CLASSNAME = "<clinit>";
	public static final String SUBSIGNATURE = "void <clinit>()";
	public SimpleClinit(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local addLocal = addLocal( "methodCountListLocal", Constants.linkedList_Type);
		Local objectLocal = addLocal("linkedListobject",Constants.object_Type);
		SootField sField = Scene.v().getSootClass("com.app.test.AppDir").getFieldByName("linkedList");
		SootField methodCountListField = sc.getFieldByName(EHBField.METHODCOUNTLIST);
		addAssignStmt(addLocal, Jimple.v().newStaticFieldRef(sField.makeRef()));
		addAssignStmt(objectLocal,Jimple.v().newVirtualInvokeExpr(addLocal, Constants.linkedListClone_method.makeRef()));
		addAssignStmt(addLocal, Jimple.v().newCastExpr(objectLocal, Constants.linkedList_Type));
		addAssignStmt(Jimple.v().newStaticFieldRef(methodCountListField.makeRef()),addLocal);
		addReturnVoidStmt();
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.STATIC);
	}

}
