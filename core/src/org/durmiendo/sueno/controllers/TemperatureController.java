package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.graphics.Color;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;
import org.durmiendo.sueno.math.Map;

public class TemperatureController extends GenericController{
    public Map tMap, fMap, cMap;
    public TemperatureController() {
        super(2);

        Events.on(EventType.WorldLoadEndEvent.class, e -> {

            start();

            for (Block block : Vars.content.blocks()) {
                block.addBar("temperature", entity -> new Bar(
                        () -> "Температура",
                        () -> Colorated.gradient(Color.cyan, Color.red, (tMap.getUnit(entity.tileX(), entity.tileY()) + 200) / 400),
                        () -> (tMap.getUnit(entity.tileX(), entity.tileY()) + 200) / 400
                ));
            }

        });

        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

        Events.on(EventType.BlockBuildBeginEvent.class, e -> {
            tMap.setValue(e.tile.x, e.tile.y, SVars.startT);
            cMap.setValue(e.tile.x, e.tile.y, SVars.startCeiling);
        });
    }

    @Override
    public void update() {
        for (int i = 0; i < fMap.value.size; i++) {
            fMap.setValue(i, SVars.freezingPower);
        }

        float fs;
        float t;
        for (int i = 0; i < tMap.value.size; i++) {
            fs = fMap.getUnit(i);
            t = tMap.getUnit(i);

            if (!isNormalFreezingSpeed(fs)) notNormalFreezingSpeed(fs, i);
            if (!isNormalTemperature(t)) notNormalTemperature(t, i);

            temperatureChange(SVars.freezingPower, i);
        }
    }

    @Override
    public void start() {
        super.start();

        tMap = new Map(Vars.world.width(), Vars.world.height());
        fMap = new Map(Vars.world.width(), Vars.world.height());
        cMap = new Map(Vars.world.width(), Vars.world.height());

        for (int i = 0; i < tMap.value.size; i++) {
            tMap.value.set(i, SVars.startT);
            cMap.value.set(i, SVars.startCeiling);
        }
    }

    @Override
    public void stop() {
        super.stop();
        save();
        tMap = null;
    }

    public void save() {

    }
    public void freezingDamage(float fs, float t, int p) {

    }

    public void notNormalFreezingSpeed(float fs, int p) {

    }

    public void notNormalTemperature(float t, int p) {

    }

    public boolean isNormalFreezingSpeed(float fs) {

        return true;
    }

    public boolean isNormalTemperature(float t) {
        return true;
    }

    public void temperatureChange(float hs, int x, int y) {
        tMap.setValue(x, y, tMap.getUnit(x, y)-hs);
    }
    public void temperatureChange(float hs, int p) {
        tMap.setValue(p, tMap.getUnit(p)-hs);
    }

    public void freezingChange(float hs, int x, int y) {
        fMap.setValue(x, y, tMap.getUnit(x, y)-hs);
    }
    public void freezingChange(float hs, int p) {
        fMap.setValue(p, tMap.getUnit(p)-hs);
    }
}
