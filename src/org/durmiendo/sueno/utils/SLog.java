package org.durmiendo.sueno.utils;

import arc.util.Log;

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
}
