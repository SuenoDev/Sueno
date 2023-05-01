package org.durmiendo.sueno.content.blocks;

import mindustry.entities.TargetPriority;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.mainContents.SuenoBlock;
import org.durmiendo.sueno.mainContents.SuenoCategory;

public class Test extends SuenoBlock {
    public Test(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        priority = TargetPriority.core;
        category = SuenoCategory.test;
        //it's a wall of course it's supported everywhere
        //envEnabled = Env.any;
    }
}
