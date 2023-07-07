package org.durmiendo.sueno.wrold.blocks.production;

import mindustry.gen.Building;
import mindustry.world.Block;

public class SDrill extends Block {

    public SDrill(String name) {
        super(name);
    }

    public class DrillBuild extends Building {
        public float progress;
        public float warmup;
        public float timeDrilled;
        public float lastDrillSpeed;

    }
}
