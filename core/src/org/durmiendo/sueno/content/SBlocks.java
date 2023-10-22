package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemeperatureSource;

import static mindustry.type.ItemStack.with;

public class SBlocks {

    public static Block
    //heaters
    heater, ts;
    public static void load() {
        heater = new Heater("heater") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            consumePower(1.5f);
            range = 160f;
            size = 2;
            health = 200;
        }};
        ts = new TemeperatureSource("ts") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            size = 1;
            health = 20;
        }};

        ts = new TemeperatureSource("ts") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            solid = true;
            update = true;
            group = BlockGroup.projectors;
            emitLight = true;
            suppressable = true;
            envEnabled |= Env.space;
            configurable = true;
        }};
    }
}
