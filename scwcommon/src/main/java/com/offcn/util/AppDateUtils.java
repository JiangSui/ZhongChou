package com.offcn.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//日期处理类
public class AppDateUtils {
    //日期转字符串
    public static String dateToString(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getFormatTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String string = format.format(new Date());
        return string;
    }

    //不同格式 转换
    public static String getFormatTime(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(date);
        return string;
    }
}
