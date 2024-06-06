package org.durmiendo.sueno.type.effects;

import arc.graphics.g2d.TextureRegion;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;


public class SStatusEffect extends UnlockableContent {
    public String name;
    public TextureRegion effect;
    public float lifetime = 20f * 60f;
    public float limit = 100f;
    public float worth = 35f;
    public float subsides = 5f * 60f;

    public boolean show = true;

    public SStatusEffect(String name) {
        super(name);

    }

    public void update() {

    }

    public void created() {

    }

    @Override
    public void init() {}

    @Override
    public boolean isHidden() {
        return !show;
    }

    @Override
    public void setStats() {

    }

    @Override
    public boolean showUnlock() {
        return true;
    }

    public void draw() {

    }

    @Override
    public String toString() {
        return localizedName;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.status;
    }
}
