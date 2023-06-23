package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.type.Planet;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.sattelites.SPlanet;
import org.durmiendo.sueno.sattelites.Satellite;
import org.durmiendo.sueno.sattelites.SatelliteBase;

public class SatelliteController extends GenericController {

    Seq<Satellite> satellites;

    public SatelliteController() {
        super(2);
        Log.info("satellite init");
        satellites = new Seq<Satellite>();
        satellites.add(new Satellite(0, new SatelliteBase(), 10, 5, SPlanets.serpulo));
    }

    @Override
    public void update() {
        Log.info("satellites draw");
        satellites.each((satellite -> {
            satellite.draw();
        }));
    }
}