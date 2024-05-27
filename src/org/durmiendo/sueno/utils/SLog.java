package org.durmiendo.sueno.utils;

import arc.util.Log;
import arc.util.Time;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;

public class SLog {
    public static int layer = 0;

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
        line();
    }

    public static void error(String msg) {
        line();
        Log.info("[yellow]Sueno[white]: [red]Error[white] " + msg);
        line();
    }

    public static void info(String msg) {
        Log.info("[cyan]@  [white] " + msg, getVert());
    }

    public static void info(String msg, Object... args) {
        Log.info("[cyan]@  [white] " + msg, getVert(), args);
    }

    public static void einfo(String msg) {
        if (SVars.extendedLogs) Log.info("[cyan]@[white] " + msg, getVert());
    }

    public static void einfo(String msg, Object... args) {
        if (SVars.extendedLogs) Log.info("[cyan]@[white] " + msg, getVert(), args);
    }

    public static void load(String msg) {
        if (SVars.extendedLogs) Log.info("[cyan]@[white] loading @...", getVert(), msg);
    }

    public static void load(String msg, Object... args) {
        if (SVars.extendedLogs) Log.info("[cyan]@[white] loading @...", getVert(), msg, args);
    }

    public static void einfoElapsed(String msg) {
        if (SVars.extendedLogs) {
            Log.info("[cyan]@[white]", getVert());
            layer-=1;
            Log.info("[cyan]@`->[white] " + msg + "[gray], load time is [orange]" + Time.elapsed() + "ms[white]", getVert());
        }
    }

    public static void mark() {
        if (SVars.extendedLogs) {
            line();
            Time.mark();
        }
    }

    public static void line() {
        if (SVars.extendedLogs) {
            if (layer == 0) Log.info("[cyan],---------------<  [yellow]Sueno[cyan]  >-------------------[white]");
            if (layer > 0) Log.info("[cyan]@,----------------------------------[white]", getVert());
            layer+=1;
        }
    }

    public static String getVert() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < layer; i++) {
            sb.append("| ");
        }
        return sb.toString();
    }
}