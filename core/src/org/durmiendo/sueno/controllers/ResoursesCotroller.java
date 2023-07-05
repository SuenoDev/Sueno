package org.durmiendo.sueno.controllers;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.controllers.resurs.ResMapUnit;

public class ResoursesCotroller extends GenericController{
    public ResoursesCotroller() {
        super(2);

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            start();

            resurses = new ResMapUnit[Vars.world.width()][Vars.world.height()];

            for (ResMapUnit[] i : resurses) {
                for (ResMapUnit j : i) {
                    j = new ResMapUnit();
                    j.counts[0] = 3.4f;
                    j.avail[0] = SItems.indiganit;
                }
            }
        });
        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

    }

    public ResMapUnit[][] resurses;

    @Override
    public void update() {
    }
}
