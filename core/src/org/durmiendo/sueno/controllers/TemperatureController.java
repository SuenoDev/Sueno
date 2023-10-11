package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.graphics.Color;
import arc.math.Interp;
import arc.util.Log;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.entities.TargetPriority;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;
import org.durmiendo.sueno.math.Map;

public class TemperatureController extends GenericController{
    public volatile Map fMap, cMap;
    public TemperatureController() {
        super(1);


        Events.on(EventType.WorldLoadEndEvent.class, e -> {

            start();

            for (Block block : Vars.content.blocks()) {
                block.addBar("temperature", entity -> new Bar(
                        () -> "Температура\n" +  String.format("%8s", Math.round(SVars.tMap.getUnit(entity.tileX(), entity.tileY()))) + " / " + String.format("%8s", fMap.getUnit(entity.tileX(), entity.tileY())),
                        () -> Colorated.gradient(Color.cyan, Color.red, (SVars.tMap.getUnit(entity.tileX(), entity.tileY()) + 200) / 400),
                        () -> (SVars.tMap.getUnit(entity.tileX(), entity.tileY()) + 200) / 400
                ));
            }

        });

        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

        Events.on(EventType.BlockBuildBeginEvent.class, e -> {
            SVars.tMap.setValue(e.tile.x, e.tile.y, SVars.startT);
            cMap.setValue(e.tile.x, e.tile.y, SVars.startCeiling);
        });
    }

    @Override
    public void update() {
        for (int i = 0; i < SVars.tMap.value.size; i++) {
            if (SVars.tMap.getUnit(i) == 100f) return;
            Log.info(SVars.tMap.getUnit(i));
        }

        SVars.tMap.setValue(100, 100, 99);
        SVars.tMap.setValue(101, 101, 99);
        SVars.tMap.setValue(100, 101, 99);
        SVars.tMap.setValue(101, 100, 99);

//        for (int i = 0; i < fMap.value.size; i++) {
//            //freezingChange(SVars.freezingPower / frequency, i);
//        }
//
//        float fs;
//        float t;
//        for (int i = 0; i < SVars.tMap.value.size; i++) {
//            fs = fMap.getUnit(i);
//            t = SVars.tMap.getUnit(i);
//            fMap.setValue(i, 0);
//
//            if (!isNormalFreezingSpeed(fs)) notNormalFreezingSpeed(fs, i);
//            if (!isNormalTemperature(t)) notNormalTemperature(t, i);
//
//            temperatureChange(fs, i);
//        }
    }

    @Override
    public void start() {
        super.start();

        SVars.tMap = new Map(Vars.world.width(), Vars.world.height());
        fMap = new Map(Vars.world.width(), Vars.world.height());
        cMap = new Map(Vars.world.width(), Vars.world.height());

        for (int i = 0; i < SVars.tMap.value.size; i++) {
            SVars.tMap.value.set(i, SVars.startT);
            cMap.value.set(i, SVars.startCeiling);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    public void freezingDamage(float fs, float t, int p) {

    }

    public void notNormalFreezingSpeed(float fs, int p) {

    }

    public void notNormalTemperature(float t, int p) {
        Building b = Vars.world.build(p);
        if (t > SVars.minEffectivityT) {
            b.applyBoost(SVars.maxBoost / 100, frequency + 1);
        }

        if (t > SVars.maxSafeT) {
            b.damage(Interp.linear.apply(20 / frequency, 300 / frequency, (t + 300) / 400));
        }
        if (t < SVars.maxSafeT) {

        }
    }

    public boolean isNormalFreezingSpeed(float fs) {
        return true;
    }

    public boolean isNormalTemperature(float t) {
        return true;
    }

    public void temperatureChange(float hs, int x, int y) {
        SVars.tMap.setValue(x, y, SVars.tMap.getUnit(x, y) + hs);
    }
    public void temperatureChange(float hs, int p) {
        SVars.tMap.setValue(p, SVars.tMap.getUnit(p) + hs);
    }

    public void freezingChange(float hs, int x, int y) {
        fMap.setValue(x, y, fMap.getUnit(x, y) + hs);
    }
    public void freezingChange(float hs, int p) {
        fMap.setValue(p, fMap.getUnit(p) + hs);
    }
}
