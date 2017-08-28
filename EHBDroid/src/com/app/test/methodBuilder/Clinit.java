package com.app.test.methodBuilder;

import com.app.test.Constants;

import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.internal.JNewExpr;
import ehb.global.Global;

/**
 * add <clinit> for Activity
 * add clinit() method.
 * {@code
 * viewLinkedList = new LinkedList<View>();
 * viewListenerLinkedList = new LinkedList<Object>();
 * viewRegistarLinkedList = new LinkedList<String>();
 * dialogLinkedList = new LinkedList<Dialog>();
 * dialogListenerLinkedList = new LinkedList<Object>();
 * dialogRegistarLinkedList = new LinkedList<String>(); 
 * isVisited = false;
 * }
 * */
public class Clinit extends MethodBuilder{
	public static final String CLASSNAME = "<clinit>";
	public static final String SUBSIGNATURE = "void <clinit>()";
	SootField viewLinkedList, listenerLinkedList,activities,isVisited,systemEventLinkedList;
	SootClass mainActivity;
	Local linkedList;
	boolean isMainActivity = false;
	public Clinit(SootClass sc, String subSignature, boolean isMainActivity) {
		super(sc, subSignature);
		this.isMainActivity = isMainActivity;
	}

	@Override
	protected void addUnits() {
		//create a linkedList
		linkedList = addLocal("linkedList", linkedList_Type);
		
		// get mainActivity fields
		mainActivity = Scene.v().getSootClass(Global.v().getMainActivity());
		//start to instrument stmts.
		for(String fieldName:Constants.EHBField.eventsLinkedList){
			SootField field = sc.getFieldByName(fieldName);
			addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(field.makeRef()), linkedList);
		}
		
		for(String fieldName:Constants.EHBField.visited){
			addAssignStmt(Jimple.v().newStaticFieldRef(sc.getFieldByName(fieldName).makeRef()),IntConstant.v(0));
		}
		
		addQueue(isMainActivity);
		
		addReturnVoidStmt();
	}

	private void addQueue(boolean main) {
		if(main){
			systemEventLinkedList = sc.getFieldByName(Constants.EHBField.SYSTEMEVENTLINKEDLIST);
			addAssignStmt(linkedList, new JNewExpr(linkedList_Type));
			addInvokeStmt(Jimple.v().newSpecialInvokeExpr(linkedList, linkedListInit_method.makeRef()));
			addAssignStmt(Jimple.v().newStaticFieldRef(systemEventLinkedList.makeRef()), linkedList);
		}
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.STATIC);
	}
}
