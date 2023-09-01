package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.sattelites.CelestialBody;

public class CelestialBodyController extends GenericController {

    private Seq<CelestialBody> cbs;
    public Table cdt;

    public CelestialBodyController() {
        super(2);
        Events.on(CampainOpen.class, e -> {
            start();
        });
        Events.on(CampainClose.class, e -> {
            stop();
        });
        cbs = new Seq();
        //cdt = new Table();
        Vars.ui.planet.add(cdt);
    }

    @Override
    public void update() {
    }

    public Seq<CelestialBody> getCB() {
        return cbs;
    }

    public void addCB(CelestialBody s) {
        s.speed = Mathf.random(50, 100);

        cbs.add(s);
        Vars.ui.planet.add(s.button);
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