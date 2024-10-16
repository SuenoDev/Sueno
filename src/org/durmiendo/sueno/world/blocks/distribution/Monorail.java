package org.durmiendo.sueno.world.blocks.distribution;

import arc.util.Nullable;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

public class Monorail extends Block {



    public @Nullable MonorailBuild lastBuild;
    public Monorail(String name) {
        super(name);
        this.update = true;
        this.solid = true;
        this.underBullets = true;
        this.hasPower = true;
        this.itemCapacity = 10;
        this.configurable = true;
        this.hasItems = true;
        this.unloadable = false;
        this.group = BlockGroup.transportation;
        this.noUpdateDisabled = true;
        this.copyConfig = false;
        this.allowConfigInventory = false;
        this.priority = -1.0F;
        rotate = true;
    }

    public boolean outputsItems() {
        return true;
    }

    public class MonorailBuild extends Building {
        public @Nullable MonorailBuild p0;
        public @Nullable MonorailBuild p1;
        public @Nullable MonorailBuild p2;

        float d = 0;


        public MonorailBuild() {

        }

        @Override
        public void pickedUp(){

        }


        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(other instanceof MonorailBuild b){
//                setEnd(b);
                return true;
            }

            return true;
        }


        @Override
        public void draw() {
            super.draw();

//            if (endx != 1 && endy != 1) {
//                Draw.z(Layer.power);
//
//                Draw.color(Color.blue);
//                SDraw.curve(x, y, px, py, ppx, ppy, endx, endy, 20);
//
//            }
//
//
//        }
//
//        public void setEnd(MonorailBuild b){
//            endx = x;
//            endy = y;
//
//            d = Mathf.dst(this.x, this.y, x, y);
        }
    }
}
