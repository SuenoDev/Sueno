package org.durmiendo.sueno.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.logic.LAccess;
import mindustry.logic.Ranged;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.world.blocks.build.Heated;

import static mindustry.Vars.indexer;

public class Heater extends Block {

    public float heatPower = 0.55f;
    public float range = 60;
    public float ceiling = 20;
    public Heater(String name) {
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        hasPower = true;
        hasItems = true;
        emitLight = true;
        lightRadius = range / 4 * heatPower;
        suppressable = true;
        envEnabled |= Env.space;

    }

    public class HeatBuild extends Building implements Ranged, Heated {

        @Override
        public float range() {
            return range;
        }

        @Override
        public void updateTile() {
            for (int x = (int) (tileX() - range/16 + size/2); x < (int) (tileX() + range/16+size/2); x++) {
                for (int y = (int) (tileY() - range/16 + size/2); y < (int) (tileY() + range/16+size/2); y++) {
                    if (SVars.temperatureController.tMap.get(x, y) < SVars.temperatureController.cMap.get(x, y)) {
                        SVars.temperatureController.fMap.add(x, y, heatPower*efficiency);
                    }
                }
            }
            super.updateTile();
        }

        public void addCeiling(){
            for (int x = (int) (tileX() - range/16 + size/2); x < (int) (tileX() + range/16+size/2); x++) {
                for (int y = (int) (tileY() - range/16 + size/2); y < (int) (tileY() + range/16+size/2); y++) {
                    SVars.temperatureController.cMap.add(x, y, ceiling/4);
                }
            }
        }

        @Override
        public void update() {
            super.update();
        }


        @Override
        public void drawSelect(){
            indexer.eachBlock(this.team, new Rect(x-range/2, y-range/2, range, range), other -> true, othert -> Drawf.selected(othert, Tmp.c1.set(Color.orange).a(Mathf.absin(4f, 1f))));
            Drawf.dashSquare(Color.orange, x, y, range);
        }

        @Override
        public void addCeiling() {
            for (int x = (int) (tileX() - range/16 + size/2); x < (int) (tileX() + range/16+size/2); x++) {
                for (int y = (int) (tileY() - range/16 + size/2); y < (int) (tileY() + range/16+size/2); y++) {
                    SVars.temperatureController.cMap.add(x, y, ceiling);
                }
            }
        }
    }
}
