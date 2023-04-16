package org.durmiendo.sueno.content;

import mindustry.world.Block;
import mindustry.world.blocks.Attributes;
import mindustry.world.meta.Attribute;
import org.durmiendo.sueno.content.distr.Test;
import org.durmiendo.sueno.extend.SuenoBlock;

public class SBlock {
    public static Block test;
    public static void load() {
        test = new Test("test") {
            {
                this.health = 300;
                this.size = 1;
                attributes.set(SAttributes.temperature, 0);
                attributes.set(SAttributes.temperatureMin, -250);
                attributes.set(SAttributes.temperatureMax, 3000);
            }
        };
    }
}
