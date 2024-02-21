package org.durmiendo.sueno.world.blocks.walls;

import mindustry.gen.Building;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.BuildVisibility;

public class UnDestroyable extends Wall {

    public UnDestroyable(String name) {
        super(name);
        health = Integer.MAX_VALUE;
        buildVisibility = BuildVisibility.sandboxOnly;
    }

    public class Build extends Building {
        @Override
        public void update() {
            super.update();
            health = Integer.MAX_VALUE;
        }
    }
}
