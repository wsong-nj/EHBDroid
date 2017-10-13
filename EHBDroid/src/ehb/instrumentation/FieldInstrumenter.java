package ehb.instrumentation;

import soot.*;

import com.app.test.Constants;
import com.app.test.constant.EHBField;

public class FieldInstrumenter implements IInstrumenter {

	private SootClass sc;

	public FieldInstrumenter(SootClass sc) {
		this.sc = sc;
	}

	/**
	 * add Constants.EHBField.UIEVENTLINKEDLIST, SYSTEMEVENTLINKEDLIST, INTERAPPEVENTLINKEDLIST, 
	 * visited and activitymenu, contextMenu.
	 * */
	@Override
	public void instrument() {
		addFieldsToActivity(sc);
	}
	
	/**
	 * add ui, system,interApp, visited and activityMenu 5 fields to activity.
	 * */
	public void addFieldsToActivity(SootClass sc){
		for(String fieldName: EHBField.eventsLinkedList){
			SootField field = getPublicStaticField(fieldName, Constants.linkedList_Type);
			if(!sc.declaresFieldByName(fieldName))
				sc.addField(field);
		}
		for(String fieldName: EHBField.visited){
			SootField field = getPublicStaticField(fieldName, BooleanType.v());
			if(!sc.declaresFieldByName(fieldName))
				sc.addField(field);
		}
		
		SootField menuField = getPublicStaticField(EHBField.ACTIVITYMENU, Constants.menu_Type);
		if(!sc.declaresFieldByName(EHBField.ACTIVITYMENU))
			sc.addField(menuField);
		
		//if sc contains onCreateContextMenu method, add a contextMenu field.
		if(sc.declaresMethod(Constants.onCreateContextMenu_method.getSubSignature())){
			SootField contextMenuField = getPublicStaticField(EHBField.CONTEXTMENU, Constants.contextMenu_Type);
			if(!sc.declaresFieldByName(EHBField.CONTEXTMENU))
				sc.addField(contextMenuField);
		}
		
	}

	private SootField getPublicStaticField(String name, Type type){
		return getSootField(name, type, Modifier.PUBLIC|Modifier.STATIC);
	}

	private SootField getSootField(String name, Type type, int modifiers){
		return new SootField(name, type, modifiers);
	}
}
