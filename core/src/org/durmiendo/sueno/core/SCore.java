package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.mod.Mod;
import mmc.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.content.SUnits;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.satellites.CharSatellite;
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

        char[] c = "Пробная версия Sueno закончилась, пожалуйста купите полную версию всего за 299$".toCharArray();
        float len = c.length * 1.3f;
        for (float i = 0; i < c.length; i++) {
            CharSatellite s = new CharSatellite(12, 0f, 0.45f * Mathf.pi, SPlanets.hielo, c[c.length - (int) i - 1]);
            s.speed = 1f / 120f;
            s.angle = (i / len * Mathf.pi * 2f);
            SVars.celestialBodyController.addCB(s);
        }
        SVars.ui.build();
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}