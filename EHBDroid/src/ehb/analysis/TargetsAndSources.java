package ehb.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.jimple.toolkits.callgraph.Targets;
import ehb.global.Global;

public class TargetsAndSources {
	
	static Logger logger = Logger.getLogger("ehb.analysis.TargetsAndSources");
	
//	public static CallGraph Global.v().getCallGraph() = Global.v().getCallGraph();

	/**
	 * get target methods of sootMethod
	 * */
	public static List<SootMethod> getTargetsMethods(SootMethod sootMethod){
		List<SootMethod> tgtMethods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> targets = new Targets(Global.v().getCallGraph().edgesOutOf(sootMethod));
		boolean hasNext = targets.hasNext();
		logger.log(Level.WARNING, "SootMethod: "+sootMethod.getSignature()+" Has target? "+hasNext);
		while(targets.hasNext()){
			SootMethod m = (SootMethod)targets.next();
			logger.log(Level.WARNING, "SootMethod: "+sootMethod.getSignature()+" Target: "+hasNext);
			tgtMethods.add(m);
		}
		return tgtMethods;
	}
	
	/**
	 * get source methods of sootMethod
	 * */
	public static List<SootMethod> getSourcesMethods(SootMethod sootMethod){
		List<SootMethod> sMethods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> sources = new Sources(Global.v().getCallGraph().edgesInto(sootMethod));
		while(sources.hasNext()){
			SootMethod m = (SootMethod)sources.next();
			sMethods.add(m);
		}
		return sMethods;
	}
	
	/**
	 * get target edges of sootMethod
	 * */
	public static Iterator<Edge> getTargetEdges(SootMethod sootMethod) {
		return Global.v().getCallGraph().edgesOutOf(sootMethod);
	}
	
	/**
	 * get source edges of sootMethod
	 * */
	public static Iterator<Edge> getSourceEdges(SootMethod sootMethod) {
		return Global.v().getCallGraph().edgesInto(sootMethod);
	}
	
	/**
	 * get all source methods of sootMethod
	 * */
	public static List<SootMethod> getAllSourceMethods(SootMethod sm){
		List<SootMethod> preMethods = new ArrayList<SootMethod>();
		if(!preMethods.contains(sm)){
			preMethods.add(sm);
		}
		for(int i = 0;i<preMethods.size();i++){
			Iterator<MethodOrMethodContext> sources = new Sources(Global.v().getCallGraph().edgesInto(preMethods.get(i)));
			//Iterator it = Global.v().getCallGraph().edgesInto(m);
			while(sources.hasNext()){
				SootMethod sourceMethod = (SootMethod)sources.next();
				if(sourceMethod.getSignature().startsWith("<android")||sourceMethod.getSignature().startsWith("<java")||
						sourceMethod.getName().equals("dummyMainMethod"))
					continue;
				else if(!preMethods.contains(sourceMethod)){
					preMethods.add(sourceMethod);
				}
			}
		}		
		return preMethods;
	}
	
	/**
	 * get all target methods of sootMethod
	 * */
	public static List<SootMethod> getAllTargetMethods(SootMethod sm){
		List<SootMethod> sucMethods = new ArrayList<SootMethod>();
		//store Java, Android, Org and DummyMain methods
		Set<SootMethod> thirdPartyMethods = new HashSet<SootMethod>();
		if(!sucMethods.contains(sm)){
			sucMethods.add(sm);
		}
		for(int i = 0;i<sucMethods.size();i++){
			Set<SootMethod> targets = getTargets(sm);
			for(SootMethod sourceMethod:targets){
//				logger.log(Level.WARNING, "SootMethod: "+sm.getSignature()+" Target: "+sourceMethod.getSignature());
				if(sourceMethod.getSignature().startsWith("<android")||sourceMethod.getSignature().startsWith("<java")){
					thirdPartyMethods.add(sourceMethod);
					continue;
				}else if(!sucMethods.contains(sourceMethod)){
					sucMethods.add(sourceMethod);
				}
			}
		}
		sucMethods.addAll(thirdPartyMethods);
		return sucMethods;
	}
	
//	public static List<Edge> getEdges(SootMethod sm){
//		Iterator<Edge> iterator = Global.v().getCallGraph().iterator();
//		List<Edge> edges = new ArrayList<>();
//		while (iterator.hasNext()) {
//			Edge edge = (Edge) iterator.next();
//			SootMethod src = edge.src();
//			if(src.getSignature().equals(sm.getSignature())){
//				edges.add(edge);
//				logger.log(Level.WARNING, "Edge: "+edge.src().getSignature()+" To: "+edge.tgt().getSignature());
//			}
//		}
//		return edges;
//	}
	
	public static Set<SootMethod> getTargets(SootMethod sm){
		Iterator<Edge> iterator = Global.v().getCallGraph().iterator();
		Set<SootMethod> tgts = new HashSet<>();
		String signature = sm.getSignature();
		while (iterator.hasNext()) {
			Edge edge = (Edge) iterator.next();
			SootMethod src = edge.src();
			if(src.getSignature().equals(signature)){
				tgts.add(edge.tgt());
//				logger.log(Level.WARNING, "Edge: "+edge.src().getSignature()+" To: "+edge.tgt().getSignature());
			}
		}
		return tgts;
	}
	
}
