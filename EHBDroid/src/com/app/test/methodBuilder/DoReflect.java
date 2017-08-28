package com.app.test.methodBuilder;

import java.util.List;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;

import com.app.test.Constants;

/**
 *  add doReflect(View, listener, registar) method.
 *  
 *  add view, listener, registar into view's belonging activity.
 *  <code>
 *  	Context con = view.getContext();
 *  	String name = con.getName();
 *  	Class class = Class.forname(name);
 *  
 *      Field viewLinkedList = class.getField("viewLinkedList");
 *      Field listenerLinkedList = class.getField("listenerLinkedList");
 *      Field VIEWREGISTARLINKEDLIST = class.getField("VIEWREGISTARLINKEDLIST");
 *      LinkedList o1 = viewLinkedList.get(null);
 *      o1.add(view);
 *      LinkedList o2 = listenerLinkedList.get(null);
 *      o2.add(object);
 *      LinkedList o3 = VIEWREGISTARLINKEDLIST.get(null); 
 *      o3.add(string);
 *  
 *  transfer to
 *  
 *  	Context con = view.getContext();
 *  	String name = con.getName();
 *  	Class class = Class.forname(name);
 *   	Field uievent = class.getField("uieventslist");
 *   	LinkedList list = viewLinkedList.get(null);
 *   	list.offer(uievent);
 *  </code>
 * */
public class DoReflect extends MethodBuilder{

	public static final String CLASSNAME = "doReflect";
	public static final String SUBSIGNATURE = "void doReflect(android.view.View,java.lang.Object,java.lang.String,boolean)";
	
	public DoReflect(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		
		Local param1,context,param2,param3,param4,booleanObj,exception,uieventJavaField,isvisited,uilinkedlist,
			flag,booleanLocal, obj, isJump, classLocal;
		
		{
//			currentActivity = addLocal("activity", sc_Type);
			param1 = addLocal("param1", view_Type);
			param2 = addLocal("param2", object_Type);
			param3 = addLocal("param3", string_Type);
			param4 = addLocal("param4", BooleanType.v());
			flag = addLocal("flag", BooleanType.v());
			uilinkedlist = addLocal("uilinkedlist", linkedList_Type);
			booleanObj = addLocal("object2", object_Type);
			exception = addLocal("exception", exception_Type);
			uieventJavaField =  addLocal("uieventfield", reflectField_Type);
			isvisited =  addLocal("isvisited", reflectField_Type);
			booleanLocal =  addLocal("booleanLocal", Boolean_Type);
			context =  addLocal("context", context_Type);
			obj = addLocal("obj", object_Type);
			isJump = addLocal("isJump", BooleanType.v());
			classLocal = addLocal("contextclass", class_Type);
		}
		
		Local uiEventLocal = addLocal("uiEventLocal", Constants.uiEvent_Type);
		
//		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
		addIdentityStmt(param1, new ParameterRef(view_Type,0));
		addIdentityStmt(param2, new ParameterRef(object_Type,1));
		addIdentityStmt(param3, new ParameterRef(string_Type,2));
		addIdentityStmt(param4, new ParameterRef(BooleanType.v(),3));
		//label0,
		AssignStmt label0 = Jimple.v().newAssignStmt(context, Jimple.v().newVirtualInvokeExpr(param1,view_getContext_method.makeRef()));
		//label1, 
		ReturnVoidStmt label1 = Jimple.v().newReturnVoidStmt();
		//label2, 
		IdentityStmt label2 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
	
		body.getUnits().add(label0);
		
		//Field uievent_field = class.getField("uieventslist");
		//Object obj = uievent_field.get(null); 
		//LinkedList list = (LinkedList) obj;
		
		addAssignStmt(classLocal, Jimple.v().newVirtualInvokeExpr(context, getClass_method.makeRef()));
		addAssignStmt(uieventJavaField,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v(Constants.EHBField.UIEVENTLINKEDLIST)));
		addAssignStmt(obj,Jimple.v().newVirtualInvokeExpr(uieventJavaField, fieldGet_method.makeRef(), NullConstant.v()));
		addAssignStmt(uilinkedlist, Jimple.v().newCastExpr(obj, linkedList_Type));
		
		//isVisited
		addAssignStmt(isvisited,Jimple.v().newVirtualInvokeExpr(classLocal,getField_method.makeRef(),StringConstant.v("isVisited")));
		addAssignStmt(booleanObj,Jimple.v().newVirtualInvokeExpr(isvisited,fieldGet_method.makeRef(),NullConstant.v()));
		addAssignStmt(booleanLocal,Jimple.v().newCastExpr(booleanObj,Boolean_Type));
		addAssignStmt(flag,Jimple.v().newVirtualInvokeExpr(booleanLocal,booleanValue_method.makeRef()));
		
		// if isJump is true, set 
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label1);
		addAssignStmt(uiEventLocal, Jimple.v().newNewExpr(Constants.uiEvent_Type));
		List<Value> paramValues = paramValues();
		paramValues.add(param1);
		paramValues.add(param2);
		paramValues.add(param3);
		paramValues.add(param4);
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(uiEventLocal, Constants.uiEventinit_method.makeRef(),paramValues));
		
		//×¢ÊÍ
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(uilinkedlist,offer_method.makeRef(),uiEventLocal));
		body.getUnits().add(label1);
		body.getUnits().add(label2);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLogException_method.makeRef(),exception));
		addGotoStmt(label1);
		
		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), label0, label1, label2);
		body.getTraps().add(trap);
	}

	@Override
	protected void newMethodName() {
		List<Type> emptyTypes = paramTypes();
		emptyTypes.add(view_Type);
		emptyTypes.add(object_Type);
		emptyTypes.add(string_Type);
		emptyTypes.add(BooleanType.v());
		currentMethod = new SootMethod(CLASSNAME, emptyTypes, VoidType.v(),Modifier.PUBLIC|Modifier.STATIC);
	}
}
