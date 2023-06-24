package org.durmiendo.sueno.core;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.controllers.SatelliteController;
import org.durmiendo.sueno.controllers.TemperatureController;

// TODO remove old temperature and write new (on controllers)

public class SCore extends Mod {
    public SCore(){
        // TODO that's bad
        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;

            Log.info("Sueno renser settings load");
        });
    }

    @Override
    public void loadContent(){
        SItems.load();
        SBlocks.load();
        SPlanets.load();

        Log.info("Sueno content load");
    }

    @Override
    public void init() {
        Log.info("Sueno init");
        SVars.temperatureController = new TemperatureController();
        SVars.satelliteController = new SatelliteController();

        SVars.ui.build();

    }
}
