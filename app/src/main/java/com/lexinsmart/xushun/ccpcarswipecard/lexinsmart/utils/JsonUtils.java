package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class JsonUtils {
    public static boolean isGoodJson(String json) {

        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }
}
