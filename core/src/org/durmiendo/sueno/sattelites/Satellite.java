package org.durmiendo.sueno.sattelites;

import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite extends ÑelestialBody {
    ÑelestialBase satelliteBase;
    SatelliteDialog d;
    public Satellite(int id, ÑelestialBase b, float r, float spacing, float distance, Planet planet) {

        super(id, r, spacing, distance, planet);

        satelliteBase = b;
        d = new SatelliteDialog();
    }

    public void touch() {
        d.build();
    }
}