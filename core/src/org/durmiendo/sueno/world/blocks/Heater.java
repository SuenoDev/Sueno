package org.durmiendo.sueno.world.blocks;

import arc.Core;
import arc.math.geom.Geometry;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.SInterp;
import org.durmiendo.sueno.world.blocks.build.Heated;

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
        configurable = true;

    }

    public class HeatBuild extends Building implements Heated {
        public float tpower = 1;

        public float range() {
            return range;
        }

        @Override
        public void updateTile() {
            if (!SVars.tempController.stop) {
                for (int x = tileX(); x < tileX()+size; x++) {
                    for (int y = tileY(); y < tileY()+size; y++) {
                        SVars.tempController.at(
                                x, y,

                                heatPower/size/16f*tpower*efficiency*
                                        (SInterp.recession.apply(
                                                0+SVars.def, SVars.maxSafeTemperature+SVars.def,
                                                SVars.tempController.at(x,y)
                                        )+1f)
                        );
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
            //Drawf.dashSquare(Color.orange, x, y, range);
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.setBackground(Core.atlas.drawable("sueno-black75"));


            Label label = new Label("Сила нагревателя " + Strings.fixed(tpower, 2) + "%");
            Slider slider = new Slider(0, 1, 0.01f, false);
            slider.setValue(tpower);
            slider.changed(() -> {
                tpower = slider.getValue();
                label.setText("Сила нагревателя " + Strings.fixed(tpower, 2)  + "%");
            });
            table.row();
            table.add(label);
            table.row();
            table.add(slider);
            table.row();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(tpower);
        }

        @Override
        public void read(Reads read) {
            super.read(read);
            tpower = read.f();
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            tpower = read.f();
        }
    }
}
