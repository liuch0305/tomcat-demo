package com.lch.tomcat.util;

/**
 * @author: liuchenhui
 * @create: 2019-12-18 19:59
 **/
public class StringUtil {

    public static String parseToString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String[]) {
            String[] objt = (String[]) obj;
            StringBuilder str = new StringBuilder();
            for (String s : objt) {
                str.append(s + ",");
            }
            return str.toString().substring(0, str.length() - 1);
        } else {
            obj.toString();
        }
        return "";
    }
}
