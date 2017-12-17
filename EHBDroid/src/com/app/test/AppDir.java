package com.app.test;

import java.util.LinkedList;

/**
 * This class will be instrumented into Android App.
 */
public class AppDir {

    public final static String APPNAME = "app-defaultConfig-outer";
    public final static String APP_PATH = "benchmark/" + AppDir.APPNAME + ".apk"; // Your app path
    public final static String XMLEVENT = APPNAME + "_xmlevents";
    public static final int CLASSCOUNT = 400;
    public final static String SDCARD = "/mnt/sdcard/";

    public static LinkedList<Integer> linkedList = new LinkedList<Integer>();

    static {
        for (int i = 0; i < 500; i++)
            linkedList.add(0);
    }

    public static String file = SDCARD + XMLEVENT + ".txt";

    public static int visitedMethodCount = 0;
}
