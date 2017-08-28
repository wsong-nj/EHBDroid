package com.app.test;
import soot.PackManager;
import soot.Transform;

public class AndroidInstrumentor{
	
	public void instrument(String[] args){
        transform(args);
	}
	
	public void transform(String[] args){
		PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new AppBodyTransformer()));
		try{
			soot.Main.main(args);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}