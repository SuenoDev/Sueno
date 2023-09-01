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
import org.durmiendo.sueno.sattelites.ÑelestialBody;

public class CelestialBodyController extends GenericController {

    private Seq<ÑelestialBody> cbs;

    public CelestialBodyController() {
        super(2);
        Events.on(CampainOpen.class, e -> {
            start();
        });
        Events.on(CampainClose.class, e -> {
            stop();
        });
        cbs = new Seq<ÑelestialBody>();
    }

    @Override
    public void update() {
    }

    public Seq<ÑelestialBody> getCB() {
        return cbs;
    }

    public void addSatellite(Satellite s) {
        if (SVars.ui.planet == null) {
            Log.info("[red]SVars.ui.planet == null!");
            return;
        }

        s.speed.x = Mathf.random(50, 100);
        //s.speed.y = Mathf.random(3,150);

        cbs.add(s);
    }


    public  void removeCB(ÑelestialBody s) {
        cbs.remove(s);
    }

    public void draw() {
        for (ÑelestialBody i : cbs) {
            Vec3 e = Vars.renderer.planets.cam.project(i.position);
            Drawf.square( e.x, e.y, 15, 0, Color.black);
            i.update();
        }
    }
}