package org.durmiendo.sueno.utils;

import arc.struct.IntMap;
import arc.util.Log;
import arc.util.Time;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;

public class SLog {
    public static int layer = 0;
    private static void log(String msg, Object... args) {
        Log.info(msg, args);
    }
    public static IntMap<String> data = new IntMap<>();

    public static void visi(String msg, int id) {
        data.put(id, msg);
    }
    public static void unvisi(int id) {
        data.remove(id);
    }

    public static void loadTime(Runnable runnable, String msg) {
        mark();
        runnable.run();
        elapsedInfo(msg);
    }

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

    public static void einfo(String msg) {
        if (SVars.extendedLogs) log("[cyan]@[white] @: " + msg, getVert(), getCaller());
    }


    public static void info(String msg) {
        log("[cyan]@[white] @: " + msg, getVert(), getCaller());
    }

    public static void info(String msg, Object... args) {
        log("[cyan]@[white] @: " + msg, getVert(), getCaller(), args);
    }

    public static void load(String msg) {
        if (SVars.extendedLogs) log("[cyan]@[white] @: loading @[gray]...[white]", getVert(), getCaller(), msg);
    }

    public static void load(String msg, Object... args) {
        if (SVars.extendedLogs) log("[cyan]@[white] @: loading @[gray]...[white]", getVert(), getCaller(), msg, args);
    }

    public static void elapsedInfo(String msg) {
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

    private static String getCaller() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        byte i = 0;
        for (StackTraceElement caller : trace) {
            i++;
            if (i < 4) continue;
            if (SLog.class.getName().equals(caller.getClassName())) continue;
            String[] classFullName = caller.getClassName().split("\\.");
            String className = classFullName[classFullName.length - 1];
            int line = caller.getLineNumber();
            return className + ":" + line;
        }
        return "unknown:??";
    }

    public static String getVert() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < layer; i++) {
            sb.append("| ");
        }
        return sb.toString();
    }
}