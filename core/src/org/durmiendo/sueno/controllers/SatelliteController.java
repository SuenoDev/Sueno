package org.durmiendo.sueno.controllers;

import arc.struct.Seq;
import arc.util.Log;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.sattelites.Satellite;

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
        satellites.forEach(s-> {
            s.update();
            if (s.orbitRadius < 1.5f) removeSatellite(s);
        });
    }

    public Seq<Satellite> getSatellites() {
        return satellites;
    }

    public void addSatellite(Satellite s) {
        if (SVars.ui.planet == null) {
            Log.info("SVars.ui.planet == " + SVars.ui.planet);
            return;
        }

        s.speed.x = 20;

        satellites.add(s);
        SVars.ui.planet.sib.add(s);
        SVars.ui.planet.st.addChild(s.button);
    }


    public  void removeSatellite(Satellite s) {
        satellites.remove(s);
        SVars.ui.planet.sib.remove(s);
        SVars.ui.planet.st.removeChild(s.button);
    }
}