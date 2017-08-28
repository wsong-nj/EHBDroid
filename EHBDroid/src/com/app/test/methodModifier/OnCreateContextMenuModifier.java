package com.app.test.methodModifier;

import java.util.Iterator;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

import com.app.test.Constants;

public class OnCreateContextMenuModifier extends MethodModifier{

	public OnCreateContextMenuModifier(SootClass sc, SootMethod method) {
		super(sc, method);
	}

	@Override
	protected void modifyMethod() {
		final Body b = method.retrieveActiveBody();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				// add contextMenu = menu before ReturnStmt.
				@Override
				public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
					Local param = b.getParameterLocal(0);
					SootField contextMenuField = getContextMenuField(b);
					AssignStmt contextMenuAS = Jimple.v().newAssignStmt(
							Jimple.v().newStaticFieldRef(contextMenuField.makeRef()),param);
					units.insertBefore(contextMenuAS, u);
					b.validate();
				}
			});
		}
	}

	private SootField getContextMenuField(Body b) {
		SootClass declaringClass = b.getMethod().getDeclaringClass();
		String actmenu = Constants.EHBField.CONTEXTMENU;
		
		if(!declaringClass.declaresFieldByName(actmenu))
			throw new RuntimeException("Context menu field does not be declared");
		return declaringClass.getFieldByName(actmenu);
	}

}
