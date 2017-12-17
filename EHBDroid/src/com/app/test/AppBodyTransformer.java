package com.app.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.app.test.methodCoverage.MethodCoverageFieldInstrumenter;
import com.app.test.methodCoverage.MethodCoverageStmtsInstrumenter;
import ehb.analysis.CallGraphBuilder;
import ehb.analysis.MethodAnalysis;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.instrumentation.ActivityInstrumenter;
import ehb.instrumentation.BodyInstrumenter;
import ehb.instrumentation.ApplicationClassInstrumenter;
import ehb.instrumentation.FieldInstrumenter;
import ehb.instrumentation.codecoverage.CoverageBodyInstrumenter;
import ehb.instrumentation.codecoverage.CoverageClassInstrumenter;
import ehb.sign.SignCheckRemover;

public class AppBodyTransformer extends BodyTransformer implements GlobalHost {

    public static Set<String> activities = (Set<String>) (((HashSet<String>) Global.v().getActivities()).clone());
    public static boolean classInstrumented = false;
    private String previousClassName = ""; // previous class name

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        try {
            System.out.println("internalTransform " + " Line52 " +"do nothing");
//            if (!classInstrumented) {
//                instrumentApplicationClasses();
//                instrumentFieldForMainActivity();
//                classInstrumented = true;
//            }
//
//            SootClass sc = b.getMethod().getDeclaringClass();
//            String name = sc.getName();
//            if (name.startsWith("android") || name.startsWith("java") || name.startsWith("com.facebook") || name.startsWith("org.eclipse"))
//                return;
//            if (isActivity(sc)) {
//                instrumentActivity(sc);
//            }
//            instrumentBody(b);
//            countNumbers(b);
//
//            if (!isExcludedBody(b)) {
//                instrumentMethodCoverage(b, sc);
//                countNumbers(b);
//            }
//
//            new SignCheckRemover(b).removeSignCheckingStmt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void instrumentApplicationClasses() {
        new ApplicationClassInstrumenter(CallGraphBuilder.getApplicationClasses()).instrument();
    }

    private void instrumentFieldForMainActivity() {
        new FieldInstrumenter(Global.v().getMainActivityClass()).instrument();
    }

    private boolean isActivity(SootClass sc) {
        String name = sc.getName();
        if (activities.contains(name)) {//remove visited activity.
            activities.remove(name);
            return true;
        }
        return false;
    }

    private void instrumentActivity(SootClass sc) {
        new ActivityInstrumenter(sc).instrument();
    }

    private void instrumentBody(Body b) {
        new BodyInstrumenter(b).instrument();
    }

    private void instrumentLineCoverage(Body body) {
        new CoverageClassInstrumenter(body.getMethod().getDeclaringClass()).instrument();
        new CoverageBodyInstrumenter(body).instrument();
    }

    private void countNumbers(Body b) {
        SootMethod method = b.getMethod();
        SootClass sc = method.getDeclaringClass();
        boolean isValidMethod = CoverageClassInstrumenter.ValidMethodAnalysis.isValidMethod(method);
        if (isValidMethod) {
            PatchingChain<Unit> units = b.getUnits();
            Main.totalLine = Main.totalLine + units.size();
            Main.totalMethod++;
            Main.methods.add(b.getMethod().getSignature());
            if (sc.getName() != previousClassName) {
                previousClassName = sc.getName();
                Main.totalClass++;
            }
        }
    }

    private void instrumentMethodCoverage(Body b, SootClass sc) {
        new MethodCoverageFieldInstrumenter(sc).instrument();
        new MethodCoverageStmtsInstrumenter(b).instrument();
    }

    public boolean isExcludedBody(Body b) {
        String methodName = b.getMethod().getName();
        SootClass sc = b.getMethod().getDeclaringClass();

        SootMethod method = b.getMethod();
        if (methodName.equals("<clinit>")) {
            return true;
        }
        //static inner class's stmts num is 3
        else if (methodName.equals("<init>")) {
            PatchingChain<Unit> units = b.getUnits();
            if (sc.getName().contains(".R$")) {
                if (units.size() == 3) {
                    return true;
                }
            } else {
                int stmtsNum = calculateStmtsNum(b.getMethod());
                if (units.size() == stmtsNum) {
                    return true;
                }
            }
        } else if (methodName.startsWith("access$")) {
            return true;
        }
        return false;
    }

    /**
     * when init Method is an innerClass,
     * if contains 1 $, the stmtsNum is 5.
     * if contains 2 $, the stmtsNum is 7.
     */
    public int calculateStmtsNum(SootMethod initMethod) {
        if (!initMethod.getName().equals("<init>")) {
            return -1;
        }
        SootClass sootClass = initMethod.getDeclaringClass();
        String className = sootClass.getName();
        int statementsNum = 3;
        if (className.contains("$")) {
            //if s is a special character, like $,|, we use \\$ instead of $
            String[] split = className.split("\\$");
            if (split[1].matches("[0-9]")) {
                statementsNum = 2 * split.length + 1;
            }
        }
        return statementsNum;
    }

    @Override
    public void addToGlobal() {

    }
}
