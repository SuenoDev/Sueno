package org.durmiendo.sueno.entities.effect;

import mindustry.gen.Building;
import org.durmiendo.sueno.type.effects.SStatusEffect;

public class StatusEntity {
    public float lifetime = 0f;
    public float progress = 0f;
    public SStatusEffect statusEffect;
    public Building owner;

    public void create() {
        progress = 0f;
    }

    public void remove() {

    }
}
