package ehb.analysis;

import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

/**
 * Get the definition of local. 
 * */
public class LocalAnalysis implements IAnalysis{
	
	SootMethod useMethod;
	Value value;
	Unit useUnit;
	
	DefinitionStmt definitionStmt;
	
	public LocalAnalysis(SootMethod sm, Value value, Unit useUnit) {
		this.useMethod = sm;
		this.value = value;
		this.useUnit = useUnit;
	}
	
	@Override
	public void analyze() {
		definitionStmt = getDefAssignStmt(useUnit, value, useMethod);
	}

	/**
	 * get definition statement before but close to currentStmt. 
	 * @param useStmt use statement.
	 * @param value value to be analyzed.
	 * @param ug unitGraph of method.
	 * @return definition statement defined value.
	 * */
	private DefinitionStmt getDefAssignStmt(Unit useStmt, Value value, SootMethod sm){
		UnitGraph ug = new ExceptionalUnitGraph(sm.retrieveActiveBody());
		DefinitionStmt defStmt = null;
		while(true){
			if(useStmt instanceof AssignStmt){
				AssignStmt as = (AssignStmt)useStmt;
				Value v = as.getLeftOp();
				if(isEqual(v, value)){
					Value rightOp = as.getRightOp();
					//1. rightOp is castExpr, assign value with rightOp.
					if((rightOp instanceof CastExpr)){
						CastExpr ceROp = (CastExpr)rightOp;
						Type type = ceROp.getType();
						RefType refType = (RefType)type;
						if(refType.getSootClass().getName().startsWith("android")){
							CastExpr ce = (CastExpr)(rightOp);
							value = ce.getOp();
						}
					}
					//2. if rightOp is Field, start to use field analysis
					else if(rightOp instanceof FieldRef){
						FieldAnalysis fieldAnalysis = new FieldAnalysis(ug.getBody().getMethod(), (FieldRef)rightOp);
						if(fieldAnalysis.isTheSameMethod()){
							value = rightOp;
						}
						else{
							fieldAnalysis.analyze();
							return fieldAnalysis.getFinalDefUnit();
						}
					}
					//3. if rightOp is JimpleLocal, assign value with rightOp.
					else if(rightOp instanceof JimpleLocal){
						value = rightOp;
					}
					//4. find the target assignStmt.
					else{
						defStmt = (AssignStmt)useStmt;	
						break;
					}		
				}
			}	
			else if(useStmt instanceof IdentityStmt){
				IdentityStmt identityStmt = (IdentityStmt)useStmt;
				Value rightOp = identityStmt.getRightOp();
				if(rightOp instanceof ThisRef){
					defStmt = (DefinitionStmt)useStmt;
				}
				else if(rightOp instanceof ParameterRef){
					defStmt = (DefinitionStmt)useStmt;
				}
			}
			useStmt = ug.getPredsOf(useStmt).get(0);
			if(ug.getHeads().contains(useStmt))
				break;
		}
		return defStmt;
	}
	
	
	/**
	 * whether v1 equals to v2.
	 * if v1, v2 are FieldRef, compare their field value.
	 * if v1, v2 are locals, compare them directly. 
	 * */
	public boolean isEqual(Value v1, Value v2){
		if(v2 instanceof FieldRef){
			if(v1 instanceof FieldRef){
				FieldRef fr1 = (FieldRef)v1;
				FieldRef fr2 = (FieldRef)v2;
				if(fr1.getField().equals(fr2.getField())){
					return true;
				}
			}
		}
		else if(v1.equals(v2)){
			return true;
		}
		return false;
	}

	public DefinitionStmt getDefinitionStmt() {
		return definitionStmt;
	}
}
