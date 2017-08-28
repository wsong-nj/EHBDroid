package ehb.analysis.entryPointCreator;

import java.util.Arrays;
import java.util.Map;

import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

//from resource files
public class CallBackFunctionFromXMLBuilder extends CallBackFunctionBuilder{

	public Map<Integer,String> idToCallBack;
	
	public CallBackFunctionFromXMLBuilder(Map<Integer,String> idToCallBack) {
		this.idToCallBack = idToCallBack;
	}
	
	@Override
	protected void buildCallBackFunctions() {
		for(SootClass sc:Scene.v().getClasses()){
			for(SootMethod sm:sc.getMethods()){
				if(sm.isConcrete()){
					for(Unit unit:sm.retrieveActiveBody().getUnits()){
						if(unit instanceof AssignStmt){
							AssignStmt stmt = (AssignStmt) unit;
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
										if(sc.declaresMethod(subSig))
											callbackFunctions.put(sc.getName(), Arrays.asList(sc.getMethod(subSig).getSignature()));
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
