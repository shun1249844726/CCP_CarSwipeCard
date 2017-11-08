package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

/**
 * Created by xushun on 2017/11/7.
 * 功能描述：
 * 心情：
 */

public class StringUtils {
    public static String checkIsNull(String instr){
        if (instr == null){
            return "";
        }else {
            return instr;
        }
    }

    public static String getImeIlast5(String iemi) {
        String last5 = "";
        if (iemi.length() > 5) {
            last5 = iemi.substring(iemi.length() - 5, iemi.length());
        }

        return last5;

    }
}
