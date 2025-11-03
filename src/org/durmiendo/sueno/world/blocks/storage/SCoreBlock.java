package org.durmiendo.sueno.world.blocks.storage;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.RegionsTextures;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.world.blocks.build.Heated;

public class SCoreBlock extends CoreBlock {
    public float heatPower = 0.3f;
    public float range = 60;
    RegionsTextures rt;
    
    public SCoreBlock(String name) {
        super(name);
        update = true;
        emitLight = true;
        lightRadius = range / 4 * heatPower;
        suppressable = true;
        envEnabled |= Env.space;
        solid = true;
    }

    @Override
    public void load() {
        super.load();
        rt = new RegionsTextures(
                Core.atlas.find("sueno-demand-normal"),
                Core.atlas.find("sueno-demand")
        );
    }

    @Override
    public void drawBase(Tile tile) {
        super.drawBase(tile);
//        Draw.draw(Draw.z(), () -> {
//            Shader b = Draw.getShader();
//            Draw.shader(SShaders.normalShader, true);
//        Log.info("b " + Core.batch.getClass().getSimpleName());
//        Draw.rect(rt, tile.x, tile.y);
//            Draw.shader(b);
//        });
//        Draw.flush();
//        SShaders.normalShader.bind();
//        SShaders.normalShader.apply();
//        Draw.rect("sueno-demand-normal17", tile.drawx(), tile.drawy()+12);
//        Draw.flush();
    }

    public class CoreBuild extends CoreBlock.CoreBuild implements Heated {
        @Override
        public void updateTile() {
            if (!TemperatureController.simulationPaused) {
                for (int x = tileX()-Mathf.floor(size/2f); x < tileX()-Mathf.floor(size/2f)+size; x++) {
                    for (int y = tileY()-Mathf.floor(size/2f); y < tileY()-Mathf.floor(size/2f)+size; y++) {
                        SVars.temperatureController.setRelativeTemperatureAt(x, y, heatPower * Time.delta);
                    }
                }
            }
            super.updateTile();
        }
    }
}
