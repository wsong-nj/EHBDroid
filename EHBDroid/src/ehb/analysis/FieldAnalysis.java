package ehb.analysis;

import java.util.List;

import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class FieldAnalysis implements IAnalysis{
	
	FieldRef targetFieldRef;
	SootClass sc;
	DefinitionStmt finalDefUnit;
	SootMethod useMethod;
	SootMethod defMethod;
	
	public FieldAnalysis(SootMethod usedMethod,FieldRef targetFieldRef) {
		this.targetFieldRef = targetFieldRef;
		this.useMethod = usedMethod;
		sc = targetFieldRef.getField().getDeclaringClass();
	}

	/**
	 * if the definition method of field is the same with its use method.
	 * */
	public boolean isTheSameMethod(){
		IntraMethodAnalysis intraMethodAnalysis = new IntraMethodAnalysis(useMethod);
		for(AssignStmt as:intraMethodAnalysis.getAllAssignStmtsWithLineNumber(true)){
			if(as.getLeftOp() instanceof FieldRef){
				FieldRef frRef = (FieldRef)as.getLeftOp();
				if(frRef.getField().equals(targetFieldRef.getField())){
					defMethod = useMethod;
					return true;
				}
			}
		}
		return false;
	}
	
	private MethodAndStmt findDefMethod(List<SootMethod> methods){
		if(isTheSameMethod()){
			return null;
		}
		for(SootMethod method:methods){
			if(!method.isConcrete()||method.equals(useMethod)){
				continue;
			}
			IntraMethodAnalysis analysis = new IntraMethodAnalysis(method);
			for(AssignStmt as:analysis.getAllAssignStmtsWithLineNumber(true)){
				if(as.getLeftOp() instanceof FieldRef){
					FieldRef fieldRef = (FieldRef)as.getLeftOp();
					if(fieldRef.getField().equals(targetFieldRef.getField())){
						return new MethodAndStmt(method, as);
					}
				}
			}
		}
		return null;
	}
	
	private DefinitionStmt findDefValue(MethodAndStmt findDefMethod ){
		if(findDefMethod==null){
			return null;
		}
		SootMethod sm = findDefMethod.getSm();
		AssignStmt stmt = (AssignStmt) findDefMethod.getStmt();
		LocalAnalysis localAnalysis = new LocalAnalysis(sm, stmt.getLeftOp() , stmt);
		localAnalysis.analyze();
		return localAnalysis.getDefinitionStmt();
	}

	public DefinitionStmt getFinalDefUnit() {
		return finalDefUnit;
	}

	@Override
	public void analyze() {
		MethodAndStmt findDefMethod = findDefMethod(sc.getMethods());
		finalDefUnit = findDefValue(findDefMethod);
	}
	
	class MethodAndStmt{
		
		SootMethod sm;
		Stmt stmt;
		public MethodAndStmt(SootMethod sm, Stmt stmt) {
			super();
			this.sm = sm;
			this.stmt = stmt;
		}
		public SootMethod getSm() {
			return sm;
		}
		public Stmt getStmt() {
			return stmt;
		}
		
	}
}

