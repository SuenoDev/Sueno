package org.durmiendo.sueno.core;

import arc.Events;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.TemperatureController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.sattelites.Satellite;
import org.durmiendo.sueno.sattelites.ÑelestialBase;


// TODO remove old temperature and write new (on controllers)

public class SCore extends Mod {
    public SCore(){
        // TODO that's bad
        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;

            Log.info("Sueno render settings load");



            for(int i = 0;i < 1000000; i ++) {
                SVars.celestialBodyController.addSatellite(
                        new Satellite(i,
                                new ÑelestialBase(),
                                Mathf.random(1.8f, 4),
                                Mathf.random(0, 360),
                                Mathf.random(0, 360),
                                SPlanets.serpulo
                        )
                );
            }
        });

        Events.on(CampainOpen.class, e -> {
            SVars.celestialBodyController.addSatellite(new Satellite(-1, new ÑelestialBase(), 5, 5, 5, SPlanets.serpulo));
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
        SVars.celestialBodyController = new CelestialBodyController();
        SVars.weathercontroller = new WeatherController();

        SVars.ui.build();

    }
}
