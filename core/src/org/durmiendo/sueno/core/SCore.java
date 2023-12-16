package org.durmiendo.sueno.core;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.content.SUnits;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.temperature.TemperatureController;

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

        SVars.ui.build();
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}