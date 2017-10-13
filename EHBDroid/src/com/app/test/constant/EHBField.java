package com.app.test.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiangxingqian on 2017/10/13.
 */
public class EHBField {

    //the follow constants will be used as fields.
    public static Set<String> eventsLinkedList = new HashSet<>();
    public static Set<String> visited = new HashSet<>();

    //the following 6 field will be instrumented as fields.
    //Avoid to collect the same event. isVisited=1 means event has been collected.
    public static final String ISVISITED = "isVisited";

    //UI, system, inter-App events linkedList, will be instrumented in activity
    public static final String UIEVENTLINKEDLIST = "uieventlinkedlist";
    public static final String SYSTEMEVENTLINKEDLIST = "systemeventlinkedlist";
    public static final String INTERAPPEVENTLINKEDLIST = "interappeventlinkedlist";
    public static final String ACTIVITYMENU = "activityMenu";
    public static final String CONTEXTMENU = "contextMenu";

    //method coverage
    public static final String METHODCOUNTLIST = "methodCountList";

    static {
        eventsLinkedList.add(UIEVENTLINKEDLIST);
        eventsLinkedList.add(SYSTEMEVENTLINKEDLIST);
        eventsLinkedList.add(INTERAPPEVENTLINKEDLIST);

        visited.add(ISVISITED);
    }
}
