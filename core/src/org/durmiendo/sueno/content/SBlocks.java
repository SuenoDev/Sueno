package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.world.blocks.Heater;

import static mindustry.type.ItemStack.with;

public class SBlocks {

    public static Block
    //heaters
    heater;
    public static void load() {
        heater = new Heater("heater") {{
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 25, Items.silicon, 40, Items.copper, 50));
            consumePower(1.5f);
            range = 80f;
            size = 2;
            health = 200;
        }};
    }
}
