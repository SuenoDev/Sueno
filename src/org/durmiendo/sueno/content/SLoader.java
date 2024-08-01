package org.durmiendo.sueno.content;

import mindustry.gen.EntityMapping;
//import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.utils.SLog;

public class SLoader {
    public static void load() {
        SLog.mark();


        SLog.mark();
//        SCall.registerPackets();
        SLog.elapsedInfo("packets");


        SLog.mark();
        SEntityMapping.init();

        EntityMapping.nameMap.keys().toSeq().each(s -> {
            EntityMapping.nameMap.put("sueno-" + s, EntityMapping.nameMap.get(s));
        });
        SLog.elapsedInfo("entity mapping");


        SLog.mark();
        SItems.load();
        SLog.elapsedInfo("items");


        SLog.mark();
        SStatusEffects.load();
        SLog.elapsedInfo("status effects");


        SLog.mark();
        SUnits.load();
        SLog.elapsedInfo("units");

        SLog.mark();
        SBlocks.load();
        SLog.elapsedInfo("blocks");


        SLog.mark();
        SPlanets.load();
        SLog.elapsedInfo("planets");


        SLog.elapsedInfo("load content finished");
    }
}
