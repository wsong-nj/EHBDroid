package ehb.instrumentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.app.test.CallBack;
import com.app.test.Main;
import com.app.test.methodBuilder.DoReflect;

import ehb.event.EventRecognizerForCode.AndroidEvent;
import ehb.event.EventRecognizerForStmt;
import ehb.event.SystemEventDispatcher;
import ehb.event.UIEventDispatcher;
import ehb.global.Global;
import ehb.sign.SignCheckRemover;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JVirtualInvokeExpr;

public class BodyInstrumenter implements IInstrumenter{
	
	Body b;
	
	public BodyInstrumenter(Body body) {
		this.b = body;
	}
	
	public void instrument(){
		handleXML(b, Global.v().getIdToCallBack());
		handleBody(b);
		
		removeFreeNovelSignCheckingStmts(b);
		removeMBankSignCheckingStmts(b);
	}
	
	private void removeMBankSignCheckingStmts(Body b) {
		new SignCheckRemover(b).removeSignCheckingMBank();
	}
	
	private void removeFreeNovelSignCheckingStmts(Body b) {
		new SignCheckRemover(b).removeSignCheckingFreeNovel();
	}

	//add doRefect() after findViewById();
	private void handleXML(final Body b, final Map<Integer, String> idToCallBack) {
		final SootClass sc = b.getMethod().getDeclaringClass();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				public void caseAssignStmt(AssignStmt stmt) {
					Value rightValue = stmt.getRightOp();
					if(rightValue instanceof JVirtualInvokeExpr){
						InvokeExpr invokeExpr = (InvokeExpr)rightValue;
						String subSignature = invokeExpr.getMethod().getSubSignature();
						if("android.view.View findViewById(int)".equals(subSignature)&&!(invokeExpr.getArg(0) instanceof Local)){
							Value v = invokeExpr.getArg(0);
							IntConstant id = (IntConstant)v;
							if(idToCallBack.containsKey(id.value)){
								String callback = idToCallBack.get(id.value);
								String subSig = "void "+callback+"(android.view.View)";
								if(sc.declaresMethod(subSig)){
									logXMLInfo(sc, b.getMethod(), stmt);
									//add doReflect
									if(!sc.declaresMethod(DoReflect.SUBSIGNATURE)){
										DoReflect doReflect = new DoReflect(sc, DoReflect.SUBSIGNATURE);
										doReflect.build();
									}
									
									SootMethod doReflect = sc.getMethod(DoReflect.SUBSIGNATURE);
									Map<String,List<String>> viewToCallBacks = CallBack.viewToCallBacks;
									String viewNameAndId = sc.getName()+id;
									viewToCallBacks.put(viewNameAndId, Arrays.asList(subSig));
									
									//insert unit.
									{
										Local base = b.getThisLocal();
										Local view = (Local) stmt.getLeftOp();
										List<Value> params = new ArrayList<Value>();
										params.add(view);
										params.add(base);
										params.add(StringConstant.v(viewNameAndId));
										units.insertAfter(
												Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doReflect.makeRef(),params)), u);
									}
								}
							}
						}
					}
				}
			});
		}
	}

	private void handleBody(final Body b) {
		final SootMethod sm = b.getMethod();
		final SootClass sc = sm.getDeclaringClass();
		final PatchingChain<Unit> units = b.getUnits();
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();
			u.apply(new AbstractStmtSwitch() {
				public void caseInvokeStmt(InvokeStmt stmt) {
//					removeFreeNovelSignCheckingStmts(b);
					EventRecognizerForStmt recognizer = new EventRecognizerForStmt(stmt);
					AndroidEvent event = recognizer.parseEvent();
					if(event==null)
						return;
					switch (event) {
						case ViewEvent:
							//insert doReflect() after unit.
							new UIEventDispatcher(stmt, b,event).dispatchEvent();
							break;
						case DialogEvent:
							//insert doDialogReflect() after unit.
							new UIEventDispatcher(stmt, b,event).dispatchEvent();
							break;
						case ServiceEvent:
							//insert MainActivity.SYSTEMEVENTLINKEDLIST.offer(new ServiceEvent(1,2,3)) after unit.
							new SystemEventDispatcher(stmt, b,event).dispatchEvent();
							break;
						case ReceiverEvent:
							//insert MainActivity.SYSTEMEVENTLINKEDLIST.offer(new ReceiverEvent(1,2,3,4)) after unit.
							new SystemEventDispatcher(stmt, b,event).dispatchEvent();
							break;
						default:
							break;
					}
					logBodyInfo(sc, sm, stmt);
				}
			});
		}
	}

//	private void removeMBankSignCheckingStmts(final Body b) {
//		
//		SootMethod method = b.getMethod();
//		String signature = method.getSignature();
//		if(signature.equals("<cn.com.cmbc.mbank.MainActivity: void onCreate(android.os.Bundle)>")){
//			PatchingChain<Unit> units = b.getUnits();
//			for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
//				final Unit u = iter.next();
//				u.apply(new AbstractStmtSwitch() {
//					public void caseAssignStmt(AssignStmt stmt) {
//						Value rightOp = stmt.getRightOp();
//						
//						if(rightOp instanceof StaticInvokeExpr){
//							StaticInvokeExpr sie = (StaticInvokeExpr)rightOp;
//							String sig = sie.getMethod().getSignature();
//							//String str3 = cn.com.cmbc.mbank.monitor.d.a(getApplicationContext());
//							if(sig.equals("<cn.com.cmbc.mbank.monitor.d: java.lang.String a(android.content.Context)>")){
//								ValueBox rightOpBox = stmt.getRightOpBox();
//								rightOpBox.setValue(StringConstant.v("e6a81c9a3c88040678ae615f063d14d0"));
//								System.out.println("Sign has been removed! "+ stmt);
//							}
//						}
//					}
//				});
//			}
//		}
//		
////		boolean isSignChecking = signChecking(stmt);
////		if(isSignChecking){
////			units.remove(stmt);
////			System.out.println("SignCheckingStmts have been removed!!!");
////		}
////		b.validate();
//	}
	
	private void removeFreeNovelSignCheckingStmts(final Body b,
			final PatchingChain<Unit> units, InvokeStmt stmt) {
		SootMethod method = stmt.getInvokeExpr().getMethod();
		if(Main.signCheckStmts.contains(method.getSubSignature())){
			units.remove(stmt);
			System.out.println("SignCheckingStmts have been removed!!!");
		}
	}
	
//	private boolean signChecking(InvokeStmt is){
//		SootMethod method = is.getInvokeExpr().getMethod();
//		if(Main.signCheckStmts.contains(method.getSubSignature()))
//			return true;
//		return false;
//	}
//	private void handleMethod(SootMethod sm) {
//		EventRecognizerForMethod eventRecognizerForMethod = new EventRecognizerForMethod(sm);
//		AndroidEvent event = eventRecognizerForMethod.parseEvent();
//		if(AndroidEvent.MenuEvent.equals(event)){
//			System.out.println("ActivityMenuEvent: "+sm.getSignature());
//		}
//		else if(AndroidEvent.ViewEvent.equals(event)){
//			System.out.println("ListActivity: "+sm.getName());
//		}
//	} 
	
	private void logXMLInfo(SootClass sc, SootMethod sm, Stmt stmt){
		logInfo(sc, sm, stmt, "XML");
	}
	
	private void logBodyInfo(SootClass sc, SootMethod sm, Stmt stmt){
		logInfo(sc, sm, stmt, "Body");
	}
	
	private void logInfo(SootClass sc, SootMethod sm, Stmt stmt, String s){
		String msg = s+". SootClass: "+sc.getName()+", SootMethod: "+b.getMethod().getSignature()+
				", Stmt: "+stmt;
		Global.v().getEHBEventSet().add(msg);
	}
	
}
