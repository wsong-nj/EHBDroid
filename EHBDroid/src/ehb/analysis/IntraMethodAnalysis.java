package ehb.analysis;

import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

public class IntraMethodAnalysis {

	protected Body body;
	protected Chain<Unit> chain;
	protected UnitGraph unitGraph;
	public IntraMethodAnalysis(SootMethod sm) {
		this.body = sm.retrieveActiveBody();
		this.chain = body.getUnits();
		this.unitGraph = new ExceptionalUnitGraph(body);
	}
	
	/**
	 * get all assign stmts
	 * */
	public List<AssignStmt> getAllAssignStmts() {
		return getAllAssignStmtsWithLineNumber(false);
	}

	/**
	 * get all assign stmts with line number
	 * */
	public List<AssignStmt> getAllAssignStmtsWithLineNumber(boolean b) {
		final List<AssignStmt> assignStmts = new ArrayList<AssignStmt>();
		int i = 0;
		for(Unit u:chain){
			i++;
			if(u instanceof AssignStmt){			
				Tag t = new IntegerConstantValueTag(i);
				AssignStmt stmt = (AssignStmt)u;
				if(b==true)
					stmt.addTag(t);
				assignStmts.add(stmt);
			}
		}
		return assignStmts;
	}

	/**
	 * get all the invoke statements 
	 * */
	public List<InvokeStmt> getAllInvokeStmts() {
		return getAllInvokeStmtsWithLineNumber(false);
	}

	/**
	 * get all the invoke statements with line number.
	 * */
	public List<InvokeStmt> getAllInvokeStmtsWithLineNumber(boolean b) {
		final List<InvokeStmt> invokeStmts = new ArrayList<InvokeStmt>();
		int i = 0;
		for(Unit u:chain){
			i++;
			if(u instanceof InvokeStmt){			
				Tag t = new IntegerConstantValueTag(i);
				InvokeStmt stmt = (InvokeStmt)u;
				if(b==true)
					stmt.addTag(t);
				invokeStmts.add(stmt);
				
			}
		}
		return invokeStmts;
	}
}