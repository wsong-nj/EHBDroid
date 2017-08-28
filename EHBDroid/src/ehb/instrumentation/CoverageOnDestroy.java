package ehb.instrumentation;

import java.util.List;

import com.app.test.methodBuilder.MethodBuilder;

import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.ThisRef;

public class CoverageOnDestroy extends MethodBuilder {

	public static final String CLASSNAME = "onDestroy";
	public static final String SUBSIGNATURE = "void onDestroy()";

	public CoverageOnDestroy(SootClass sc) {
		this(sc, SUBSIGNATURE);
	}

	public CoverageOnDestroy(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		Local activity = addLocal("activity", sc_Type);
		addIdentityStmt(activity, new ThisRef(sc_Type));
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(codeCoverageToolkitPrintResult.makeRef()));
		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(activity, onDestroy_method.makeRef()));
		addReturnVoidStmt();
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(), Modifier.PUBLIC);
	}
}
