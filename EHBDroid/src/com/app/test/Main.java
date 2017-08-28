package com.app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ehb.analysis.CallGraphBuilder;
import ehb.global.EHBOptions;
import ehb.global.Global;
import ehb.xml.manifest.CallBackGenerator;
import ehb.xml.manifest.ProcessManifest;
import ehb.xml.resource.ProcessResource;
import ehb.xml.resource.ResourceAttributes;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.options.Options;

/**
 * good programming<--beauty<--perfect
 */
public class Main {

	// total number of CLASS, Method, LOC and act in app
	public static int totalMethod;
	public static int totalClass;
	public static int totalLine;
	public static int totalAct;

	public static String output = "output" + "/" + AppDir.APPNAME;
	public static String xmleventStoration = output + "/" + AppDir.XMLEVENT + ".txt";
	public static List<String> methods = new ArrayList<String>();

	public static void main(String[] args) {

		String params[] = { "-android-jars", "D:/SDK/platforms", "-process-dir",
				"L:/EHBbenchmarks/benchmark/" + AppDir.APPNAME + ".apk" };
		String apk = params[3];

		EHBOptions.v().setStrategy(AppDir.STGY);

		analyzeXML(apk);
		analyzeCode(apk, params);

		printEHBResult();
	}

	public static void analyzeXML(String apk) {

		ProcessManifest processMan = new ProcessManifest();
		processMan.loadManifestFile(apk);
		processMan.addToGlobal();

		ProcessResource processResource = new ProcessResource();
		processResource.loadResourceFile(apk);
		List<ResourceAttributes> resourceAttributes = processResource.getResources();

		CallBackGenerator generator = new CallBackGenerator(resourceAttributes);
		generator.generate();
		generator.addToGlobal();
	}

	private static void analyzeCode(String apk, String[] params) {

		CallGraphBuilder cgb = new CallGraphBuilder(apk);
		cgb.build();
		cgb.addToGlobal();
		G.reset();

		initSoot(params);

		instrumentApp(params);
	}

	// print app info
	private static void printEHBResult() {

		// write counts and print appInfos
		{
			String mocPath = output + "/" + Global.v().getPkg() + "_moc.txt";
			StringBuilder appInfos = new StringBuilder();
			String mainActName = "MainActivity: " + Global.v().getMainActivity();
			String acts = "All activities: " + Global.v().getActivities();
			StringBuilder idToCallback = new StringBuilder();
			for (int i : Global.v().getIdToCallBack().keySet())
				idToCallback.append("id: " + i + " methods: " + Global.v().getIdToCallBack().get(i) + "\n");
			int actCount = Global.v().getActivities().size();
			String counts = "Activity Count: " + actCount + " Method Count:" + totalMethod + " Class Count: "
					+ totalClass + " Line of Code:" + totalLine;
			writeToFile(mocPath, counts);

			appInfos.append(mainActName + "\n").append(acts + "\n").append(idToCallback).append(counts);
			System.out.println(appInfos.toString());
		}

		// write ehbevents.
		{
			StringBuilder sb = new StringBuilder();
			for (String s : Global.v().getEHBEventSet()) {
				sb.append(s + "\n");
			}
			String eventsPath = output + "/" + Global.v().getPkg() + "_EhbEvents.txt";
			writeToFile(eventsPath, sb.toString());
		}
		storeGlobals(xmleventStoration);
	}

	private static void instrumentApp(String[] params) {
		AndroidInstrumentor androidInstrument = new AndroidInstrumentor();
		androidInstrument.instrument(params);
	}

	/**
	 * store six elements:
	 * viewToCallBacks,activityToFilters,serviceToFilters,receiverToFilters,
	 * mainActivity,ehbstgy
	 * 
	 * @param location
	 *            serializarion object
	 */
	private static void storeGlobals(String location) {
		File file = new File(location);
		try {
			if (file.exists()) {
				file.delete();
				file = new File(location);
			}
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(CallBack.viewToCallBacks);
			oos.writeObject(Global.v().getActivityToFilters());
			oos.writeObject(Global.v().getServiceToFilters());
			oos.writeObject(Global.v().getReceiverToFilters());
			oos.writeObject(Global.v().getMainActivity());
			oos.writeObject(EHBOptions.v().getStrategy());
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param path
	 *            path to store msg
	 * @param msg
	 *            stored message
	 */
	private static void writeToFile(String path, String msg) {
		File file = new File(path);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(msg.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initSoot(String[] args) {
		Options.v().set_soot_classpath(args[3] + ";" + "lib/rt.jar;" + "lib/jce.jar;" + "lib/tools.jar;"
				+ "lib/android.jar;" + "lib/android-support-v4.jar;" + "bin");
		Options.v().set_validate(true);
		// Options.v().set_android_jars(args[1]);
		Options.v().set_src_prec(Options.src_prec_apk);
		 Options.v().set_output_format(Options.output_format_jimple);
//		Options.v().set_output_format(Options.output_format_dex);
		Options.v().set_output_dir(output);
		Options.v().set_allow_phantom_refs(true);

		for (String classAsSignature : CallGraphBuilder.getClassesAsSignature()) {
			Scene.v().addBasicClass(classAsSignature, SootClass.SIGNATURES);
		}
		for (String classAsBody : CallGraphBuilder.getApplicationClasses()) {
			Scene.v().addBasicClass(classAsBody, SootClass.BODIES);
		}
		for (String className : CallGraphBuilder.getEntrypoints()) {
			Scene.v().addBasicClass(className, SootClass.BODIES);
		}
		Scene.v().loadNecessaryClasses();
		
	}

	public static final Set<String> signCheckStmts = new HashSet<String>();

	static {
		signCheckStmts.add("void checkSignLegal(android.content.Context)");
	}

}
