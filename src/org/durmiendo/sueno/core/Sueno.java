package org.durmiendo.sueno.core;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.Vars;
import mindustry.mod.Mod;
import mmc.annotations.ModAnnotations;
import org.durmiendo.sueno.content.SLoader;
import org.durmiendo.sueno.settings.SettingsBuilder;
import org.durmiendo.sueno.utils.SLog;


@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.sueno",
        modInfoPath = "res/mod.json",
        classPrefix = "S"
)
public class Sueno extends Mod {

    public Sueno() {
        SLog.init();
        SLog.info("Sueno new instance!");




        SVars.core = this;
    }

    @Override
    public void loadContent() {
//        Vars.renderer = new SRenderer();


//        SLog.loadTime(() -> {
//            final int[] loaded = {0};
//            Core.atlas.getRegionMap().each((s, atlasRegion) -> {
//                SVars.regions.put(atlasRegion.texture, s);
//                TextureRegion n = Core.atlas.find(s + "-normal");
//                if (Core.atlas.isFound(n)) {
//                    loaded[0]++;
//                    SLog.load("normal texture, founded: " + s);
//                    Core.atlas.getRegionMap().put(s, new RegionsTextures(atlasRegion, n));
//                }
//            });
//            SLog.info(loaded[0] + " normal textures loaded!");
//
//            try {
//                Draw.flush();
//                Core.batch = new SSortedSpriteBatch();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }, "normal texture load");
        SVars.sueno = Vars.mods.getMod(getClass());
        Log.info("load Sueno content");

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