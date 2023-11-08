package org.durmiendo.sueno.core;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.content.SUnits;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.temperature.Controller;

@ModAnnotations.RootDirectoryPath(rootDirectoryPath = "core")
@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.hjson"
)
public class SCore extends Mod {

    public SCore(){
        // TODO that's bad
        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;
        });

        /*Events.on(ClientLoadEvent.class, e -> {
            for(int i = 0;i < 500; i++) {
                SVars.celestialBodyController.addCB(
                        new Satellite(
                                new CelestialBase(),
                                Mathf.random(1.8f, 5),
                                Mathf.random(0, 360),
                                Mathf.random(0, 360),
                               SPlanets.serpulo
                        )
                );
            }
        });*/
    }

    @Override
    public void loadContent(){
        SItems.load();
        SUnits.load();
        SBlocks.load();
        SPlanets.load();
    }

    @Override
    public void init() {
        //SVars.temperatureController = new TemperatureController();
        //SVars.celestialBodyController = new CelestialBodyController();
        SVars.weathercontroller = new WeatherController();
        SVars.tempController = new Controller();

        SVars.ui.build();
        //SaveVersion.addCustomChunk("SuenoChunk", new SChunk());
    }
}
