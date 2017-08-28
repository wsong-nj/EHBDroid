package ehb.event;

import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NullConstant;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

import com.app.test.AndroidConstants;
import com.app.test.CallBack;

public class EventRecognizerForStmt extends EventRecognizerForCode{
	
	InvokeStmt is;

	public EventRecognizerForStmt(InvokeStmt is) {
		super();
		this.is = is;
	}
	
	public AndroidEvent parseEvent(){
		
		InvokeExpr invokeExpr = is.getInvokeExpr();
		SootMethod methodOfStmt = invokeExpr.getMethod();
		String rmsubsig = methodOfStmt.getSubSignature();
		
		Value arg = invokeExpr.getMethod().getParameterCount()>0?invokeExpr.getArg(0):null;
		Value invoker = getInvoker(invokeExpr);
		
		if(!(invoker==null||arg==null||arg==NullConstant.v())){
			if(CallBack.getViewRegistars().contains(rmsubsig)){
				RefType type =(RefType) invoker.getType();
				//if invoker is subClass of android.view.View
				if(Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(type.getSootClass()).contains(AndroidConstants.AndroidViewClass)){
					return AndroidEvent.ViewEvent;
				}
			}
			
			else if(CallBack.getDialogRegistars().contains(rmsubsig)){
				RefType type =(RefType) invoker.getType();
				//if invoker is subClass of android.app.Dialog
				if(Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(type.getSootClass()).contains(AndroidConstants.AndroidDialogClass)){
					return AndroidEvent.DialogEvent;
				}
			}
			
			else if(rmsubsig.equals(CallBack.receiver_registerReceiver)){
				return AndroidEvent.ReceiverEvent;
			}
			
			else if(CallBack.getSerViceRegistars().contains(rmsubsig)){
				return AndroidEvent.ServiceEvent;
			}
		}
		return null;
	}

	private Value getInvoker(InvokeExpr invokeExpr) {
		Value invoker = null;
		if(invokeExpr instanceof JVirtualInvokeExpr){
			JVirtualInvokeExpr jvInvokeExpr = (JVirtualInvokeExpr)invokeExpr;
			invoker = jvInvokeExpr.getBase();
		}
		else if(invokeExpr instanceof JSpecialInvokeExpr){
			JSpecialInvokeExpr jsInvokeExpr = (JSpecialInvokeExpr)invokeExpr;
			invoker = jsInvokeExpr.getBase();
		}
		return invoker;
	}
}
