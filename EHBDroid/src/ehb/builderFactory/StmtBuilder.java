package ehb.builderfactory;

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

public class StmtBuilder extends LocalBuilder{
	
	protected IdentityStmt addIdentityStmt(Chain<Unit> units,Value local, Value identityRef){
		IdentityStmt stmt = Jimple.v().newIdentityStmt(local, identityRef);
		units.add(stmt);
		return stmt;
	}
	
	protected AssignStmt addAssignStmt(Chain<Unit> units,Value leftOp, Value rightOp){
		AssignStmt stmt = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.add(stmt);
		return stmt;
	}
	
	protected InvokeStmt addInvokeStmt(Chain<Unit> units,Value op){
		InvokeStmt stmt = Jimple.v().newInvokeStmt(op);
		units.add(stmt);
		return stmt;
	}
	
	protected ReturnVoidStmt addReturnVoidStmt(Chain<Unit> units) {
		ReturnVoidStmt stmt = Jimple.v().newReturnVoidStmt();
		units.add(stmt);
		return stmt;
	}
	
	protected ReturnStmt addReturnTypeStmt(Chain<Unit> units,Value v) {
		ReturnStmt stmt =Jimple.v().newReturnStmt(v);
		units.add(stmt);
		return stmt;
	}
	
	protected IfStmt addIfStmt(Chain<Unit> units,Expr expr,Unit target){
		IfStmt ifStmt = Jimple.v().newIfStmt(expr, target);
		units.add(ifStmt);
		return ifStmt;
	}
	
	protected GotoStmt addGotoStmt(Chain<Unit> units,Unit target){
		GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
		units.add(gotoStmt);
		return gotoStmt;
	}
}
