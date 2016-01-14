package com.safe.safeguard.utils;

/**
 * Created by user on 2016/1/14.
 */
public class StringUtils {
    /**
     * 判断是否为空
     *
     * @param text
     * @return
     */
    public static boolean isNullOrEmpty(String text) {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0 || "null".equals(text.trim())) {
            return true;
        } else {
            return false;
        }
    }
}
