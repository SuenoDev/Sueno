package org.durmiendo.sueno.content.distr;

import mindustry.entities.TargetPriority;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.extend.SuenoBlock;

public class Test extends SuenoBlock {
    public Test(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        priority = TargetPriority.core;

        //it's a wall of course it's supported everywhere
        //envEnabled = Env.any;
    }
}
