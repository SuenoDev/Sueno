package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType;

public class TemperatureController extends GenericController{
    public TemperatureController() {
        super(2);

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            start();
        });
        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

    }

    @Override
    public void update() {
        Log.info("temparature update");
    }
}
