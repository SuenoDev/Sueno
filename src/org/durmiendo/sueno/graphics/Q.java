package org.durmiendo.sueno.graphics;


import arc.graphics.g2d.Draw;
import org.durmiendo.sueno.core.SVars;

public class Q {
    public static byte getCurrent() {
        return (byte) SVars.qval;
    }
    
    public static byte
        LOW = 1, MEDIUM = 2, HIGH = 3;

    
    public static boolean is(byte quality) {
        return quality == getCurrent();
    }
    
    public static void run(Runnable r) {
        float z = Draw.z();
        r.run();
        Draw.z(z);
    }
    
    public static void low(Runnable r) {
        if ( is(LOW)    ) run(r);
    }
    
    public static void medium(Runnable r) {
        if ( is(MEDIUM) ) run(r);
    }
    
    public static void high(Runnable r) {
        if ( is(HIGH)   ) run(r);
    }
    
    public static void multi(Runnable low, Runnable medium, Runnable high) {
        low(low);
        medium(medium);
        high(high);
    }
    
    public static void multi(Runnable low, Runnable high) {
        multi(low, low, high);
    }
}