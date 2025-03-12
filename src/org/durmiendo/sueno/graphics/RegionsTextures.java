package org.durmiendo.sueno.graphics;

import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;

public class RegionsTextures extends TextureAtlas.AtlasRegion {
    public TextureRegion norm;
    public TextureRegion region;
    public RegionsTextures(TextureRegion region, TextureRegion norm) {
        super(region);
        this.norm = norm;
        this.region = region;
    }
}
