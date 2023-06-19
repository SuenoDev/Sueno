package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.Test;

public class SBlocks {
    public static Block test, heater;
    public static void load() {
        test = new Test("test") {
            {
                requirements(Category.defense, new ItemStack[]{new ItemStack(Items.coal, 3575)});
                this.health = 300;
                this.size = 1;
                min = -250;
            }
        };
        heater = new Heater("heater") {
            {
                requirements(Category.defense, new ItemStack[]{new ItemStack(Items.coal, 35750)});
                this.health = 300;
                this.size = 2;
                min = -250;
            }
        };
    }
}
