package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SLoader;
import org.durmiendo.sueno.utils.SLog;

@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.json",
        classPrefix = "S"
)
public class SCore extends Mod {
    public SCore(){
        SVars.extendedLogs = Core.settings.getBool("extended-logs");
        SVars.core = this;
    }

    @Override
    public void loadContent(){
        SVars.sueno = Vars.mods.getMod(getClass());
        //SLog.line();
        SLoader.load();
    }

    @Override
    public void init() {
        SLog.info("extended logs: " + SVars.extendedLogs);

        SLog.info("start load settings");
        Setter.load();
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(SVars.sueno.name + '-' + nameWithoutPrefix);
    }
}