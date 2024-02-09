package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.struct.Seq;
import org.durmiendo.sueno.events.SEvents;
import org.durmiendo.sueno.satellites.CelestialBody;
import org.durmiendo.sueno.satellites.Satellite;

// TODO @nekit508: use AsyncProcess please, cause my GenericController is GenericCum
public class CelestialBodyController extends GenericController {
    public final Seq<CelestialBody> cbs;

    public CelestialBodyController() {
        super(100);
        Events.on(SEvents.CampaignOpenEvent.class, e -> {
            start();
        });
        Events.on(SEvents.CampaignCloseEvent.class, e -> {
            stop();
        });
        cbs = new Seq();
    }

    @Override
    public void update() {
        for (CelestialBody i : cbs) {
            i.update();
        }
    }

    public void addCB(CelestialBody s) {
        cbs.add(s);
    }

    public  void removeCB(CelestialBody s) {
        if (s.getClass() == Satellite.class) {
            ((Satellite) s).sd.hide();
        }
        cbs.remove(s);

    }

    public void draw() {
        for (CelestialBody c : cbs) {
            c.draw();
        }
    }
}