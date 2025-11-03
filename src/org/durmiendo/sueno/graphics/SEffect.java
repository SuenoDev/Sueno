package org.durmiendo.sueno.graphics;

import arc.func.Cons;
import arc.util.Time;
import mindustry.Vars;

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

    public Container at(float x, float y, float r) {
        Container container = new Container();
        container.x = x;
        container.y = y;
        container.effect = this;
        container.rad = r;
        return container;
    }

    public class Container {
        public float life = 0f;
        public SEffect effect;
        public float x = 0, y = 0;
        public float rad = 1f;

        public boolean render() {
            renderer.get(this);

            if (Vars.state.isPlaying())
                life += Time.delta;
            return life > lifeTime;
        }
    }
}
