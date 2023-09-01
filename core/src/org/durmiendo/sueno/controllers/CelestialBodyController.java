package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Icon;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
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
        Vars.ui.planet.fill(hernya1 -> {
            hernya1.pane(hernya2 -> {
                hernya2.table(hernya3 -> {
                    hernya3.button(Icon.admin, () -> {

                    });
                }).growX();
            }).top().left().width(200).height(200);
        });

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