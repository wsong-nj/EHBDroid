package ehb.event;

import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

import com.app.test.Constants;
import com.app.test.event.SystemEventConstants;

import ehb.event.EventRecognizerForCode.AndroidEvent;
import ehb.global.Global;

public class SystemEventDispatcher extends EventDispatcher{
	
	public SystemEventDispatcher(InvokeStmt stmt, Body b, AndroidEvent ae) {
		super(stmt, b, ae);
	}

	@Override
	public void addDispathingStmts() {
		PatchingChain<Unit> units = body.getUnits();
		InvokeExpr invokeExpr = stmt.getInvokeExpr();
		String registar = invokeExpr.getMethod().getSubSignature();
		Value manager = invokeExpr.getUseBoxes().get(invokeExpr.getUseBoxes().size()-1).getValue();
		List<Value> values = new ArrayList<Value>();
		try {
			Local base = null;
			base = body.getThisLocal();
		} catch (Exception e) {
			System.err.println(stmt+" should not exist in static method");
		}
		if(AndroidEvent.ServiceEvent.equals(ae)){
			Local linkedList = addRandomLocal(body, "linkedList", Constants.linkedList_Type);
			Local systemEvent = addRandomLocal(body, "systemEvent", Constants.systemEvent_Type);
			Value listener = getListener(invokeExpr);
			values.add(manager);
			values.add(listener);
			values.add(StringConstant.v(registar));
			units.insertBefore(Jimple.v().newAssignStmt(
					systemEvent, Jimple.v().newNewExpr(Constants.systemEvent_Type)), stmt);
			units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(systemEvent, Constants.systemEventinit_method.makeRef(),values)), stmt);
			units.insertBefore(Jimple.v().newAssignStmt(
					linkedList, Jimple.v().newStaticFieldRef(Global.v().getmActivity().getFieldByName(Constants.EHBField.SYSTEMEVENTLINKEDLIST).makeRef())), stmt);
			units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList, Constants.offer_method.makeRef(),systemEvent)), stmt);
			body.validate();
		}
		else if(AndroidEvent.ReceiverEvent.equals(ae)){
			Local linkedList = addRandomLocal(body, "linkedList", Constants.linkedList_Type);
			Local receiverEvent = addRandomLocal(body, "receiverEvent", Constants.receiverEvent_Type);
			Value listener = invokeExpr.getArg(0);
			Value intentFilter = invokeExpr.getArg(1);
			values.add(manager);
			values.add(listener);
			values.add(StringConstant.v(registar));
			values.add(intentFilter);
			units.insertBefore(Jimple.v().newAssignStmt(
					receiverEvent, Jimple.v().newNewExpr(Constants.receiverEvent_Type)), stmt);
			units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(receiverEvent, Constants.receiverEventinit_method.makeRef(),values)), stmt);
			units.insertBefore(Jimple.v().newAssignStmt(
					linkedList, Jimple.v().newStaticFieldRef(Global.v().getmActivity().getFieldByName(Constants.EHBField.SYSTEMEVENTLINKEDLIST).makeRef())), stmt);
			units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(linkedList, Constants.offer_method.makeRef(),receiverEvent)), stmt);
//			body.validate();
		}
	}

	// use random to avoid of the same name
	private Local addRandomLocal(Body body, String name, Type t){
		int i = (int)(10000*Math.random());
		Local local = Jimple.v().newLocal(name+i, t);
		body.getLocals().add(local);
		return local;
	}
	
	private Value getListener(InvokeExpr invokeExpr){
		String subSignature = invokeExpr.getMethod().getSubSignature();
		if(SystemEventConstants.serviceType1List.contains(subSignature)){
			return invokeExpr.getArg(0);
		}
		else if(SystemEventConstants.serviceType2List.contains(subSignature)){
			return invokeExpr.getArg(1);
		}
		else if(SystemEventConstants.serviceType4List.contains(subSignature)){
			return invokeExpr.getArg(3);
		}
		throw new RuntimeException("Invalid InvokerExpr: "+invokeExpr);
	}
	
}
