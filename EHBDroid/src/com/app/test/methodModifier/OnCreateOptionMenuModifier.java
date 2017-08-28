package com.app.test.methodModifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;

import com.app.test.Constants;

public class OnCreateOptionMenuModifier extends MethodModifier{

	public OnCreateOptionMenuModifier(SootClass sc,
			SootMethod method) {
		super(sc, method);
	}

	@Override
	protected void modifyMethod() {
		final Body b = method.retrieveActiveBody();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				@Override			
				public void caseReturnStmt(ReturnStmt stmt) {
					Local base = b.getThisLocal();
					Local param = b.getParameterLocal(0);
					
					SootField menuField = getMenuField(b);
					
					AssignStmt activityMenuAS = Jimple.v().newAssignStmt(
							Jimple.v().newStaticFieldRef(menuField.makeRef()),param);
					units.insertBefore(activityMenuAS, u);
					
					List<Value> paramValues = new ArrayList<Value>();
					paramValues.add(base);
					paramValues.add(param);
					
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Constants.systemEventHandlerAddMenuItem_method.makeRef(),paramValues)), u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Constants.uiEventHandlerAddMenuItem_method.makeRef(),paramValues)), u);
					units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Constants.interAppEventHandlerAddMenuItem_method.makeRef(),paramValues)), u);
					b.validate();
				}
			});
		}
	}

	private SootField getMenuField(Body b) {
		SootClass declaringClass = b.getMethod().getDeclaringClass();
		String actmenu = Constants.EHBField.ACTIVITYMENU;
		
		if(!declaringClass.declaresFieldByName(actmenu))
			throw new RuntimeException("Activity menu field does not be declared");
		return declaringClass.getFieldByName(actmenu);
	}
	
}
