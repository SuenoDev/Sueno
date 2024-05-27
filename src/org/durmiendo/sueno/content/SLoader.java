package org.durmiendo.sueno.content;

import mindustry.gen.EntityMapping;
import org.durmiendo.sueno.gen.SCall;
import org.durmiendo.sueno.gen.SEntityMapping;
import org.durmiendo.sueno.utils.SLog;

public class SLoader {
    public static void load() {
        SLog.mark();


        SLog.mark();
        SCall.registerPackets();
        SLog.einfoElapsed("packets");


        SLog.mark();
        SEntityMapping.init();

        EntityMapping.nameMap.keys().toSeq().each(s -> {
            EntityMapping.nameMap.put("sueno-" + s, EntityMapping.nameMap.get(s));
        });
        SLog.einfoElapsed("entity mapping");


        SLog.mark();
        SItems.load();
        SLog.einfoElapsed("items");


        SLog.mark();
        SUnits.load();
        SLog.einfoElapsed("units");


        SLog.mark();
        SBlocks.load();
        SLog.einfoElapsed("blocks");


        SLog.mark();
        SPlanets.load();
        SLog.einfoElapsed("planets");

        SLog.einfoElapsed("load content finished");
    }
}
