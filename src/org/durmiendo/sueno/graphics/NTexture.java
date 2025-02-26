package org.durmiendo.sueno.graphics;

import arc.graphics.Texture;

public class NTexture extends Texture {
    public Texture normal;

    public NTexture(Texture base, Texture normal) {
        super();
        this.normal = normal;

        load(base.getTextureData());
    }

    @Override
    public void bind(int unit) {
        normal.bind(4);
        super.bind(unit);

    }

    @Override
    public void bind() {
        normal.bind(4);
        super.bind(glTarget);
    }
}
