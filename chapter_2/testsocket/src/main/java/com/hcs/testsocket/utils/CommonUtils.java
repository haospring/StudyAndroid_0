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

    public static String intToIp(int ip) {
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }

    public static long ipToInt(String ipAddress) {
        String[] ipAddressArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < Constants.IP_ARRAYS_LENGTH; i++) {
            long ip = Long.parseLong(ipAddressArray[i]);
            result |= (ip << i * 8);
        }

        return result;
    }
}
