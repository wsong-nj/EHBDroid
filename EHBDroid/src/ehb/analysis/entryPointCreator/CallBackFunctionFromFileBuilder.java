package ehb.analysis.entryPointCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

//from AndroidCallbacks.txt
public class CallBackFunctionFromFileBuilder extends CallBackFunctionBuilder{
	
	File file;
	public CallBackFunctionFromFileBuilder(File file) {
		this.file = file;
	}
	
	public void buildCallBackFunctions(){
		Chain<SootClass> chain = Scene.v().getClasses();
		List<SootClass> usedCallbacks = getUsedCallbacks(chain);
		for(SootClass usedCallback:usedCallbacks){
	        List<SootClass> implementerClasses = Scene.v().getActiveHierarchy().getImplementersOf(usedCallback);
			for(SootClass implementerClass:implementerClasses){
				if(implementerClass.getName().startsWith("android")||implementerClass.getName().startsWith("java"))
					continue;
				List<SootMethod> instanceMethods = implementerClass.getMethods();
				List<String> list = new ArrayList<String>();
				for(SootMethod callbackMethod:instanceMethods){
					List<String> subSignitures = getSubSignitures(usedCallback.getMethods());
					if(subSignitures.contains(callbackMethod.getSubSignature())){
						list.add(callbackMethod.getSignature());
					}
				}
				if(list.size()>0)
					callbackFunctions.put(implementerClass.getName(),list);
			}
		}
	}

	private List<SootClass> getUsedCallbacks(Chain<SootClass> chain) {
        Set<String> callbacks = getCallbacks(file);
        List<SootClass> classes = new ArrayList<>();
		for(SootClass sc:chain){
			if(callbacks.contains(sc.getName())){
				if(sc.getMethods().size()>0){
					if(!sc.isInterface()){
						continue;
					}
					classes.add(sc);
				}
			}
		}
		return classes;
	}
	
	private List<String> getSubSignitures(List<SootMethod> methods) {
		List<String> sigs = new ArrayList<>();
		for(SootMethod method:methods){
			sigs.add(method.getSubSignature());
		}
		return sigs;
	}
	
	public Set<String> getCallbacks(File file){
		Set<String> callbacks = new HashSet<String>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s=br.readLine())!=null){
				callbacks.add(s);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callbacks;
	}
}
