package ehb.sign;

import java.util.Iterator;

import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

/**
 * Bofore real-world apps start, they will first check its sign to make sure the
 * app has not been repackaged. Thus, to make sure the instrumented app can run
 * normally, we need to remove such sigh checking statements.
 * 
 * To remove to the sign checking stmts, you need first to go through the source
 * code of the tested app, find the right position of sign checking stmts, and
 * remove it. The source code can be retrieved by decompiling.
 */
public class SignCheckRemover {

	Body b;
	PatchingChain<Unit> units;

	public SignCheckRemover(Body b) {
		super();
		this.b = b;
		units = b.getUnits();
	}

	public void removeSignCheckingStmt(){
		removeSignCheckingBaiduwaimai();
		removeSignCheckingFreeNovel();
		removeSignCheckingMBank();
	}
	
	// "<cn.com.cmbc.mbank.MainActivity: void onCreate(android.os.Bundle)>"
	public void removeSignChecking(String methodOfSignStmt, final IStmtRemover remover) {
		SootMethod method = b.getMethod();
		String signature = method.getSignature();
		if (signature.equals(methodOfSignStmt)) {
			for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
				final Unit u = iter.next();
				remover.remove(u);
			}
		}
	}

	public void removeSignCheckingMBank() {
		String bodySignature = "<cn.com.cmbc.mbank.MainActivity: void onCreate(android.os.Bundle)>";
		final String methodSignature = "<cn.com.cmbc.mbank.monitor.d: java.lang.String a(android.content.Context)>";
		final String changeValue = "e6a81c9a3c88040678ae615f063d14d0";

		removeSignChecking(bodySignature, new IStmtRemover() {
			@Override
			public void remove(Unit u) {
				u.apply(new AbstractStmtSwitch() {
					public void caseAssignStmt(AssignStmt stmt) {
						Value rightOp = stmt.getRightOp();
						if (rightOp instanceof StaticInvokeExpr) {
							StaticInvokeExpr sie = (StaticInvokeExpr) rightOp;
							String sig = sie.getMethod().getSignature();
							// String str3 =
							// cn.com.cmbc.mbank.monitor.d.a(getApplicationContext());
							if (sig.equals(methodSignature)) {
								ValueBox rightOpBox = stmt.getRightOpBox();
								rightOpBox.setValue(StringConstant.v(changeValue));
								log(stmt);
							}
						}
					}
				});
			}
		});
	}

	public void removeSignCheckingFreeNovel() {

		String bodySignature = "<com.dzbook.activity.MainActivity: void onCreate(android.os.Bundle)>";
		final String methodSignature = "void checkSignLegal(android.content.Context)";

		removeSignChecking(bodySignature, new IStmtRemover() {
			@Override
			public void remove(Unit u) {
				u.apply(new AbstractStmtSwitch() {
					public void caseInvokeStmt(InvokeStmt stmt) {
						SootMethod method = stmt.getInvokeExpr().getMethod();
						if (methodSignature.equals(method.getSubSignature())) {
							units.remove(stmt);
							log(stmt);
						}
					}
				});
			}
		});
	}

	public void removeSignCheckingBaiduwaimai() {

		String bodySignature = "<com.baidu.lbs.waimai.BaseFragmentActivity: void onResume()>";
		final String methodSignature = "<com.baidu.mobstat.StatService: void onResume(android.content.Context)>";
			
		String onPauseBody = "<com.baidu.lbs.waimai.BaseFragmentActivity: void onPause()>";
		final String onPauseMethod = "<com.baidu.mobstat.StatService: void onPause(android.content.Context)>";
		
		removeSignChecking(bodySignature, new IStmtRemover() {
			@Override
			public void remove(Unit u) {
				u.apply(new AbstractStmtSwitch() {
					public void caseInvokeStmt(InvokeStmt stmt) {
						SootMethod method = stmt.getInvokeExpr().getMethod();
						if (methodSignature.equals(method.getSignature())) {
							units.remove(stmt);
							log(stmt);
						}
					}
				});
			}
		});
		
		removeSignChecking(onPauseBody, new IStmtRemover() {
			@Override
			public void remove(Unit u) {
				u.apply(new AbstractStmtSwitch() {
					public void caseInvokeStmt(InvokeStmt stmt) {
						SootMethod method = stmt.getInvokeExpr().getMethod();
						if (onPauseMethod.equals(method.getSignature())) {
							units.remove(stmt);
							log(stmt);
						}
					}
				});
			}
		});
	}

	public void log(Stmt stmt) {
		System.out.println("Sign has been removed! " + stmt);
	}
}
