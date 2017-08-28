package ehb.event;

//94ÐÐ×¢ÊÍµôbody.validate();
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;

import com.app.test.CallBack;
import com.app.test.methodBuilder.DoDialogReflect;
import com.app.test.methodBuilder.DoReflect;

import ehb.analysis.LocalAnalysis;
import ehb.analysis.MethodAnalysis;
import ehb.event.EventRecognizerForCode.AndroidEvent;
import ehb.global.EHBOptions;

public class UIEventDispatcher extends EventDispatcher{
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	boolean isJumpingEvent;
	
	public UIEventDispatcher(InvokeStmt stmt, Body b, AndroidEvent ae) {
		super(stmt, b, ae);
	}

	@Override
	protected void addDispathingStmts() {
		InvokeExpr invokeExpr = stmt.getInvokeExpr();
		PatchingChain<Unit> units = body.getUnits();
		SootClass sc = body.getMethod().getDeclaringClass();
		
		Local invoker =(Local) invokeExpr.getUseBoxes().get(invokeExpr.getUseBoxes().size()-1).getValue();
		Local listener = (Local)invokeExpr.getArg(0);
		String registar = invokeExpr.getMethod().getSubSignature();
		
		Type parseListenerType = parseListenerType(listener, body.getMethod(), stmt);
		RefType refType = (RefType)parseListenerType;
		SootClass listenerSootClass = refType.getSootClass();
		
		List<String> callbacks = getCallbacks(registar, ae);
		for(String callbackName:callbacks){
			if(listenerSootClass.declaresMethod(callbackName)){
				SootMethod callback = listenerSootClass.getMethod(callbackName);
				List<Value> values = new ArrayList<Value>();
				values.add(invoker);
				values.add(listener);
				values.add(StringConstant.v(callbackName));
				boolean isJumping = EHBOptions.v().isStaticAnalysis()?isJumpingMethod(callback):false;
				String msg = "Listener: "+listenerSootClass.getName()+" Callback: "+callback.getSignature()+" is Jumpling event? "+isJumping;
//				logger.log(Level.WARNING, msg);
				if(isJumping)
					values.add(IntConstant.v(1));
				else {
					values.add(IntConstant.v(0));
				}
				classifyEvent(ae, values, units, sc);		
			}
		}
	}

	private boolean isJumpingMethod(SootMethod callback) {
		MethodAnalysis methodAnalysis = new MethodAnalysis(callback);
		methodAnalysis.analyze();
		return methodAnalysis.isJumpMethod();
	}

	private void classifyEvent(AndroidEvent ae, List<Value> values,PatchingChain<Unit> units, SootClass sc) {
		
		//if stmt is a view event,base.doReflect(view, listener, callback);
		if(ae.equals(AndroidEvent.ViewEvent)){
			if(!sc.declaresMethod(DoReflect.SUBSIGNATURE))
				addDoReflectMethod(sc);
			SootMethod doReflect = sc.getMethod(DoReflect.SUBSIGNATURE);
			units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doReflect.makeRef(),values)), stmt);
//			body.validate();
		}
		//else if stmt is a dialog event, base.doReflect(dialog, listener, callback);
		else if(ae.equals(AndroidEvent.DialogEvent)){
			if(!sc.declaresMethod(DoDialogReflect.SUBSIGNATURE))
				addDoDialogReflectMethod(sc);
			SootMethod doDialogReflect = sc.getMethod(DoDialogReflect.SUBSIGNATURE);
			units.insertBefore( Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(doDialogReflect.makeRef(),values)), stmt);
//			body.validate();
		}
	}
	
	//add doReflectMethod to sc. @see DoReflect
	private void addDoReflectMethod(SootClass sc){
		DoReflect doReflect = new DoReflect(sc, DoReflect.SUBSIGNATURE);
		doReflect.build();
	}
	
	//add doDialogReflectMethod to sc. @see DoDialogReflect
	private void addDoDialogReflectMethod(SootClass sc){
		DoDialogReflect doDialogReflect = new DoDialogReflect(sc, DoDialogReflect.SUBSIGNATURE);
		doDialogReflect.build();
	}
	
	/**
	 * get callbacks of rm according to AndroidEvent
	 * @param rm register method.
	 * @param ae android event type, like View or Dialog event
	 * */
	public List<String> getCallbacks(String rm, AndroidEvent ae){
		if(AndroidEvent.DialogEvent.equals(ae)){
			return CallBack.dialogToCallBacks.get(rm);
		}
		else if(AndroidEvent.ViewEvent.equals(ae)){
			return CallBack.viewToCallBacks.get(rm);
		}
		throw new RuntimeException(this.getClass()+" unexpected AndroidEvent Type!");
	}

	/**
	 * parse the concrete type of listener.
	 * <p>
	 * e,g. btn.setOnClickListener(new OnClickListener({...})
	 * The type of listener is android.view.View$OnClickListener, not the concrete type.
	 * To get the concrete type of listener, we need use local analysis to track the
	 * definition  statement of listener and retrieve its actual type.
	 * </p>
	 * */
	private Type parseListenerType(Local value, SootMethod sm, Unit useUnit){
		LocalAnalysis localAnalysis = new LocalAnalysis(sm, value, useUnit);
		localAnalysis.analyze();
		DefinitionStmt definitionStmt = localAnalysis.getDefinitionStmt();
		
		if(definitionStmt==null)
			return value.getType();
		
		Value rightOp = definitionStmt.getRightOp();
		if(rightOp instanceof ThisRef){
			ThisRef thisRef = (ThisRef)rightOp;
			return thisRef.getType();
		}
		else if(rightOp instanceof NewExpr){
			NewExpr newExpr = (NewExpr)rightOp;
			return newExpr.getType();
		}
		return value.getType();
	}
	
}
