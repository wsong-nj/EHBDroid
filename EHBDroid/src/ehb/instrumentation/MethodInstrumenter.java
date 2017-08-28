package ehb.instrumentation;

import soot.SootClass;
import soot.SootMethod;

import com.app.test.Constants;
import com.app.test.methodBuilder.Clinit;
import com.app.test.methodBuilder.OnCreateOptionsMenu;
import com.app.test.methodModifier.ClinitForActivityModifier;
import com.app.test.methodModifier.OnCreateContextMenuModifier;
import com.app.test.methodModifier.OnCreateOptionMenuModifier;

import ehb.global.Global;

/**
 * */
public class MethodInstrumenter implements IInstrumenter {

	SootClass sc;

	public MethodInstrumenter(SootClass sc) {
		this.sc = sc;
	}

	@Override
	public void instrument() {
		instrumentMethodsToActivity(sc);
	}

	private void instrumentMethodsToActivity(SootClass activity) {
		if (!activity.declaresMethod(Clinit.SUBSIGNATURE))
			addClinitForActivity(activity);
		else
			modifyClinitForActivity(activity.getMethod(Clinit.SUBSIGNATURE),
					activity);

		if (!activity.declaresMethod(OnCreateOptionsMenu.SUBSIGNATURE))
			addOnCreateOptionsMenuMethod(activity);
		else
			modifyOnCreateOptionMenu(activity.getMethod(OnCreateOptionsMenu.SUBSIGNATURE));

		String sig = "void onCreateContextMenu(android.view.ContextMenu,android.view.View,android.view.ContextMenu$ContextMenuInfo)";
		if (activity.declaresMethod(sig)) {
			modifyOnCreateContextMenu(activity.getMethod(sig));
		}
	}

	private void modifyOnCreateOptionMenu(SootMethod method) {
		new OnCreateOptionMenuModifier(method.getDeclaringClass(), method).modify();
	}

	private void modifyOnCreateContextMenu(SootMethod method) {
		new OnCreateContextMenuModifier(method.getDeclaringClass(), method).modify();
	}

	private void modifyClinitForActivity(SootMethod method, SootClass sc) {
		new ClinitForActivityModifier(sc, method).modify();
	}

	private void addClinitForActivity(final SootClass sc) {
		if (sc.equals(Global.v().getmActivity()))
			new Clinit(sc, Clinit.SUBSIGNATURE, true).build();
		else
			new Clinit(sc, Clinit.SUBSIGNATURE, false).build();
	}

	private void addOnCreateOptionsMenuMethod(SootClass sc) {
		OnCreateOptionsMenu optionMenu = new OnCreateOptionsMenu(sc,
				Constants.EHBMethod.onCreateOptionsMenu_activity);
		optionMenu.build();
	}

}
