package com.fitech.framework.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 石昊东 2011-4-29
 * 
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     * 
     * @param str
     * @return str为空返回true,list不为空返回false
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 判断字符串数组是否为空
     * 
     * @param str
     * @return str为空返回true,list不为空返回false
     */
    public static boolean isEmpty(String[] strs) {
        if (strs == null || strs.length == 0 || (strs.length == 1 && (strs[0] == null || strs[0].equals("")))) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 判断是否为数字
     * 
     * @param str 传入的字符串
     * @return 是数字返回true,否则返回false
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        return pattern.matcher(str.trim()).matches();
    }

    /**
     * 获取字符串中的第一个数字
     * 
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 获取字符串中的第一个数字
     * 
     * @param content
     * @return
     */
    public static String getAlpNumbers(String content) {
        Pattern pattern = Pattern.compile("[a-zA-Z\\d]+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 获取字符串的第一串非数字字符串
     * 
     * @param content
     * @return
     */
    public static String getNotNumberStr(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 效验字符长度 中文2个、英文1个（不能判断其它国语言）
     * 
     * @param value 需要判断的值
     * @return int 返回值的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            }
            else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static void main(String[] args) {
        String n = getNumbers("<=19.93%");
        String s = getNotNumberStr("<=19.93%");
        System.out.println(n);
        System.out.println(s);
    }
}
