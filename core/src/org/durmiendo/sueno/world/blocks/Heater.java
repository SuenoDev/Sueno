package org.durmiendo.sueno.world.blocks;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.logic.LAccess;
import mindustry.logic.Ranged;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;

import static mindustry.Vars.indexer;

public class Heater extends Block {

    public float heatPower = 0.3f;
    public float range = 60f;
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
            super.updateTile();
            for(int x = Mathf.round(this.x-range/16); x < this.x + range/16; x++) {
                for(int y = Mathf.round(this.y-range/16); y < this.y + range/16; x++) {
                    SVars.temperatureController.freezingСhange(heatPower, x/8, y/8);
                }
            }
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.progress) return Mathf.clamp(1);
            return super.sense(sensor);
        }

        @Override
        public void drawSelect(){
            indexer.eachBlock(this, range, other -> true, other -> Drawf.selected(other, Tmp.c1.set(Color.orange).a(Mathf.absin(4f, 1f))));
            Drawf.dashSquare(Color.orange, x, y, range);
        }
    }
}
