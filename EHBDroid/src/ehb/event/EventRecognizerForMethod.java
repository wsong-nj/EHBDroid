package ehb.event;

import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import com.app.test.AndroidConstants;
import com.app.test.Constants;

public class EventRecognizerForMethod extends EventRecognizerForCode{
	
	SootMethod sm;

	public EventRecognizerForMethod(SootMethod method) {
		this.sm = method;
	}
	
	@Override
	public AndroidEvent parseEvent() {
		
		String methodsubsig = sm.getSubSignature();
		SootClass declaringClass = sm.getDeclaringClass();
		//Method onOptionsItemSelected and onCreateOptionMenu must be declared in the same class.
		if(Constants.EHBMethod.onOptionsItemSelected_Name.equals(methodsubsig)&&
				declaringClass.declaresMethod(Constants.EHBMethod.onCreateOptionsMenu_activity)){
			if(Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(declaringClass)
					.contains(AndroidConstants.AndroidActivityClass)){
				return AndroidEvent.MenuEvent;
			}
		}
		if(Constants.EHBMethod.onListItemClick_Name.equals(methodsubsig)){
			List<SootClass> superClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(declaringClass);
			if(superClasses.contains(AndroidConstants.AndroidListActivityClass)){
				return AndroidEvent.ViewEvent;
			}
		}
		return null;
	}
}
