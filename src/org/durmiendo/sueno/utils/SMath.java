package org.durmiendo.sueno.utils;

import arc.math.Mathf;
import arc.math.geom.Vec2;

public class SMath {
    public static Vec2 circlePointer(float x1, float y1, float r, float x2, float y2, Vec2 result) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        float len = Mathf.sqrt(Mathf.sqr(dx) + Mathf.sqr(dy));

        dx = dx / len;
        dy = dy / len;

        result.x = dx * r + x1;
        result.y = dy * r + y1;

        return result;
    }
}
