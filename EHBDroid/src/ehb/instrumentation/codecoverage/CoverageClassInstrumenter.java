package ehb.instrumentation.codecoverage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.app.test.Constants;

import ehb.analysis.IAnalysis;
import ehb.builderfactory.LocalBuilder;
import ehb.global.Global;
import ehb.instrumentation.CoverageClinit;
import ehb.instrumentation.CoverageOnDestroy;
import ehb.instrumentation.IInstrumenter;
import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;

/**
 * 代码覆盖率之--类插桩，类插桩分为两个步骤： 1. 添加静态代码块 2. 修改静态代码块
 * 
 * 存在一个问题: 如果一个类是内部类，其不能定义静态代码块，因此内部类的初始化在其最外部类中(注意：内部类的嵌套)
 * 
 * @author Administrator
 * 
 *         Usage: new CoverageClassInstrumentation(sc).instrument();
 *
 */
public class CoverageClassInstrumenter extends LocalBuilder implements IInstrumenter {

	public static final String METHODSUBSIGNATURE_CLINIT = "void <clinit>()";
	public static final String METHODSUBSIGNATURE_ONDESTROY = "void onDestroy()";

	public static Set<String> activitiesClone = (Set<String>) (((HashSet<String>) Global.v().getActivities()).clone());
	public static List<String> visitedClasses = new ArrayList<>();
	public static List<String> visitedInnerClasses = new ArrayList<>();

	private SootClass sc;
	private boolean isOutClass;

	/**
	 * <clinit>, <init>, <access$1 are invalid method
	 */
	private int validMethodCount;

	public CoverageClassInstrumenter(SootClass sc) {
		this.sc = sc;
		validMethodCount = new ValidMethodAnalysis(sc).getValidMethodCount();
	}

	/**
	 * if a sootClass is an innerClass, invoke this constructor
	 * 
	 * @param outerClass
	 *            An innerClass
	 * @param validMethodCount
	 *            innerClass's valid method
	 */
	public CoverageClassInstrumenter(SootClass outerClass, int validMethodCount) {
		this.sc = outerClass;
		isOutClass = true;
		this.validMethodCount = validMethodCount;
	}

	@Override
	public void instrument() {
		if(validMethodCount==0) //If a class does not have any class, ignore it.
			return;
		
		if (sc.isConcrete()||sc.isAbstract()) {
			if (InnerClassAnalysis.isInnerClass(sc)) {
//				System.out.println("InnerClass: "+sc);
				if (!visitedInnerClasses.contains(sc.getName())) {
					CoverageGlobals.addClassIndex();
					visitedInnerClasses.add(sc.getName());
					SootClass outClass = InnerClassAnalysis.getOutClass(sc);
//					System.out.println("OutClass: "+outClass);
					new CoverageClassInstrumenter(outClass, validMethodCount).instrument();
				}
			} else {
				if (isOutClass) {
					handleClinit();
//					System.out.println(this.getClass()+" 哈哈: "+sc.getMethodByName("<clinit>").retrieveActiveBody().toString());
				} else if (!visitedClasses.contains(sc.getName())) {
					CoverageGlobals.addClassIndex();
					visitedClasses.add(sc.getName());
					handleClinit();
				}
			}

			// if sc is activity, instrument CoverageGlobal.printResult() to onDestroy.
			if (activitiesClone.contains(sc.getName())) {
				activitiesClone.remove(sc.getName());
				handleOnDestroy(sc);
			}
		}
	}

	/**
	 * modify or add <clinit>
	 */
	private void handleClinit() {
		if (sc.declaresMethodByName("<clinit>"))
			modifyClinit();
		else
			addClinit();
	}

	private void modifyClinit() {
		SootMethod clinitMethod = sc.getMethodByName("<clinit>");
		Body b = clinitMethod.retrieveActiveBody();
		PatchingChain<Unit> units = clinitMethod.getActiveBody().getUnits();

		List<Value> values = new ArrayList<>();
		values.add(IntConstant.v(CoverageGlobals.classIndex));
		values.add(IntConstant.v(validMethodCount));

		InvokeStmt initbbblllij = Jimple.v().newInvokeStmt(
				Jimple.v().newStaticInvokeExpr(Constants.codeCoverageToolkitInitbbblllij.makeRef(), values));
		Unit last = units.getLast();
		units.insertBefore(initbbblllij, last);
		b.validate();
	}

	private void addClinit() {
		new CoverageClinit(sc, CoverageGlobals.classIndex, validMethodCount).build();
	}

	/**
	 * modify or add onDestroy()
	 * 
	 * @param activity
	 *            an activity class
	 */
	private void handleOnDestroy(SootClass activity) {
		if (activity.declaresMethod(METHODSUBSIGNATURE_ONDESTROY))
			modifyOnDestroy(activity);
		else
			addOnDestroy(activity);
	}

	private void modifyOnDestroy(SootClass activity) {
		SootMethod onDestroy = activity.getMethod(METHODSUBSIGNATURE_ONDESTROY);
		Body b = onDestroy.retrieveActiveBody();
		PatchingChain<Unit> units = b.getUnits();
		Unit last = units.getLast();
		units.insertBefore(
				Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(codeCoverageToolkitPrintResult.makeRef())),
				last);
		b.validate();
	}

	private void addOnDestroy(SootClass activity) {
		new CoverageOnDestroy(activity).build();
	}

	// ----------------------------------------------------------------------------
	public static class InnerClassAnalysis {

		public static boolean isInnerClass(SootClass sc) {
			return sc.getName().contains("$");
		}

		public static SootClass getOutClass(SootClass sc) {
			if (isInnerClass(sc)) {
				return parseOutClass(sc);
			} else
				return sc;
		}

		private static SootClass parseOutClass(SootClass sc) {
			String outClassName = sc.getName().split("\\$")[0];
			return Scene.v().getSootClass(outClassName);
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * 分析一个class中有效的方法数
	 * 
	 * @author Administrator
	 */
	public static class ValidMethodAnalysis implements IAnalysis {
		private SootClass sc;
		private int validMethodCount;

		public ValidMethodAnalysis(SootClass sc) {
			this.sc = sc;
			analyze();
		}

		@Override
		public void analyze() {
			for (SootMethod sm : sc.getMethods()) {
				if (isValidMethod(sm)) {
					validMethodCount++;
				}
			}
		}

		public int getValidMethodCount() {
			return validMethodCount;
		}

		/**
		 * exclude methods: <clinit>, <init> and access$1
		 * 
		 * @param sm
		 * @return whether sm is a valid method
		 */
		public static boolean isValidMethod(SootMethod sm) {
			if (sm.isConcrete()) {
				String subSignature = sm.getSubSignature();
				if (subSignature.contains("<clinit>()") || subSignature.contains("<init>()")
						|| subSignature.contains("access$")) {
					return false;
				} else
					return true;

			} else
				return false;
		}
	}
}
