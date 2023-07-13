package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.Env;

public class SBlocks {

    public static Block
    //satellites

    satellitePad, satelliteConstructor, satelliteAssembler, satelliteProgrammer,

    //drills
    drillo;
    public static void load() {
        //satellites

        //drills
        drillo = new Drill("drillo"){{
            requirements(Category.production, ItemStack.with(Items.coal, 100000) );
            tier = 2;
            drillTime = 600;
            size = 2;
            //mechanical drill doesn't work in space
            envEnabled ^= Env.space;
            researchCost = ItemStack.with(Items.copper, 10);

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
    }
}
