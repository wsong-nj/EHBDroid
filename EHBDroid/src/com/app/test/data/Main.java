package com.app.test.data;

import java.util.Set;

import com.app.test.AppDir;

import ehb.analysis.CallGraphBuilder;
import polyglot.ast.Return;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JIfStmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;

public class Main {
	public static void main(String[] args) {
		String params[] = {"-android-jars","D:/SDK/platforms",
				"-process-dir", "E:/benchmark/"+AppDir.APPNAME+".apk"};
//		String params[] = {"-android-jars","D:/adt-eclipse/sdk/platforms","-process-dir", "D:/WorkSpace/Administrator/workspace/svn/TestedApk_Simple/"+args[0]+".apk","-option","oneevent"};
		String apkFileLocation = params[3];
		CallGraphBuilder cgb = new CallGraphBuilder(apkFileLocation);
		soot.G.reset();
		cgb.build();
		Set<String> entryPoint = cgb.getEntryPoint();
		System.out.println(entryPoint);
		test();
		test1();
	}
	
	public static void test(){
		SootClass sootClass = Scene.v().getSootClass("cz.romario.opensudoku.gui.SudokuPlayActivity$3");
		SootMethod method = sootClass.getMethod("void <init>(cz.romario.opensudoku.gui.SudokuPlayActivity)");
		Body body = method.retrieveActiveBody();
		
		BriefUnitGraph briefUnitGraph = new BriefUnitGraph(body);
		
		for(Unit unit:briefUnitGraph.getBody().getUnits()){
			if(unit instanceof ReturnVoidStmt){
				System.out.println("48: "+briefUnitGraph.getPredsOf(unit));
			}
			System.out.println(unit+" ");
			if(unit instanceof IfStmt){
				JIfStmt ifStmt = (JIfStmt)unit;
				Stmt target = ifStmt.getTarget();
				System.out.println("IFStmt: Succs: "+briefUnitGraph.getSuccsOf(ifStmt));
				System.out.println("IFStmt:-------------------------"+unit+" Target: "+target+" isGotoStmt: "+(target instanceof GotoStmt));
			}else if(unit instanceof GotoStmt){
				System.out.println("GotoStmt:-----------------------"+unit);
			}
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(briefUnitGraph.toString());
		
		
		
		
//		PatchingChain<Unit> units = method.getActiveBody().getUnits();
		
//		EventDistinguisher eventDistinguisher = new EventDistinguisher(method);
//		System.out.println(eventDistinguisher.isActivityJumpingEvent());
//		
//		SootClass sootClass1 = Scene.v().getSootClass("cz.romario.opensudoku.gui.SudokuListActivity");
//		SootMethod method1 = sootClass1.getMethodByName("onListItemClick");
//		
//		EventDistinguisher eventDistinguisher1 = new EventDistinguisher(method1);
//		System.out.println(eventDistinguisher1.isActivityJumpingEvent());
		
	}
	public static void test1(){
		SootClass sootClass = Scene.v().getSootClass("cz.romario.opensudoku.gui.SudokuBoardView");
		SootMethod method = sootClass.getMethodByName("onTouchEvent");
		Body body = method.retrieveActiveBody();
		
		BriefUnitGraph briefUnitGraph = new BriefUnitGraph(body);
		for(Unit unit:briefUnitGraph.getBody().getUnits()){
			System.out.println(unit);
			if(unit instanceof IfStmt){
				System.out.println("IFStmt:-------------------------"+unit);
			}else if(unit instanceof GotoStmt){
				System.out.println("GotoStmt:-----------------------"+unit);
			}
			else if(unit instanceof LookupSwitchStmt){
				System.out.println("LookUpStmt:---------------------"+unit);
			}
		}
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(briefUnitGraph.toString());
	}
}
