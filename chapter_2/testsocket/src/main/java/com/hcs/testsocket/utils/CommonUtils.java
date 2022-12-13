package com.hcs.testsocket.utils;

public class CommonUtils {
    public static String bytesToHexString(byte[] bytes, int offset, int len) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = offset; i < len; ++i) {
            result.append("0x");
            String hex = Integer.toHexString(bytes[i] & Constants.OXFF).toUpperCase();
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            result.append(hex);
            if (i < len - 1) {
                result.append(Constants.STRING_SEPARATOR + Constants.STRING_SPACE);
            }
        }
        result.append("]");
        return result.toString();
    }
}
