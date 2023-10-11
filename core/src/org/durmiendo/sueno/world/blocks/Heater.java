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

    public class HeatBuild extends Building implements Ranged {

        @Override
        public float range() {
            return range;
        }

        @Override
        public void updateTile() {
            for (float x = this.x - range / 16; x < this.x + range / 16; x += 0.01f) {
                for (float y = this.y - range / 16; y < this.y + range / 16; y += 0.01f) {
                    if (true) {//(SVars.temperatureController.cMap.getUnit((int) x, (int) y) > SVars.tMap.getUnit((int) x, (int) y)) {
                        SVars.tMap.setValue((int) x, (int) y, 1);
                    }
                }
            }
            super.updateTile();

        }

        @Override
        public void update() {
            super.update();
        }


        @Override
        public void drawSelect(){
            //indexer.eachBlock(this.team, new Rect(x-range/2, y-range/2, range, range), other -> true, othert -> Drawf.selected(othert, Tmp.c1.set(Color.orange).a(Mathf.absin(4f, 1f))));
            Drawf.dashSquare(Color.orange, x, y, range);
        }
    }
}
