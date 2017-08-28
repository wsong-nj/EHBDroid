package com.app.test.methodModifier;

import java.util.Iterator;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.internal.JNewExpr;

import com.app.test.Constants;

import ehb.global.Global;

public class ClinitForActivityModifier extends MethodModifier{
	
	public ClinitForActivityModifier(SootClass currentClass,
			SootMethod currentMethod) {
		super(currentClass, currentMethod);
	}

	@Override
	protected void modifyMethod() {
		final Body b = method.retrieveActiveBody();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				@Override
				public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
					
					Local linkedList = Jimple.v().newLocal("linkedList",Constants.linkedList_Type);
					b.getLocals().add(linkedList);
					
					for(String fieldName:Constants.EHBField.eventsLinkedList){
						SootField field = sc.getFieldByName(fieldName);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
						units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(field.makeRef()), linkedList),u);
					}
					
					for(String fieldName:Constants.EHBField.visited){
						SootField field = sc.getFieldByName(fieldName);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(field.makeRef()),IntConstant.v(0)),u);
					}
					
					if(sc.equals(Global.v().getmActivity())){
						SootField systemEventLinkedList = sc.getFieldByName(Constants.EHBField.SYSTEMEVENTLINKEDLIST);
						units.insertBefore(Jimple.v().newAssignStmt(linkedList, new JNewExpr(Constants.linkedList_Type)),u);
						units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, Constants.linkedListInit_method.makeRef())),u);
						units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(systemEventLinkedList.makeRef()), linkedList),u);
					}
					b.validate();
				}
			});
		}
	}
}
