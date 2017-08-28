package com.app.test.methodModifier;

import java.util.ArrayList;
import java.util.List;

import ehb.builderfactory.StmtInsertor;
import soot.Body;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;

public abstract class MethodModifier extends StmtInsertor implements IModifier{
	
	SootClass sc;
	SootMethod method;
	Body body;
	Chain<Unit> units;
	
	public MethodModifier(SootClass currentClass, SootMethod currentMethod) {
		this.sc = currentClass;
		this.method = currentMethod;
		body = currentMethod.retrieveActiveBody();
		units = body.getUnits();
	}
	
	public void modify(){
		modifyMethod();
	}
	
	protected abstract void modifyMethod();

	protected Local addLocal(String name,Type refType){
		return addLocal(body, name, refType);
	}
	
	protected Local addLocalArray(String name,Type refType){
		return addLocalArray(body,name, refType);
	}
	
	protected List<Value> paramValues(){
		return new ArrayList<Value>();
	}
	
	protected List<Type> paramTypes(){
		return new ArrayList<Type>();
	}
	
	protected IdentityStmt insertIdentityStmt(Value local, Value identityRef,Unit u){
		return insertIdentityStmt(units, local, identityRef, u);
	}
	
	protected AssignStmt insertAssignStmt(Value leftOp, Value rightOp,Unit u){
		return insertAssignStmt(units, leftOp, rightOp, u);
	}
	
	protected InvokeStmt insertInvokeStmt(Value op,Unit u){
		return insertInvokeStmt(units, op, u);
	}
	
	protected ReturnVoidStmt insertReturnVoidStmt(Unit u) {
		return insertReturnVoidStmt(units, u);
	}
	
	protected ReturnStmt insertReturnTypeStmt(Value v,Unit u) {
		return insertReturnTypeStmt(units, v, u);
	}
	
	protected IfStmt insertIfStmt(Expr expr,Unit target,Unit u){
		return insertIfStmt(units, expr, target, u);
	}
	
	protected GotoStmt insertGotoStmt(Unit target,Unit u){
		return insertGotoStmt(units, target, u);
	}
	
	public SootMethod getMethod() {
		return method;
	}
	
}
