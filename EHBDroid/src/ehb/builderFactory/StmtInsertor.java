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

public class StmtInsertor extends LocalBuilder{
	
	protected IdentityStmt insertIdentityStmt(Chain<Unit> units, Value local, Value identityRef,Unit u){
		IdentityStmt stmt = Jimple.v().newIdentityStmt(local, identityRef);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	protected AssignStmt insertAssignStmt(Chain<Unit> units, Value leftOp, Value rightOp,Unit u){
		AssignStmt stmt = Jimple.v().newAssignStmt(leftOp,rightOp);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	protected InvokeStmt insertInvokeStmt(Chain<Unit> units, Value op,Unit u){
		InvokeStmt stmt = Jimple.v().newInvokeStmt(op);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	protected ReturnVoidStmt insertReturnVoidStmt(Chain<Unit> units, Unit u) {
		ReturnVoidStmt stmt = Jimple.v().newReturnVoidStmt();
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	protected ReturnStmt insertReturnTypeStmt(Chain<Unit> units, Value v,Unit u) {
		ReturnStmt stmt =Jimple.v().newReturnStmt(v);
		units.insertBefore(stmt, u);
		return stmt;
	}
	
	protected IfStmt insertIfStmt(Chain<Unit> units, Expr expr,Unit target,Unit u){
		IfStmt ifStmt = Jimple.v().newIfStmt(expr, target);
		units.insertBefore(ifStmt, u);
		return ifStmt;
	}
	
	protected GotoStmt insertGotoStmt(Chain<Unit> units, Unit target,Unit u){
		GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
		units.insertBefore(gotoStmt, u);
		return gotoStmt;
	}
}
