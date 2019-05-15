package com.android.toolkitlibrary.network.utils;

/**
 * 字符串
 * 
 * @author uu
 */
public class StringUtils {

    /**
     * return s == null || s.trim().length() == 0;
     */
    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

}
