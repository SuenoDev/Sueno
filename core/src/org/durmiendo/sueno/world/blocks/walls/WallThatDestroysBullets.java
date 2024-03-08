package org.durmiendo.sueno.world.blocks.walls;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.world.blocks.defense.Wall;

public class WallThatDestroysBullets extends Wall {
    public WallThatDestroysBullets(String name) {
        super(name);
        update = true;
    }

    public class WallThatDestroysBulletsBuild extends WallBuild {
        public boolean initialized;

        @Override
        public void updateTile() {
            initialized = true;

            super.updateTile();
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            initialized = read.bool();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(initialized);
        }
    }
}
