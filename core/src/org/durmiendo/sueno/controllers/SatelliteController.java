package org.durmiendo.sueno.controllers;

import arc.Events;
import mindustry.Vars;
import mindustry.type.Planet;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.sattelites.SPlanet;

public class SatelliteController extends GenericController {

    public SatelliteController() {
        super(2);


    }

    @Override
    public void update() {
        Planet hielo = Vars.content.planet("hielo");
    }
}