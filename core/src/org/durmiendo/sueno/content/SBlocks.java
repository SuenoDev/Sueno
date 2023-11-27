package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemeperatureSource;
import org.durmiendo.sueno.world.blocks.production.WallDrill;

import static mindustry.type.ItemStack.with;

public class SBlocks {

    public static Block
    //heaters
    heater, ts;
    public static void load() {
        // TODO delete this
        heater = new Heater("heater") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            consumePower(1.5f);
            range = 160f;
            size = 2;
            health = 200;
        }};
        ts = new TemeperatureSource("ts1") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            size = 1;
            health = 20;
        }};

        ts = new TemeperatureSource("ts2") {{
            requirements(Category.effect, with(Items.scrap, 10000000));
            solid = true;
            update = true;
            group = BlockGroup.projectors;
            emitLight = true;
            suppressable = true;
            envEnabled |= Env.space;
            configurable = true;
        }};
        new WallDrill("WaDR-22") {{
            requirements(Category.effect, with(Items.sporePod, 69));
            size = 3;
            rotators = 3;
            rotatorsSideSpace = 0.8f;
            health = 96;
            tier = 5;
            drillTime = 60 * 2.5f * 2f;

            consumeLiquid(Liquids.water, 0.25f / 60f).boost();
        }};
    }
}
