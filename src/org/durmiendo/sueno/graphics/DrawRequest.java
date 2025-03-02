package org.durmiendo.sueno.graphics;

import arc.graphics.Blending;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;

class DrawRequest implements Comparable<DrawRequest>{
    TextureRegion region = new TextureRegion();
    float x, y, z, originX, originY, width, height, rotation, color, mixColor;
    float[] vertices = new float[24];
    Texture texture;
    Blending blending;
    Runnable run;

    @Override
    public int compareTo(DrawRequest o){
        return Float.compare(z, o.z);
    }
}