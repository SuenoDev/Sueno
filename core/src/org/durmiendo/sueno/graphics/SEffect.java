package org.durmiendo.sueno.graphics;

import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.util.Time;
import mindustry.graphics.Layer;

public class SEffect {
    public Cons<Container> renderer;
    public float lifeTime;

    public SEffect(float lifeTime, Cons<Container> renderer) {
        this.renderer = renderer;
        this.lifeTime = lifeTime;
    }

    public Container at(float x, float y) {
        Container container = new Container();
        container.x = x;
        container.y = y;
        container.effect = this;
        return container;
    }

    public class Container {
        public float life = 0f;
        public SEffect effect;
        public float x = 0, y = 0;

        public boolean render() {
            renderer.get(this);

            life += Time.delta;
            return life > lifeTime;
        }
    }
}
