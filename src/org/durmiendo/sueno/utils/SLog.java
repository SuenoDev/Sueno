package org.durmiendo.sueno.utils;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;

public class SLog {

    public static void infoFld(Object o, String field) {
        Class c = o.getClass();
        try {
            Field f = c.getField(field);
            Log.info(field + " " + f.get(o));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }


    public static void infoFlds(Object o, String... field) {
        for (String f : field) {
            infoFld(o, f);
        }
        Log.info("\n--------------------");
    }

    public static void error(String msg) {
        Log.info("----------------------");
        Log.info("Sueno Error: " + msg);
        Log.info("----------------------");
    }

    public static void info(String msg) {
        Log.info("Sueno info: " + msg);
    }
    public static void info(String msg, Object... args) {
        Log.info("Sueno info: " + msg, args);
    }

    public static void debug(String msg) {
        if (SVars.debug) Log.info("Sueno debug: " + msg);
    }
}
