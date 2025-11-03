package org.durmiendo.sueno.content;

import arc.Core;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import mindustry.gen.EntityMapping;
//import org.durmiendo.sueno.gen.SCall;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.core.Setter;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.graphics.DynamicTexture;
import org.durmiendo.sueno.utils.SLog;

public class SLoader {
    public static void load() {
        SLog.mark();

// no packet in this mod
//        SLog.mark();
        //SCall.registerPackets();
//        SLog.elapsedInfo("packets");


        SLog.mark();
        
        SLog.loadTime(SGraphics::load, "graphics");
        
        
        SEntityMapping.init();
        EntityMapping.nameMap.keys().toSeq().each(s -> {
            EntityMapping.nameMap.put("sueno-" + s, EntityMapping.nameMap.get(s));
        });
        SLog.elapsedInfo("entity mapping");

        SLog.loadTime(SItems::load, "items");
        SLog.loadTime(SLiquids::load, "liquids");
        SLog.loadTime(SStatusEffects::load, "effects");
        SLog.loadTime(SUnits::load, "units");
        SLog.loadTime(SBlocks::load, "blocks");
        SLog.loadTime(SPlanets::load, "planets");


        SLog.elapsedInfo("load content finished");
    }
}
