package org.durmiendo.sueno.utils;

import arc.util.Strings;

public class SStrings {

    public static StringBuilder fixed(float value, int decimals) {
        StringBuilder sb = Strings.fixedBuilder(value, decimals);
        for (int i = sb.length() - 1; i >= 1; i--) {
            if (sb.charAt(i) == '.') {
                sb.deleteCharAt(i);
                break;
            }
            if (sb.charAt(i) != '0') {
                break;
            }
            sb.deleteCharAt(i);
        }
        return sb;
    }
}
