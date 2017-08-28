package com.app.test.methodCoverage;

import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.internal.JimpleLocal;

import com.app.test.AppDir;
import com.app.test.Constants;

import ehb.instrumentation.IInstrumenter;

/**
 * instrument method coverage stmts to app
 * */
public class MethodCoverageStmtsInstrumenter implements IInstrumenter{
	Body body;
	public static SootClass previousClass = null;
	public static int methodIndex = 0;
	
	public MethodCoverageStmtsInstrumenter(Body body) {
		this.body = body;
	}
	
	public void instrumentStmts(){
		SootClass currentClass = body.getMethod().getDeclaringClass();
		if(!body.getMethod().getDeclaringClass().equals(previousClass)){
			previousClass = currentClass;
			methodIndex = 0;
		}
		else{
			methodIndex++;
		}
		instrumentPlusPlusStmt(methodIndex, body);
	}
	
	/**
	 * instrument methodCount++;
	 * <p>
	 * if(methodlist.get(index)==0){
	 * <ul>		methodlist.set(index,1);
	 * <p>	visitedMethodCount++; 
	 * <p>}
	 * 
	 * */
	public void instrumentPlusPlusStmt(int methodIndex, Body b){
		SootField methodCountListField = b.getMethod().getDeclaringClass().getFieldByName(Constants.EHBField.METHODCOUNTLIST);
		SootField globalValue = Scene.v().getSootClass(AppDir.class.getName()).getFieldByName("visitedMethodCount");
		PatchingChain<Unit> units = b.getUnits();
		Unit tail = units.getLast();
		
		Local local = Jimple.v().newLocal("invalid", Constants.object_Type);
		
		//fields must be translated into locals.
		Local methodCountListLocal = Jimple.v().newLocal("methodCount", Constants.linkedList_Type);
		Local visitedMethodsLocal = Jimple.v().newLocal("visitedCount", IntType.v());
		
		Local flagObj = Jimple.v().newLocal("flagObj", Constants.object_Type);
		Local flagInteger = Jimple.v().newLocal("flagInteger", Constants.integer_Type);// 0 represents nonvisited,1 represents visited 
		Local flagInt = Jimple.v().newLocal("flagInt", IntType.v());
		
		b.getLocals().add(local);
		
		b.getLocals().add(visitedMethodsLocal);
		b.getLocals().add(flagInt);
		b.getLocals().add(methodCountListLocal);
		b.getLocals().add(flagObj);
		b.getLocals().add(flagInteger);
		
		//1. if(methodCount.get(methodIndex).equals(0))
		//methodCountListLocal = MethodCountListField.makeRef();
		AssignStmt assignStmt = Jimple.v().newAssignStmt(local, Jimple.v().newNewExpr(Constants.object_Type));
		InvokeStmt is = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(local, Constants.objectInit_method.makeRef()));
		
		units.insertBefore(Jimple.v().newAssignStmt(methodCountListLocal, Jimple.v().newStaticFieldRef(methodCountListField.makeRef())),tail);
		//flagObj = methodCountListLocal.get(methodIndex);
		units.insertBefore(Jimple.v().newAssignStmt(flagObj,Jimple.v().newVirtualInvokeExpr(
				methodCountListLocal, Constants.linkedListGet_method.makeRef(),IntConstant.v(methodIndex))),tail);
		//(Integer)flagInteger = (Integer) flagObj;
		units.insertBefore(Jimple.v().newAssignStmt(flagInteger, Jimple.v().newCastExpr(flagObj, Constants.integer_Type)), tail);
		// int i = flagInteger.intValue();
		units.insertBefore(Jimple.v().newAssignStmt(flagInt, Jimple.v().newVirtualInvokeExpr(flagInteger, Constants.integerIntValue_method.makeRef())), tail);
		// if(i == 0) jump to bodyStmts 
		units.insertBefore(Jimple.v().newIfStmt(Jimple.v().newNeExpr(flagInt, IntConstant.v(0)), assignStmt),tail);
		
		//2. visitedMethodsLocal++;
		// visitedMethodsLocal = globalValue.makeRef();
		units.insertBefore(Jimple.v().newAssignStmt(visitedMethodsLocal,Jimple.v().newStaticFieldRef(globalValue.makeRef())),tail);
		// visitedMethodsLocal++;
		units.insertBefore(Jimple.v().newAssignStmt(visitedMethodsLocal, Jimple.v().newAddExpr(visitedMethodsLocal, IntConstant.v(1))),tail);
		// globalValue.makeRef() = visitedMethodsLocal;
		units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(globalValue.makeRef()), visitedMethodsLocal),tail);
		
		//3. methodlist.set(index,1)
		// flagInteger = Integer.valueOf(1);
		units.insertBefore(Jimple.v().newAssignStmt(flagInteger, Jimple.v().newStaticInvokeExpr(Constants.integerValueOf_method.makeRef(),IntConstant.v(1))), tail);
		List<Value> values = new ArrayList<Value>();
		values.add(IntConstant.v(methodIndex));
		values.add(flagInteger);
		// methodCountListLocal.setValue(methodIndex, 1);
		units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(methodCountListLocal, Constants.linkedListSet_method.makeRef(),values)),tail);
		units.insertBefore(assignStmt, tail);
		units.insertBefore(is, tail);
		try {
			body.validate();
		} catch (Exception e) {
			System.out.println("InValid body: "+b.getMethod());
		}
		
	}

	@Override
	public void instrument() {
		instrumentStmts();
	}
	
}
