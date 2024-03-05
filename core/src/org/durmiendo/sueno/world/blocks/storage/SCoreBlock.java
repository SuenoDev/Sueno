package org.durmiendo.sueno.world.blocks.storage;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.world.blocks.build.Heated;

public class SCoreBlock extends CoreBlock {
    public float heatPower = 0.3f;
    public float range = 60;
    public SCoreBlock(String name) {
        super(name);
        update = true;
        emitLight = true;
        lightRadius = range / 4 * heatPower;
        suppressable = true;
        envEnabled |= Env.space;
        solid = true;
    }

    public class CoreBuild extends CoreBlock.CoreBuild implements Heated {
        @Override
        public void updateTile() {
            if (!SVars.temperatureController.stop) {
                for (int x = tileX()-Mathf.floor(size/2f); x < tileX()-Mathf.floor(size/2f)+size; x++) {
                    for (int y = tileY()-Mathf.floor(size/2f); y < tileY()-Mathf.floor(size/2f)+size; y++) {
                        SVars.temperatureController.at(x, y, heatPower * Time.delta);
                    }
                }
            }
            super.updateTile();
        }
    }
}
