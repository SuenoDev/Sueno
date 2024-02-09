package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.content.SUnits;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.satellites.CelestialBase;
import org.durmiendo.sueno.satellites.Satellite;
import org.durmiendo.sueno.temperature.TemperatureController;

import static arc.Core.settings;

@ModAnnotations.RootDirectoryPath(rootDirectoryPath = "core")
@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.json",
        classPrefix = "S"
)
@ModAnnotations.MainClass
public class SCore extends Mod {
    public SCore(){
        SVars.core = this;
        settings.put("campaignselect", true);
    }

    @Override
    public void loadContent(){
        SVars.sueno = Vars.mods.getMod(getClass());

        SCall.registerPackets();
        SEntityMapping.init();

        SItems.load();
        SUnits.load();
        SBlocks.load();
        SPlanets.load();
    }

    @Override
    public void init() {
        SVars.weathercontroller = new WeatherController();
        SVars.tempTemperatureController = new TemperatureController();
        SVars.celestialBodyController = new CelestialBodyController();
        //TODO: убрать куда нибудь с глаз долой
        for (int i = 0; i < 90; i++) {
            Satellite s = new Satellite(new CelestialBase(),12, i*4, 60, SPlanets.hielo);
            s.speed = 40f;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(),12, i*4, -60, SPlanets.hielo);
            s.speed = -40f;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(),12, i*4, 0, SPlanets.hielo);
            s.speed = 40f;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(),12, 0, i*4, SPlanets.hielo);
            s.speed = 40f;
            s.speedType=false;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(), 12, i*4f, i*4f, SPlanets.hielo);
            s.speed = 80f;
            s.speedType=true;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(),12, i*4f+120f, i*4f, SPlanets.hielo);
            s.speed = 80f;
            s.speedType=true;
            SVars.celestialBodyController.addCB(s);
            s = new Satellite(new CelestialBase(),12, i*4f+240f, i*4f, SPlanets.hielo);
            s.speed = 80f;
            s.speedType=true;
            SVars.celestialBodyController.addCB(s);
        }

        SVars.ui.build();
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}