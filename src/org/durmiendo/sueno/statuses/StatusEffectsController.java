package org.durmiendo.sueno.statuses;

import arc.Events;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class StatusEffectsController {
    public ObjectMap<Bullet, StatusEffectBullet> bullets = new ObjectMap<>();
    public ObjectMap<Unit, StatusEffectEntry> effects = new ObjectMap<>();
    public ObjectMap<UnitType, Float> immunities = new ObjectMap<>();
    public float time;

    public StatusEffectsController() {
        Events.run(EventType.Trigger.update, this::update);
        Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            StatusEffectBullet entry = bullets.get(e.bullet);
            if (entry != null) {
                entry.effect.apply(e.unit);
            }
        });
    }

    public void init() {

    }


    public void update() {
        long startTime = Time.millis();

        time = Time.timeSinceMillis(startTime);
    }

    public float immunity(UnitType type) {
        if (!immunities.containsKey(type)) return type.health;
        return immunities.get(type);
    }

    public void apply(StatusEffectEntry entry, Unit u) {
        effects.put(u, entry);
    }
}
