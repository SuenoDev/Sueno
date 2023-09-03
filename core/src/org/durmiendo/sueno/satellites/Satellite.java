package org.durmiendo.sueno.satellites;

import arc.Core;
import arc.graphics.Color;
import mindustry.type.Planet;
import mindustry.ui.Styles;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite extends CelestialBody {
    CelestialBase i;
    SatelliteDialog sd = new SatelliteDialog(Styles.fullDialog);
    public Satellite( CelestialBase b, float r, float spacing, float distance, Planet planet) {

        super(r, spacing, distance, planet);
        health = 375;


        button.image(Core.atlas.find("sueno-satellite"));
        button.clicked(() -> {
            sd.put(this);
            sd.show();
        });
        i = b;
        c = Color.black;
    }

    @Override
    public void update() {
        super.update();

    }

    public void upds() {
        if (orbitRadius < planet.radius) this.destroy();
        if (orbitRadius < planet.radius+1) {
            down();
            damage(0.01f);
        }
    }

    public void up() {
        orbitRadius+=0.001f;
        sd.upd(this);
        upds();
    }

    public void down() {
        orbitRadius-=0.001f;
        sd.upd(this);
        upds();
    }

    @Override
    public void destroy() {
        super.destroy();
        sd.hide();
    }
}