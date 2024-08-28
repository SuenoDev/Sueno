package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.Vars;
import mindustry.mod.Mod;
import mmc.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SLoader;
import org.durmiendo.sueno.settings.SettingsBuilder;


@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.json",
        classPrefix = "S"
)
public class Sueno extends Mod {
    public Sueno() {
        SVars.core = this;
    }

    @Override
    public void loadContent() {
        SVars.sueno = Vars.mods.getMod(getClass());
        Log.info("load Sueno content");

//        if (SVars.mainDirecory.child("settings.ulk").exists())
            SettingsBuilder.load();

        SLoader.load();
    }

    @Override
    public void init() {
        Log.info("extended logs: " + SVars.extendedLogs);
        Log.info("load Sueno settings");

        Setter.load();

    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}