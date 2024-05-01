package org.durmiendo.sueno.entities.effect;

import mindustry.ctype.Content;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Unit;
import org.durmiendo.sueno.gen.IndexableEntity__all;
import org.durmiendo.sueno.gen.IndexableEntity__draw;
import org.durmiendo.sueno.gen.IndexableEntity__sync;
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
