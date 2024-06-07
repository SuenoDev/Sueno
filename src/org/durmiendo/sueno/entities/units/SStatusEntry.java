package org.durmiendo.sueno.entities.units;

import org.durmiendo.sueno.type.effects.SStatusEffect;

public class SStatusEntry {
    public SStatusEffect effect;
    public float level;
    public boolean active = false;
    public float life = 0f;

    public SStatusEntry touch(float power){
        this.level -= power;
        if (level <= 0) activete();
        return this;
    }

    public void make(SStatusEffect effect){
        this.effect = effect;
        this.level = effect.limit;
        this.active = false;
        this.life = 0f;
    }

    public void activete() {
        active = true;
        life = effect.lifetime;
    }
}
