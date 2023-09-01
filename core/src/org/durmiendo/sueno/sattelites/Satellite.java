package org.durmiendo.sueno.sattelites;

import mindustry.type.Planet;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite extends CelestialBody {
    CelestialBase satelliteBase;
    SatelliteDialog d;
    public Satellite(int id, CelestialBase b, float r, float spacing, float distance, Planet planet) {

        super(id, r, spacing, distance, planet);

        satelliteBase = b;
        d = new SatelliteDialog();
    }

    public void touch() {
        d.build();
    }
}