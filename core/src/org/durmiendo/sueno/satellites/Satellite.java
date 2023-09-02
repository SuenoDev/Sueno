package org.durmiendo.sueno.satellites;

import arc.Core;
import arc.graphics.Color;
import mindustry.type.Planet;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite extends CelestialBody {
    CelestialBase satelliteBase;
    public Satellite( CelestialBase b, float r, float spacing, float distance, Planet planet) {

        super(r, spacing, distance, planet);
        button.image(Core.atlas.find("sueno-satellite"));
        button.clicked(() -> {

        });
        satelliteBase = b;
        c = Color.black;
    }
}