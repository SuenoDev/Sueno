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
                group = BlockGroup.walls;
                requirements(Category.defense, new ItemStack[]{new ItemStack(Items.coal, 3)});
                this.health = 300;
                this.size = 1;
                attributes.set(SAttributes.temperature, 0);
                attributes.set(SAttributes.temperatureMin, -250);
                attributes.set(SAttributes.temperatureMax, 300);
            }
        };
    }
}
