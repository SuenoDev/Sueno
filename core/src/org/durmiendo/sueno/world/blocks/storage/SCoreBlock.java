package org.durmiendo.sueno.world.blocks.storage;

import arc.util.Log;
import mindustry.gen.Building;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.SInterp;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.build.Heated;

public class SCoreBlock extends CoreBlock {
    public float heatPower = 16f*size;
    public float range = 60;
    public float ceiling = 20;
    public SCoreBlock(String name) {
        super(name);
        update = true;
        emitLight = true;
        lightRadius = range / 4 * heatPower;
        suppressable = true;
        envEnabled |= Env.space;
        solid = true;
    }

    public class CoreBuild extends Building implements Heated {
        @Override
        public boolean allowUpdate() {
            return true;
        }

        public float range() {
            return range;
        }

        @Override
        public void update() {
            Log.info("update");
            super.update();
        }

        @Override
        public void updateTile() {
            Log.info("update tile");
            if (!SVars.TemperatureСontroller.stop) {
                for (int x = tileX()-2 ; x < tileX()+size; x++) {
                    for (int y = tileY()-2; y < tileY()+size; y++) {
                        SVars.TemperatureСontroller.at(x, y, 1000);
                    }
                }
            }
            super.updateTile();
        }
    }
}
