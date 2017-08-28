package com.app.test.methodCoverage;

import java.util.List;

import soot.Body;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Jimple;

import com.app.test.AppDir;
import com.app.test.Constants;
import com.app.test.methodBuilder.SimpleClinit;

import ehb.builderfactory.LocalBuilder;
import ehb.global.Global;
import ehb.instrumentation.IInstrumenter;

/**
 * 统计方法覆盖率
 * */
public class MethodCoverageFieldInstrumenter extends LocalBuilder implements IInstrumenter{
	
	SootClass sc;
	
	public MethodCoverageFieldInstrumenter(SootClass sc) {
		this.sc = sc;
	}

	@Override
	public void instrument() {
		addMethodListForClass();
	}
	
	/**
	 * add methodlist to every class, and initilize it in clinit method. 
	 * <p>If sc contains clinit, we insert statement:
	 * <ul>LinkedList methodCountListLocal = AppDir.linkedList.clone();</ul>
	 * <p>else create a clinit method and insert the above statement. 
	 * */
	private void addMethodListForClass() {
		List<SootClass> visitedClasses = Global.v().getVisitedClasses();
		SootField methodCountList = new SootField(Constants.EHBField.METHODCOUNTLIST, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
		if(!sc.declaresFieldByName(Constants.EHBField.METHODCOUNTLIST)){
			sc.addField(methodCountList);
		}
		SootField sootField = sc.getFieldByName(Constants.EHBField.METHODCOUNTLIST);
		if(sc.declaresMethodByName("<clinit>")){
			SootMethod clinitMethod = sc.getMethodByName("<clinit>");
			Body b = clinitMethod.retrieveActiveBody();
			PatchingChain<Unit> units = clinitMethod.getActiveBody().getUnits();
			if(!visitedClasses.contains(sc)){
				visitedClasses.add(sc);
				Unit last = units.getFirst();
				Local methodListLocal = addLocal(b, "methodCountListLocal", Constants.linkedList_Type);
				Local objectLocal = addLocal(b,"linkedListobject",Constants.object_Type);
				SootField initlinkedList = Scene.v().getSootClass(AppDir.class.getName()).getFieldByName("linkedList");
				units.insertBefore(Jimple.v().newAssignStmt(methodListLocal, Jimple.v().newStaticFieldRef(initlinkedList.makeRef())),last);
				units.insertBefore(Jimple.v().newAssignStmt(objectLocal,Jimple.v().newVirtualInvokeExpr(methodListLocal, Constants.linkedListClone_method.makeRef())),last);
				units.insertBefore(Jimple.v().newAssignStmt(methodListLocal, Jimple.v().newCastExpr(objectLocal, Constants.linkedList_Type)),last);
				units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sootField.makeRef()),methodListLocal),last);
				b.validate();
			}
		}
		else{
			addSimpleClinitForClass();
			visitedClasses.add(sc);
		}
	}

	private void addSimpleClinitForClass() {
		new SimpleClinit(sc, SimpleClinit.SUBSIGNATURE).build();
	}

}
