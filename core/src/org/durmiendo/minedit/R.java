package org.durmiendo.minedit;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.ctype.Content;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class R {
    public static String getName(Field fi) {
        String r = null;
        try {
            fi.setAccessible(true);
            r = fi.getName();
            fi.setAccessible(false);
        } catch (Exception e) {
            Log.info("getName error: " + e);
        }
        return r;
    }
    public static Class getType(Field fi) {
        Class r = null;
        try {
            fi.setAccessible(true);
            r = fi.getType();
            fi.setAccessible(false);
        } catch (Exception e) {
            Log.info("getType error: " + e);
        }
        return r;
    }
    public static Object getField(Field fi, Object obj) {
        Object r = null;
        try {
            fi.setAccessible(true);
            r = fi.get(obj);
            fi.setAccessible(false);
        } catch (Exception e) {
            Log.info("getField error: " + obj.toString() + " " + getName(fi) + " " + e);
        }
        if (getType(fi).isPrimitive()) return r;
        Constructor c = null;
        Class<?> parent = obj.getClass().getSuperclass();
        while (parent != null) {
            if (parent == Object.class) break;
            if (parent == Comparable.class) break;
            try {
                c = parent.getDeclaredConstructor(null);
            } catch (NoSuchMethodException e) {}
            parent = parent.getSuperclass();
            if (parent == Content.class) break;
        }
        try {
            if (c == null) return r;
            r = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Log.info("getField error: " + obj.toString() + " " + getName(fi) + " " + e);
        }
        return r;
    }
    public static void setField(Field fi, Object obj, Object value) {
        try {
            fi.setAccessible(true);
            fi.set(obj, value);
            fi.setAccessible(false);
        } catch (IllegalAccessException e) {
            Log.info("setField error: " + obj.toString() + " " + getName(fi) + " " + e);
        }
    }

    public static <T> T cloneObject(T object) {
        try {
            Class<T> clazz = (Class<T>) object.getClass();
            T copy = Reflect.invoke(clazz, "clone");
            return copy;
        } catch (Exception e) {
            Log.info("Clone error: " + object.toString() + " " + e);
        }
        return null;
    }

    public static Seq<Field> getFields(Class<?> clazz) {
        Seq<Field> fields = new Seq<>(clazz.getDeclaredFields());
        Class<?> parent = clazz.getSuperclass();
        while (parent != null) {
            fields.addAll(parent.getDeclaredFields());
            parent = parent.getSuperclass();
        }
        return fields;
    }
}
