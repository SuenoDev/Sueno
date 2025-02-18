package org.durmiendo.sueno.utils;

import arc.Events;
import arc.struct.IntMap;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import mindustry.game.EventType;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;

public class SLog {
    public static void init() {
        Events.run(EventType.Trigger.update, () -> {
            timer -= Time.delta;
        });
    }
    public static int layer = 0;

    private static void log(String msg, Object... args) {
        if (SVars.extendedLogs) Log.info(msg, args);
    }

    public static IntMap<String> data = new IntMap<>();

    public static void visi(String msg, int id) {
        data.put(id, msg);
    }
    public static void unvisi(int id) {
        data.remove(id);
    }

    public static float timer;
    public static void tInfo(String msg, float time) {
        if (timer <= 0) {
            info(msg);
            timer = time;
        }
    }

    public static void tInfo(Object msg, float time) {
        if (timer <= 0) {
            info(msg.toString());
            timer = time;
        }
    }

    public static void dInfo(Object msg) {
        if (timer <= 0) {
            info(msg.toString());
            timer = 3;
        }
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
            log(field + " " + f.get(o));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }


    public static void infoFlds(Object o, String... field) {
        for (String f : field) {
            infoFld(o, f);
        }
        line();
    }

    public static void einfo(String msg) {
        log("[cyan]@[white] @: " + msg, getVert(), getCaller());
    }


    public static void info(String msg) {
        log("[cyan]@[white] @: " + msg, getVert(), getCaller());
    }

    public static void info(String msg, Object... args) {
        log("[cyan]@[white] @: " + msg, getVert(), getCaller(), args);
    }

    public static void load(String msg) {
        log("[cyan]@[white] @: loading @[gray]...[white]", getVert(), getCaller(), msg);
    }

    public static void load(String msg, Object... args) {
        log("[cyan]@[white] @: loading @[gray]...[white]", getVert(), getCaller(), msg, args);
    }

    public static void elapsedInfo(String msg) {
        log("[cyan]@[white]", getVert());
        layer-=1;
        log("[cyan]@`->[white] " + msg + "[gray], load time is [orange]" + Time.elapsed() + "ms[white]", getVert());
    }

    public static void err(String msg) {
        String err = Strings.format("[red]@: @", getCaller(), msg);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < err.length()-4; i++) {
            b.append("=");
        }
        String a = b.toString();
        log("[red]@[white]", a);
        log(err + "[white]");
        log("[red]@[white]", a);
    }

    public static void mark() {
        line();
        Time.mark();
    }

    public static void line() {
        if (layer == 0) {
            log("[cyan],---<  [yellow]Sueno[white]");
        }
        if (layer > 0) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < 40 - layer*2; i++) {
                b.append("-");
            }
            String a = b.toString();
            log("[cyan]@,@[white]", getVert(), a);
        }
        layer+=1;
    }

    private static String getCaller() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        short i = 0;
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