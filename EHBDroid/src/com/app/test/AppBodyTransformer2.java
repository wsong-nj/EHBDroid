package com.app.test;

import com.app.test.methodBuilder.DoReflect;
import com.app.test.methodCoverage.MethodCoverageFieldInstrumenter;
import com.app.test.methodCoverage.MethodCoverageStmtsInstrumenter;
import ehb.analysis.CallGraphBuilder;
import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.instrumentation.ActivityInstrumenter;
import ehb.instrumentation.ApplicationClassInstrumenter;
import ehb.instrumentation.BodyInstrumenter;
import ehb.instrumentation.FieldInstrumenter;
import ehb.instrumentation.codecoverage.CoverageBodyInstrumenter;
import ehb.instrumentation.codecoverage.CoverageClassInstrumenter;
import ehb.instrumentation.codecoverage.CoverageGlobals;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JVirtualInvokeExpr;

import java.util.*;

public class AppBodyTransformer2 extends BodyTransformer implements GlobalHost {

    public static Set<String> activities = (Set<String>) (((HashSet<String>) Global.v().getActivities()).clone());
    public static boolean classInstrumented = false;
    private String previousClassName = ""; // previous class name

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
//        SootClass mainActivityClass = Global.v().getMainActivityClass();
//        if (b.getMethod().getDeclaringClass().getName().contains("LoginActivity")) {
//            if (b.getMethod().getSubSignature().equals("void onCreate(android.os.Bundle)")) {
//                SootClass sootClass = Scene.v().getSootClass(Util2.class.getName());
////                if (!Scene.v().getApplicationClasses().contains(sootClass)) {
//                sootClass.setApplicationClass();
//                instrumentBody(b);
////                }
//            }
//        }
    }

    private void instrumentBody(Body body) {
        try {
            PatchingChain<Unit> units = body.getUnits();
            for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext(); ) {
                final Unit u = iter.next();
                u.apply(new AbstractStmtSwitch() {
                    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
                        System.out.println("caseReturnVoidStmt " + " Line52 " + stmt);
                        SootMethod method = Scene.v().getMethod("<com.app.test.Util2: void outPrint(java.lang.String)>");
                        StringConstant haha = StringConstant.v("haha");
                        StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(method.makeRef(), haha);
                        units.insertBefore(Jimple.v().newInvokeStmt(staticInvokeExpr), stmt);
                        body.validate();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToGlobal() {

    }
}
