package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.sattelites.Satellite;
import org.durmiendo.sueno.sattelites.CelestialBody;

public class CelestialBodyController extends GenericController {

    private Seq<CelestialBody> cbs;

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

    public Seq<CelestialBody> getCB() {
        return cbs;
    }

    public void addSatellite(Satellite s) {
        if (SVars.ui.planet == null) {
            Log.info("[red]SVars.ui.planet == null!");
            return;
        }

        s.speed = Mathf.random(50, 100);

        cbs.add(s);
    }


    public  void removeCB(CelestialBody s) {
        cbs.remove(s);
    }

    public void draw() {
        for (CelestialBody i : cbs) {
            Vec3 e = Vars.renderer.planets.cam.project(i.position);
            Drawf.square( e.x, e.y, 15, 0, Color.black);
            i.update();
        }
    }
}