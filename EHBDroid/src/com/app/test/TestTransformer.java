package com.app.test;

import soot.Body;
import soot.BodyTransformer;

import java.util.Map;

/**
 * Created by xiangxingqian on 2017/10/11.
 */
public class TestTransformer extends BodyTransformer {
    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        //do nothing
    }
}
