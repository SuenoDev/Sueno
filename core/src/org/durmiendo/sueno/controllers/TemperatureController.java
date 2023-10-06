package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Bar;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.temperature.Map;

public class TemperatureController extends GenericController{
    public Map temperatureList;
    public TemperatureController() {
        super(20);

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            start();
        });
        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });
        Events.on(EventType.BlockBuildEndEvent.class, e -> {
            e.tile.block().addBar("T", entity -> new Bar(
                    () -> "T",
                    () -> Color.cyan,
                    () -> (temperatureList.getUnit(e.tile.x, e.tile.y) + 200) / 400
            ));
        });
    }

    @Override
    public void update() {
        int i = 0;
        for (float t : temperatureList.value) {
            t -= SVars.freezingPower;
            if (t < -100) {
                if (Vars.world.build(i).efficiency-SVars.frostefficiency < 0) Vars.world.build(i).health-=SVars.frostDamage;
                Vars.world.build(i).efficiency-=SVars.frostefficiency;
            } else if (t > 100) {

            }
            i++;
        }
    }

    @Override
    public void start() {
        super.start();

        temperatureList = new Map(Vars.world.width(), Vars.world.width());
        temperatureList.value.forEach(t -> t = 100f);

    }

    @Override
    public void stop() {
        super.stop();
        save();
        temperatureList = null;

    }

    public void save() {

    }
}
