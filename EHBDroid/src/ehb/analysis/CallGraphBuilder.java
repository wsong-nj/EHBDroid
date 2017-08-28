package ehb.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import com.app.test.AppDir;
import com.app.test.CallBack;
import com.app.test.Constants;
import com.app.test.MathUtil;
import com.app.test.Util;
import com.app.test.data.AndroidIntentFilter;
import com.app.test.data.PatternMatcher;
import com.app.test.event.InterAppEvent;
import com.app.test.event.InterAppEventHandler;
import com.app.test.event.ReceiverEvent;
import com.app.test.event.SystemEvent;
import com.app.test.event.SystemEventConstants;
import com.app.test.event.SystemEventHandler;
import com.app.test.event.UIEvent;
import com.app.test.event.UIEventHandler;

import ehb.analysis.entryPointCreator.AndroidEntryPointCreator;
import ehb.analysis.entryPointCreator.CallBackFunctionFromFileBuilder;
import ehb.analysis.entryPointCreator.CallBackFunctionFromXMLBuilder;
import ehb.global.EHBOptions;
import ehb.global.Global;
import ehb.global.GlobalHost;
import ehb.instrumentation.codecoverage.CoverageToolkit;
import ehb.xml.manifest.ProcessManifest;

/**
 * 几乎用不到
 * */
public class CallGraphBuilder implements GlobalHost{
	
	public static HashSet<String> entrypoints;
	public static Set<String> classesAsSignature = new HashSet<>();
	public static Set<String> applicationClasses= new HashSet<>();
	
	//class as SootClass.SIGNATURES and SootClass.BODIES
	static{
		classesAsSignature.add("java.io.PrintStream");
        classesAsSignature.add("java.lang.System");
        classesAsSignature.add("java.util.LinkedList");
        classesAsSignature.add("java.lang.reflect.Field");
        classesAsSignature.add("java.lang.reflect.Method");
        classesAsSignature.add("java.lang.Class");
        classesAsSignature.add("java.lang.Boolean");
        classesAsSignature.add("java.lang.Exception");
        classesAsSignature.add("java.lang.Throwable");
        classesAsSignature.add("java.util.AbstractCollection");
        classesAsSignature.add("java.lang.Object");
        classesAsSignature.add("java.lang.Thread");
        classesAsSignature.add("java.lang.StringBuilder");
        classesAsSignature.add("javax.crypto.KeyGenerator");
        
        classesAsSignature.add("android.widget.Toast");
        classesAsSignature.add("android.app.ListActivity");
        classesAsSignature.add("android.widget.Toast");
        classesAsSignature.add("android.app.Activity");
        classesAsSignature.add("android.view.Menu");
        classesAsSignature.add("android.content.Intent");
        classesAsSignature.add("android.util.Log");
        classesAsSignature.add("android.app.Application");
        classesAsSignature.add("android.app.Application$ActivityLifecycleCallbacks");
        classesAsSignature.add("android.app.Application$OnProvideAssistDataListener");
        classesAsSignature.add("android.app.IntentService");
        classesAsSignature.add("android.content.ContentProvider");
        classesAsSignature.add("android.database.CharArrayBuffer");

        applicationClasses.add("android.app.Dialog");
        applicationClasses.add("android.view.MenuItem");
        applicationClasses.add("android.view.View");
        applicationClasses.add("android.content.Context");
        applicationClasses.add("android.view.MenuItem$OnMenuItemClickListener");
        applicationClasses.add(CallBack.class.getName());
        applicationClasses.add(Util.class.getName());
        applicationClasses.add(CoverageToolkit.class.getName());
//      applicationClasses.add(IEventHandler.class.getName());
        applicationClasses.add(UIEventHandler.class.getName());
        applicationClasses.add(UIEventHandler.UIEventTesterForSeq.class.getName());
        applicationClasses.add(SystemEventHandler.class.getName());
		applicationClasses.add(InterAppEventHandler.class.getName());
		applicationClasses.add(UIEventHandler.UIEventTesterForSingleEvent.class.getName());
//		applicationClasses.add(AbstractEventHandler.AppMenuItemClickListener.class.getName());
		applicationClasses.add("com.app.test.event.SystemEventHandler$1");
		applicationClasses.add("com.app.test.event.UIEventHandler$1");
		applicationClasses.add("com.app.test.event.InterAppEventHandler$1");
		applicationClasses.add(SystemEventConstants.class.getName());
		applicationClasses.add(UIEvent.class.getName());
		applicationClasses.add(InterAppEvent.class.getName());
		applicationClasses.add(SystemEvent.class.getName());
		applicationClasses.add(ReceiverEvent.class.getName());
		applicationClasses.add(AppDir.class.getName());
		applicationClasses.add(AndroidIntentFilter.class.getName());
		applicationClasses.add(AndroidIntentFilter.AuthorityEntry.class.getName());
		applicationClasses.add(Constants.LogTag.class.getName());
		applicationClasses.add(Constants.EHBField.class.getName());
		applicationClasses.add(Constants.EHBClass.class.getName());
		applicationClasses.add(Constants.EHBMethod.class.getName());
		applicationClasses.add(PatternMatcher.class.getName());
		applicationClasses.add(MathUtil.class.getName());
	}

	private String apkFileLocation; 
	public CallGraphBuilder(String apkFileLocation) {	
		this.apkFileLocation = apkFileLocation;
	}

	public void initSoot(){
		Options.v().set_soot_classpath(apkFileLocation+";"+
				"lib/rt.jar;" +
				"lib/jce.jar;" +
				"lib/tools.jar;" +
				"lib/android.jar;"+
				"lib/android-support-v4.jar;"+
				"bin");	
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_no_output_source_file_attribute(true);		
		Options.v().set_whole_program(true);
	
		for(String classAsSignature:classesAsSignature){
			Scene.v().addBasicClass(classAsSignature,SootClass.SIGNATURES);
		}
		for(String classAsBody:applicationClasses){
			Scene.v().addBasicClass(classAsBody,SootClass.BODIES);
		}
		
		SootMethod entry = buildDummyMainMethod(); 
//		Scene.v().setEntryPoints(Collections.singletonList(entry));
		Scene.v().addBasicClass(entry.getDeclaringClass().getName(), SootClass.BODIES);	
		for (String className : entrypoints){		
			Scene.v().addBasicClass(className, SootClass.BODIES);	
		}		
		Scene.v().loadNecessaryClasses();
		
		if(EHBOptions.v().isStaticAnalysis()){
			Map<String, List<String>> buildCallBackFunctions = buildCallBackFunctions();
			entrypoints.addAll(buildCallBackFunctions.keySet());
			SootMethod entry2 = buildDummyMainMethod(buildCallBackFunctions); 
			Scene.v().setEntryPoints(Collections.singletonList(entry2));
			CHATransformer.v().transform();
		}
//		Global.v().setCallGraph(Scene.v().getCallGraph());
	}

	private Map<String, List<String>> buildCallBackFunctions() {
		Map<String, List<String>> callbackFunctions;
		//from AndroidCallbacks.txt
		CallBackFunctionFromFileBuilder callBackFunctionsBuilder = new CallBackFunctionFromFileBuilder(new File("AndroidCallbacks.txt"));
		callBackFunctionsBuilder.build();
		callbackFunctions = callBackFunctionsBuilder.getCallbackFunctions();
		
		//from XML
		CallBackFunctionFromXMLBuilder CallBackFunctionFromXMLBuilder = new CallBackFunctionFromXMLBuilder(Global.v().getIdToCallBack());
		CallBackFunctionFromXMLBuilder.build();
		callbackFunctions.putAll(CallBackFunctionFromXMLBuilder.getCallbackFunctions());
		return callbackFunctions;
	}

	private SootMethod buildDummyMainMethod() {
		AndroidEntryPointCreator firstEntryPointCreator = createEntryPointCreator();
		return firstEntryPointCreator.createDummyMain();	
	}
	
	private SootMethod buildDummyMainMethod(Map<String, List<String>> callBackFunctions) {
		AndroidEntryPointCreator creator = new AndroidEntryPointCreator
				(new ArrayList<String>(entrypoints));
		creator.setCallbackFunctions(callBackFunctions);
		return creator.createDummyMain();	
	}
	
	private AndroidEntryPointCreator createEntryPointCreator() {
		ProcessManifest processMan = new ProcessManifest();
		processMan.loadManifestFile(apkFileLocation);
		entrypoints = processMan.getEntryPointClasses();	
		AndroidEntryPointCreator entryPointCreator = new AndroidEntryPointCreator
			(new ArrayList<String>(entrypoints));
		return entryPointCreator;
	}
	
	public Set<String> getEntryPoint(){
		return entrypoints;
	}

	public void build() {
		initSoot();
	}

	@Override
	public void addToGlobal() {
		if(Scene.v().hasCallGraph()){
			Global.v().setCallGraph(Scene.v().getCallGraph());
		}
	}
	
	public static HashSet<String> getEntrypoints() {
		return entrypoints;
	}

	public String getApkFileLocation() {
		return apkFileLocation;
	}

	public static Set<String> getClassesAsSignature() {
		return classesAsSignature;
	}

	public static Set<String> getApplicationClasses() {
		return applicationClasses;
	}
	
}
