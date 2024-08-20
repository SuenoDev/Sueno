package org.durmiendo.sueno.processors;

import arc.Core;
import arc.input.InputProcessor;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import mindustry.Vars;
import org.durmiendo.sueno.math.area.Area;


public class SuenoInputProcessor implements InputProcessor {
    public ObjectMap<Area, WorldListener> listeners = new ObjectMap<>();
    public ObjectSet<WorldListener> active = new ObjectSet<>();
    public Vec2 tmp = new Vec2();

    public void addListener(Area area, WorldListener listener) {
        listeners.put(area, listener);
    }

    public void removeListener(Area area) {
        listeners.remove(area);
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, KeyCode button) {
        tmp = Core.input.mouseWorld(screenX, screenY);
//        SLog.info("down x: " + tmp.x + " y: " + tmp.y);
        if (!active.isEmpty()) {
            Vars.player.shooting(false);
        }
        listeners.forEach(l -> {
            if (l.key.insidePoint(tmp.x, tmp.y)) {
                active.add(l.value);
                l.value.apply(tmp.x, tmp.y, (byte) 1);
            }
        });
        return InputProcessor.super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, KeyCode button) {
        tmp = Core.input.mouseWorld(screenX, screenY);
//        SLog.info("up x: " + tmp.x + " y: " + tmp.y);
        active.each(l -> {
            l.apply(tmp.x, tmp.y, (byte) 2);
        });
        active.clear();
        return InputProcessor.super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        tmp = Core.input.mouseWorld(screenX, screenY);
//        SLog.info("moved x: " + tmp.x + " y: " + tmp.y);

        if (!active.isEmpty()) {
            Vars.player.shooting(false);
        }

        active.each(listener -> {
            listener.apply(tmp.x, tmp.y, (byte) 0);
        });
        return InputProcessor.super.touchDragged(screenX, screenY, pointer);
    }

    @FunctionalInterface
    public interface WorldListener  {
        void apply(float x, float y, byte state);
        // state 0 = move
        // state 1 = press
        // state 2 = unpress
    }
}

