package ehb.event;

import soot.Body;
import soot.jimple.InvokeStmt;
import ehb.builderfactory.StmtBuilder;
import ehb.event.EventRecognizerForCode.AndroidEvent;

public abstract class EventDispatcher extends StmtBuilder{
	
	InvokeStmt stmt;
	AndroidEvent ae;
	final Body body;
	
	public EventDispatcher(InvokeStmt stmt, Body b, AndroidEvent ae) {
		this.stmt = stmt;
		this.body = b;
		this.ae = ae;
	}
	
	public void dispatchEvent(){
		addDispathingStmts();
	}
	
	/**
	 * add dispatching stmts to body.
	 * */
	protected abstract void addDispathingStmts();
}
