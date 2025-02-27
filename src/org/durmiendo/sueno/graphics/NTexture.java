package org.durmiendo.sueno.graphics;

import arc.graphics.Texture;

public class NTexture extends Texture {
    public Texture normal;
    public Texture base;

    public NTexture(Texture base, Texture normal) {
        super();
        this.normal = normal;
        this.base = base;
    }
}
