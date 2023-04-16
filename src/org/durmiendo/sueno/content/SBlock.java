package org.durmiendo.sueno.content;

import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.Attributes;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.content.distr.Test;
import org.durmiendo.sueno.extend.SuenoBlock;

public class SBlock {
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
