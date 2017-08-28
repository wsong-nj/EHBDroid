package com.app.test.methodBuilder;

import java.util.ArrayList;
import java.util.List;

import ehb.builderfactory.StmtBuilder;
import soot.Body;
import soot.Local;
import soot.RefType;
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
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.util.Chain;

public abstract class MethodBuilder extends StmtBuilder implements IBuilder{

	public Chain<Unit> units;
	public SootClass sc;
	public String subSignature;
	public SootMethod currentMethod;
	public RefType sc_Type;
	public Body body;
	
	public MethodBuilder(SootClass sc, String subSignature){
		this.sc = sc;
		this.subSignature = subSignature;
		sc_Type = RefType.v(sc);
	}
	
	public void build(){
		newMethodName();
		newMethodBody();
		sc.addMethod(currentMethod);
	}
	
	protected void newMethodBody() {
		body = Jimple.v().newBody(currentMethod);
		body.getLocals();
		currentMethod.setActiveBody(body);
		units = body.getUnits();
		addUnits();
	}

	protected abstract void addUnits() ;

	protected abstract void newMethodName() ;

	public Local addLocal(String name,Type refType){
		return addLocal(body, name, refType);
	}
	
	public Local addLocalArray(String name,Type refType){
		return addLocalArray(body, name, refType);
	}

	protected IdentityStmt addIdentityStmt(Value local, Value identityRef){
		return addIdentityStmt(units,local, identityRef);
	}
	
	protected AssignStmt addAssignStmt(Value leftOp, Value rightOp){
		return addAssignStmt(units,leftOp, rightOp);
	}
	
	protected InvokeStmt addInvokeStmt(Value op){
		return addInvokeStmt(units,op);
	}
	
	protected ReturnVoidStmt addReturnVoidStmt() {
		return addReturnVoidStmt(units);
	}
	
	protected ReturnStmt addReturnTypeStmt(Value v) {
		return addReturnTypeStmt(units,v);
	}
	
	protected IfStmt addIfStmt(Expr expr,Unit target){
		return addIfStmt(units,expr,target);
	}
	
	protected GotoStmt addGotoStmt(Unit target){
		return addGotoStmt(units,target);
	}
	
	public List<Value> paramValues(){
		return new ArrayList<Value>();
	}
	
	public List<Type> paramTypes(){
		return new ArrayList<Type>();
	}
	
	public SootMethod getMethod() {
		return currentMethod;
	}
}
