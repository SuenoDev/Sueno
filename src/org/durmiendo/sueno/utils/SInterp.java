package org.durmiendo.sueno.utils;

import arc.math.Interp;
import arc.math.Mathf;

import static arc.math.Mathf.*;

public class SInterp {
    public static Interp old = x -> {
        if (x < 0.1f) return sin(5f*pi*x)/2f;
        else return sqr(x-0.1f)/1.6f+0.5f;
    };
}
