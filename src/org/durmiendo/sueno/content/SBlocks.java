package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.content.blocks.Test;

public class SBlocks {
    public static Block test;
    public static void load() {
        test = new Test("test") {
            {
                requirements(Category.defense, new ItemStack[]{new ItemStack(Items.coal, 3575)});
                this.health = 300;
                this.size = 1;
                attributes.set(SAttributes.temperatureMin, -250);
            }
        };
    }
}
