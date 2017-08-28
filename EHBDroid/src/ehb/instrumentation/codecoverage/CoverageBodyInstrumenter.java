package ehb.instrumentation.codecoverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ParameterRef;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import com.app.test.exception.EHBExceptionReporter;

import ehb.analysis.IAnalysis;
import ehb.builderfactory.LocalBuilder;
import ehb.instrumentation.IInstrumenter;

public class CoverageBodyInstrumenter extends LocalBuilder implements
		IInstrumenter {

	private Body body;
	private UnitGraph graph;

	public CoverageBodyInstrumenter(Body b) {
		this.body = b;
		this.graph = new BriefUnitGraph(b);
	}

	@Override
	public void instrument() {
		boolean isValid = CoverageClassInstrumenter.ValidMethodAnalysis
				.isValidMethod(body.getMethod());
		if (isValid) {
			CoverageGlobals.controlClassAndMethodIndex(body.getMethod()
					.getDeclaringClass());
			instrumentBody();
		}
	}

	private void instrumentBody() {

		MethodAnaysis analysis = new MethodAnaysis(body);
		Unit startUnit = analysis.getStartUnit();
		int branch = analysis.calcuBranch();
		Map<Unit, List<Integer>> unitToLength = analysis.getUnitToLength();

		PatchingChain<Unit> units = body.getUnits();

		// 1. init array bbb[i][j][k] and lll[i][j][k]
		{
			List<Value> values = new ArrayList<>();
			values.add(IntConstant.v(CoverageGlobals.classIndex));
			values.add(IntConstant.v(CoverageGlobals.methodIndex));
			values.add(IntConstant.v(branch));
			InvokeStmt initbbblllijk = Jimple.v()
					.newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(
									codeCoverageToolkitInitbbblllijk.makeRef(),
									values));
			units.insertAfter(initbbblllijk, startUnit);
		}

		// 2. insert instrumentData statement before each branch
		{
			int branchIndex = 1;
			for (Unit unit : unitToLength.keySet()) {
				List<Integer> list = unitToLength.get(unit);
				if (list.size() > 1) {
					try {
						List<Unit> predsOf = graph.getPredsOf(unit);
						for (int i = 0; i < predsOf.size(); i++) {// 如果有多个分支，则将该语句插入到语句的前驱的前面
							Unit preUnit = predsOf.get(i);
							if (isCaughtExceptionStmt(preUnit))
								continue;

							List<Value> values = new ArrayList<>();
							values.add(IntConstant
									.v(CoverageGlobals.classIndex));
							values.add(IntConstant
									.v(CoverageGlobals.methodIndex));
							values.add(IntConstant.v(branchIndex));
							values.add(IntConstant.v(list.get(i)));
							InvokeStmt instrumentData = Jimple
									.v()
									.newInvokeStmt(
											Jimple.v().newStaticInvokeExpr(
													codeCoverageToolkitInstrumentData
															.makeRef(), values));
							units.insertBefore(instrumentData, preUnit);
							branchIndex++;
						}
					} catch (Exception e) {
						for (Unit unit2 : unitToLength.keySet()) {
							System.out.println("87: Unit: " + unit2 + " List: "
									+ unitToLength.get(unit2));
						}
						throw new RuntimeException();
					}
				} else if (list.size() == 1) { // 如果只有一个分支，则将语句插入到该语句的前面
					if (isCaughtExceptionStmt(unit))
						continue;

					List<Value> values = new ArrayList<>();
					values.add(IntConstant.v(CoverageGlobals.classIndex));
					values.add(IntConstant.v(CoverageGlobals.methodIndex));
					values.add(IntConstant.v(branchIndex));
					values.add(IntConstant.v(list.get(0)));
					InvokeStmt instrumentData = Jimple.v().newInvokeStmt(
							Jimple.v()
									.newStaticInvokeExpr(
											codeCoverageToolkitInstrumentData
													.makeRef(), values));
					units.insertBefore(instrumentData, unit);
					branchIndex++;
				}
			}
		}
	}

	private boolean isCaughtExceptionStmt(Unit unit) {
		if (unit instanceof IdentityStmt) {
			IdentityStmt identityStmt = (IdentityStmt) unit;
			Value rightOp = identityStmt.getRightOp();
			if (rightOp instanceof CaughtExceptionRef
					|| rightOp instanceof ThisRef
					|| rightOp instanceof ParameterRef) {
				System.out.println("122: " + unit + " body: "
						+ body.getMethod().getName());
				return true;
			}
		}
		return false;
	}

	// ---------------------------------------------------------------------------------
	public static class MethodAnaysis implements IAnalysis {

		/**
		 * record the length of each branchunit.
		 */
		private Map<Unit, List<Integer>> unitToLength = new HashMap<>();

		// private List<Object> unitToLength = new ArrayList<>();

		/**
		 * In Jimple, Identity Stmt must be at the first line. In Java, super()
		 * and this() must be at the first line.
		 * 
		 * Therefore, the statement order of a jimple method must be like: 1.
		 * Identity Statements 2. super() or this() 3. other statments.
		 */
		private Unit startUnit;

		private Body body;
		private UnitGraph unitGraph;

		private List<Unit> visitedUnits = new ArrayList<>();

		public MethodAnaysis(Body b) {
			this.body = b;
			this.unitGraph = new BriefUnitGraph(body);
			analyze();
		}

		@Override
		public void analyze() {
			PatchingChain<Unit> units = body.getUnits();
			startUnit = units.getFirst();
			for (Unit unit : units) {
				if (unit instanceof IdentityStmt) {
					startUnit = unit;
				} else if (unit instanceof InvokeStmt) {
					InvokeStmt is = (InvokeStmt) unit;
					InvokeExpr invokeExpr = is.getInvokeExpr();
					if (invokeExpr instanceof SpecialInvokeExpr) {
						startUnit = unit;
						break;
					}
					break;
				} else
					break;
			}
			tranUnitGraph();
		}

		public void tranUnitGraph() {
			List<Unit> tails = unitGraph.getTails();
			for (Unit last : tails) {
				calcuDepth(last, 0, last);
			}
		}

		public void calcuDepth(Unit unit, int depth, Unit branchUnit) {
			depth++;
			if (visitedUnits.contains(unit)) {
				addLength(branchUnit, depth);
				return;
			}
			visitedUnits.add(unit);
			if (isEntry(unit)) {
				addLength(branchUnit, depth);
			} else if (isInBranch(unit) || isOutBranch(unit)) {
				addLength(branchUnit, depth);
				for (Unit u : unitGraph.getPredsOf(unit)) {
					calcuDepth(u, 0, unit);
				}
			} else {
				try {
					calcuDepth(unitGraph.getPredsOf(unit).get(0), depth,
							branchUnit);
				} catch (Exception e) {
					EHBExceptionReporter.report(this.getClass(), body
							.getMethod().getDeclaringClass(), body, body
							.getMethod(), unit, " Unit do not have preds!"
							+ " E: " + e);
					// throw new RuntimeException("Unit: "+unit+" ");
				}

			}
		}

		private boolean addLength(Unit unit, int depth) {
			if (unitToLength.keySet().contains(unit)) {
				unitToLength.get(unit).add(depth);
				return true;
			} else {
				List<Integer> list = new ArrayList<>();
				list.add(depth);
				unitToLength.put(unit, list);
				return false;
			}
		}

		/**
		 * If unit is a in branch(inDegree>1).
		 * 
		 * @param unit
		 * @return
		 */
		private boolean isInBranch(Unit unit) {
			return unitGraph.getPredsOf(unit).size() > 1;
		}

		/**
		 * If unit is a out branch(outDegree>1).
		 * 
		 * @param unit
		 * @return
		 */
		private boolean isOutBranch(Unit unit) {
			return (unit instanceof IfStmt)
					|| (unit instanceof LookupSwitchStmt)
					|| (unit instanceof TableSwitchStmt);
		}

		private boolean isEntry(Unit unit) {
			return unitGraph.getPredsOf(unit).size() == 0;
		}

		public int calcuBranch() {
			int branch = 0;
			for (Unit unit : unitToLength.keySet()) {
				int size = unitToLength.get(unit).size();
				branch = branch + size;
			}
			return branch;
		}

		public Unit getStartUnit() {
			return startUnit;
		}

		public Map<Unit, List<Integer>> getUnitToLength() {
			return unitToLength;
		}
	}
}
