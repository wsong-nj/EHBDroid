package ehb.instrumentation;

import java.util.List;

import com.app.test.methodBuilder.MethodBuilder;

import soot.Modifier;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.VoidType;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;

public class CoverageClinit extends MethodBuilder {

	public static final String CLASSNAME = "<clinit>";
	public static final String SUBSIGNATURE = "void <clinit>()";

	private int classIndex;
	private int validMethodCount;

	public CoverageClinit(SootClass sc, int classIndex, int validMethodCount) {
		super(sc, SUBSIGNATURE);
		this.classIndex = classIndex;
		this.validMethodCount = validMethodCount;
	}

	public CoverageClinit(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		List<Value> paramValues = paramValues();
		paramValues.add(IntConstant.v(classIndex));
		paramValues.add(IntConstant.v(validMethodCount));
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(codeCoverageToolkitInitbbblllij.makeRef(), paramValues));
		addReturnVoidStmt();
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.STATIC);
	}

}
