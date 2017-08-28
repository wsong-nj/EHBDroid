package ehb.builderfactory;

import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.jimple.Jimple;

import com.app.test.Constants;

public class LocalBuilder extends Constants{
	
	protected Local addLocal(Body body,String name,Type refType){
		Local newLocal = Jimple.v().newLocal(name, refType);
		body.getLocals().add(newLocal);
		return newLocal;
	}
	
	protected Local addLocalArray(Body body,String name,Type refType){
		Local newLocal = Jimple.v().newLocal(name, ArrayType.v(refType, 1));
		body.getLocals().add(newLocal);
		return newLocal;
	}
}
