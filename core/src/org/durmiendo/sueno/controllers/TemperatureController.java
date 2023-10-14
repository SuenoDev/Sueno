package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.graphics.Color;
import arc.math.Interp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;
import org.durmiendo.sueno.math.Map;


public class TemperatureController extends GenericController{
    public volatile Map tMap, fMap, flMap, cMap;
    public int size;
    public TemperatureController() {
        super(1);


        Events.on(EventType.WorldLoadEndEvent.class, e -> {

            start();



            for (Block block : Vars.content.blocks()) {
                block.addBar("temperature", entity -> new Bar(
                        () -> "Температура " + Math.round(tMap.get(entity.tileX(), entity.tileY())) + "°C\n" + Math.round(flMap.get(entity.tileX(), entity.tileY())) + "°C / сек",
                        () -> Colorated.gradient(Color.cyan, Color.red, (tMap.get(entity.tileX(), entity.tileY()) + 200) / 400),
                        () -> {
                            Float r = tMap.get(entity.tileX(), entity.tileY());
                            return (r + 200) / 400;
                        }
                ));
            }
        });

        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

        Events.on(EventType.BlockBuildBeginEvent.class, e -> {
            tMap.set(e.tile.x, e.tile.y, Float.valueOf(SVars.startT));
            cMap.set(e.tile.x, e.tile.y, Float.valueOf(SVars.startCeiling));
        });


    }

    @Override
    public void update() {
        for (int p = 0; p < SVars.temperatureController.size; p++) {
            fMap.addi(p, Float.valueOf(SVars.freezingPower/100));
            Float fs = fMap.geti(p);
            tMap.addi(p, fs);
//            if (!isNormalFreezingSpeed(fs)) notNormalFreezingSpeed(fs, p);
            flMap.seti(p, fs);
            fMap.seti(p, Float.valueOf(0));
        }
//        for (int i = 0; i < SVars.temperatureController.size; i++) {
//            Float t = tMap.geti(i);
//            if (!isNormalTemperature(t)) notNormalTemperature(t, i);
//        }
    }

    @Override
    public void start() {
        super.start();
        size = Vars.world.width() * Vars.world.height();
        tMap = new Map(Vars.world.width(), Vars.world.height());
        fMap = new Map(Vars.world.width(), Vars.world.height());
        cMap = new Map(Vars.world.width(), Vars.world.height());
        flMap = new Map(Vars.world.width(), Vars.world.height());

        tMap.fill(Float.valueOf(SVars.startT));
        fMap.fill(Float.valueOf(0f));
        cMap.fill(Float.valueOf(SVars.startCeiling));
        flMap.fill(Float.valueOf(0f));
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
}
