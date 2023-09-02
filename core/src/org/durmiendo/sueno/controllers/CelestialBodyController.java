package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.math.Mathf;
import arc.struct.Seq;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.satellites.CelestialBody;

// TODO @nekit508: use AsyncProcess please, cause my GenericController is GenericCum
public class CelestialBodyController extends GenericController {
    public final Seq<CelestialBody> cbs;

    public CelestialBodyController() {
        super(2);
        Events.on(CampainOpen.class, e -> {
            start();
        });
        Events.on(CampainClose.class, e -> {
            stop();
        });
        cbs = new Seq();
    }

    @Override
    public void update() {
    }

    public void addCB(CelestialBody s) {
        s.speed = Mathf.random(50, 100);

        cbs.add(s);
    }

    public  void removeCB(CelestialBody s) {
        cbs.remove(s);
    }

    public void draw() {
        for (CelestialBody i : cbs) {
            i.draw();
            i.update();
        }
    }
}