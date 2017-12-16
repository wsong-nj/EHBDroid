package com.app.test;

import java.util.Map;

/**
 * Created by xiangxingqian on 2017/12/16.
 */
public class Test {

    public static void main(String[] args){
        Map<String, String> map = System.getenv();
        for(String s:map.keySet()){
            Object o = map.get(s);
            System.out.println("main " + " Line14 " +s+" "+o);
        }
        System.out.println("main " + " Line16 " +map.get("ANDROID_HOME"));
    }
}
