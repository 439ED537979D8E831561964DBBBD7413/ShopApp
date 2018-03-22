package com.yj.shopapp.util;

/**
 * 字符串助手
 */
public class StringHelper {

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 全角转换成半角
     *
     * @param input 原始字符串
     * @return 转换后的字符串
     */
    public static String toBanJiao(String input) {
        if (isEmpty(input) == true) {
            return input;
        } else {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == '\u3000') {
                    c[i] = ' ';
                } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            return new String(c);
        }
    }

    /**
     * 半角转换成全角
     *
     * @param input 原始字符串
     * @return 转换后的字符串
     */
    public static String toQuanJiao(String input) {
        if (isEmpty(input) == true) {
            return input;
        } else {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == ' ') {
                    c[i] = '\u3000';
                } else if (c[i] < '\177') {
                    c[i] = (char) (c[i] + 65248);
                }
            }
            return new String(c);
        }
    }

    /**
     * 判断电话逻辑，直接改成判断11位，之前的正则无法判断147,177号段
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        return mobiles.matches("^1[0-9]{10}");
    }

    public static boolean isSalesTime(String str) {
        if (isEmpty(str)) {
            return true;
        }
        if (DateUtils.ToDate(str, "yyyy-MM-dd")) {
            return true;
        }
        return false;
    }

    /**
     * 去除头尾中间空格后两string是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isequal(String a, String b) {
        if (a.trim().replace(" ", "").equals(b.trim().replace(" ", ""))) {
            return true;
        }
        return false;
    }

}
