package org.durmiendo.sueno.type.effects;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import org.durmiendo.sueno.entities.effect.StatusEntity;

public class SStatusEffect extends UnlockableContent {
    public String name;
    public TextureRegion effect;
    public float lifetime;
    //public Effect particle;

    public SStatusEffect(String name) {
        super(name);
        lifetime = 20f;
    }

    public void update(StatusEntity e) {
        e.lifetime -= Time.delta;
        if(e.lifetime <= 0) e.remove();
    }

    public void draw(StatusEntity e) {

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loadIcon() {
        super.loadIcon();
    }

    @Override
    public void load() {
        super.load();
        effect = Core.atlas.find(name);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.effect_UNUSED;
    }
}
