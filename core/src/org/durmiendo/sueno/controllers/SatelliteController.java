package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.sattelites.Satellite;
import org.durmiendo.sueno.sattelites.SatelliteBase;

public class SatelliteController extends GenericController {

    private Seq<Satellite> satellites;

    public SatelliteController() {
        super(2);
        /*Events.on(CampainOpen.class, e -> {
            start();
        });
        Events.on(CampainClose.class, e -> {
            stop();
        });*/
        start();
        Log.info("satellite init");
        satellites = new Seq<Satellite>();



    }

    @Override
    public void update() {


    }

    public Seq<Satellite> getSatellites() {
        return satellites;
    }

    public void addSatellite(Satellite s) {
        if (SVars.ui.planet == null) {
            Log.info("SVars.ui.planet == " + SVars.ui.planet);
            return;
        }
        satellites.add(s);
        Vars.ui.planet.addChild(s.button);
        SVars.ui.planet.sib.add(s.button);
    }


    public  void removeSatellite(Satellite s) {
        Vars.ui.planet.removeChild(s.button);
        satellites.remove(s);
        SVars.ui.planet.sib.remove(s.button);
    }
}