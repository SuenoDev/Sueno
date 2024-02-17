package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.type.Planet;
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


    public boolean isVisible(CelestialBody s) {
//        Vec3 pos3 = new Vec3(Vars.renderer.planets.cam.position);
//        Vec3 pos2 = new Vec3(s.planet.position);
//        Vec3 pos1 = new Vec3(s.position);
//        float r = s.planet.radius;
//        Vec3 normVector = pos2.sub(new Vec3(pos3)).nor();
//        float distanceToPos1 = pos2.dst(new Vec3(pos1));
//        Vec3 transformedVector = new Vec3();
//        transformedVector.x = normVector.x * distanceToPos1;
//        transformedVector.y = normVector.y * distanceToPos1;
//        transformedVector.z = normVector.z * distanceToPos1;
//        float distanceToCenter = transformedVector.dst(Vec3.Zero);
        return true;
    }


    public void draw() {
        for (CelestialBody c : cbs) {
            if (isVisible(c)) c.draw();
        }
    }
}