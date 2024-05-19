package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.gen.EntityMapping;
import mindustry.io.SaveVersion;
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
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.temperature.TemperatureCustomChunk;
import org.durmiendo.sueno.utils.SLog;

@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.json",
        classPrefix = "S"
)
public class SCore extends Mod {
    public SCore(){
        SVars.core = this;
    }

    @Override
    public void loadContent(){
        SVars.sueno = Vars.mods.getMod(getClass());

        SCall.registerPackets();
        SEntityMapping.init();
        EntityMapping.nameMap.keys().toSeq().each(s -> {
            EntityMapping.nameMap.put("sueno-" + s, EntityMapping.nameMap.get(s));
        });

        SItems.load();
        SUnits.load();
        SBlocks.load();
        SPlanets.load();

        SLog.info("content loaded");
    }

    @Override
    public void init() {
        SVars.weathercontroller = new WeatherController();
        SVars.celestialBodyController = new CelestialBodyController();
        SaveVersion.addCustomChunk("sueno-temperature-chunk", new TemperatureCustomChunk());
        VoidStriderCollapseEffectController.init();
//        char[] c = "Sueno в разработке!".toCharArray();
//        float len = c.length * 5.3f;
//        for (float i = 0; i < c.length; i++) {
//            CharSatellite s = new CharSatellite(12, 0f, 0.45f * Mathf.pi, SPlanets.hielo, c[c.length - (int) i - 1]);
//            s.speed = 1f / 120f;
//            s.angle = (i / len * Mathf.pi * 2f);
//            SVars.celestialBodyController.addCB(s);
//        }
        SVars.ui.build();

        Core.settings.put("campaignselect", true);

        Vars.renderer.maxZoom = 30f;
        SLog.info("max zoom setting from: 30.0f");

        Vars.renderer.minZoom = 0.5f;
        SLog.info("min zoom setting from: 0.5f");

        SLog.info("mod inited");
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}