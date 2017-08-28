package com.app.test;

import java.util.LinkedList;

import com.app.test.event.UIEventHandler;


/**
 * This class will be instrumented into Android App.
 * */
public class AppDir {
	
	public static final int CLASSCOUNT = 400;//如果是小app，则class总数为400
	public final static String APPNAME = "yefu";
	
	//Event declared in XML
	public final static String XMLEVENT = APPNAME+"_xmlevents";
	
	public final static String SDCARD = "/mnt/sdcard/";

	// Testing Strategy
	public final static String STGY = UIEventHandler.ONESEQ;
	
	public static LinkedList<Integer> linkedList = new LinkedList<Integer>();
	static{
		for(int i =0;i<500;i++)
			linkedList.add(0);
	}
	
	public static String file = SDCARD+XMLEVENT+".txt";
	
	public static int visitedMethodCount = 0;
	
//	public static void  main(String[] args) {
//		File f = new File("L:/EHBbenchmarks/benchmark");
//		String files = "";
//		for(String s:f.list()){
//			files = files+"\",\""+s.substring(0,s.indexOf("."));
//		}
//		System.out.println(files);
//	}
}
