package org.durmiendo.sueno.math;

import java.util.Objects; // Для удобства в equals/hashCode

public class Vec4 {
    public float x, y, z, w;
    
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4 vec4 = (Vec4) o;
        float epsilon = 0.00001f;
        return Math.abs(vec4.x - x) < epsilon &&
                Math.abs(vec4.y - y) < epsilon &&
                Math.abs(vec4.z - z) < epsilon &&
                Math.abs(vec4.w - w) < epsilon;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}