package org.durmiendo.sueno.graphics;

import arc.graphics.Color;
import mindustry.entities.Effect;
import mindustry.gen.Bullet;

public class SCEffect extends Effect {
    @Override
    public float render(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data) {

        return super.render(id, color, life, lifetime, rotation, x, y, data);
    }

    public static class EffectContainer extends Effect.EffectContainer {
        public Bullet b;

        @Override
        public void set(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data) {
            super.set(id, color, life, lifetime, rotation, x, y, data);
        }
    }
}
