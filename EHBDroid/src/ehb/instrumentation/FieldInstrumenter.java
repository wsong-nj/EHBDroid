package ehb.instrumentation;

import soot.BooleanType;
import soot.Modifier;
import soot.SootClass;
import soot.SootField;

import com.app.test.Constants;
import com.app.test.Constants.EHBField;

import ehb.global.Global;

public class FieldInstrumenter implements IInstrumenter {

	SootClass sc;

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
		for(String fieldName:Constants.EHBField.eventsLinkedList){
			SootField field = new SootField(fieldName, Constants.linkedList_Type, Modifier.PUBLIC|Modifier.STATIC);
			if(!sc.declaresFieldByName(fieldName))
				sc.addField(field);
		}
		for(String fieldName:Constants.EHBField.visited){
			SootField field = new SootField(fieldName, BooleanType.v(), Modifier.PUBLIC|Modifier.STATIC);
			if(!sc.declaresFieldByName(fieldName))
				sc.addField(field);
		}
		
		SootField menuField = new SootField(EHBField.ACTIVITYMENU, Constants.menu_Type, Modifier.PUBLIC|Modifier.STATIC);
		if(!sc.declaresFieldByName(EHBField.ACTIVITYMENU))
			sc.addField(menuField);
		
		//only if sc contains onCreateContextMenu method, add contextMenu field. 
		if(sc.declaresMethod(Constants.onCreateContextMenu_method.getSubSignature())){
			SootField contextMenuField = new SootField(EHBField.CONTEXTMENU, Constants.contextMenu_Type, Modifier.PUBLIC|Modifier.STATIC);
			if(!sc.declaresFieldByName(EHBField.CONTEXTMENU))
				sc.addField(contextMenuField);
		}
		
	}
}
