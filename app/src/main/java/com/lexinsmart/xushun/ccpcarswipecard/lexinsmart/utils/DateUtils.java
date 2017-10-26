package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
	/**
	 * String ->Timestamp
	 * String tsStr = "2011-05-09 11:49:45";  
	 * @param tsStr
	 * @return
	 */
	public static Timestamp StringToTimestamp(String tsStr){
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
        try {  
            ts = Timestamp.valueOf(tsStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return ts;
	}
	/**
	 * string -> date
	 * yyyy-MM-dd
	 * @param dateStr
	 * @return
	 */
	public static Date StringToDate(String dateStr){
        Date date = new Date();  
        //注意format的格式要与日期String的格式相匹配  
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        try {  
            date = sdf.parse(dateStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return date;  
	}

	/**
	 * 获取指定字符串出现的次数
	 * 
	 * @param srcText 源字符串
	 * @param findText 要查找的字符串
	 * @return
	 */
	public static int appearNumber(String srcText, String findText) {
	    int count = 0;
	    Pattern p = Pattern.compile(findText);
	    Matcher m = p.matcher(srcText);
	    while (m.find()) {
	        count++;
	    }
	    return count;
	}
}
