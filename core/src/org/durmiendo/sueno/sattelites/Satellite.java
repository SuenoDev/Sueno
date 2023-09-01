package org.durmiendo.sueno.sattelites;

import arc.Core;
import arc.graphics.Color;
import mindustry.Vars;
import mindustry.type.Planet;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite extends CelestialBody {
    CelestialBase satelliteBase;
    SatelliteDialog d;
    public Satellite( CelestialBase b, float r, float spacing, float distance, Planet planet) {

        super(r, spacing, distance, planet);
        button.image(Core.atlas.find("sueno-satellite"));
        button.clicked(() -> {
           d.show();
        });
        satelliteBase = b;
        d = new SatelliteDialog();
        c = Color.black;
    }
}