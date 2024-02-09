package org.durmiendo.sueno.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.Colorated;
import org.durmiendo.sueno.world.blocks.build.Heated;

import static mindustry.Vars.indexer;

public class TemeperatureSource extends Block {
    @Override
    public void drawOverlay(float x, float y, int rotation) {
        super.drawOverlay(x, y, rotation);
    }

    public TemeperatureSource(String name) {
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        hasPower = true;
        hasItems = true;
        emitLight = true;
        suppressable = true;
        envEnabled |= Env.space;
        configurable = true;
    }

    public class HeatBuild extends Building implements Heated {
        public float range = 0;
        public float te = 0;

        public float range() {
            return range;
        }

        @Override
        public void draw() {
            super.draw();
            Drawf.additive(Core.atlas.find("sueno-tst"), Colorated.gradient(Color.cyan, Color.red, te/300f), x, y);
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.setBackground(Core.atlas.drawable("sueno-black75"));

            Label label = new Label("Источник температуры (блоки) " + range/16 + " блоков");
            table.add(label);
            table.row();

            Slider s = new Slider(0f, 30*16, 16f, false);
            s.setValue(range);
            s.changed(() -> {
                range = s.getValue();
                label.setText("Источник температуры (блоки) " + range/16 + " блоков");
            });
            table.add(s);
            table.row();

            //TextButton ib = new TextButton("Изменять температуру", Styles.flatTogglet);

            Slider slider = new Slider(SVars.tempTemperatureController.normalTemp, 330-SVars.tempTemperatureController.normalTemp, 5, false);
            slider.setValue(te);

            Label labels = new Label("Температура " + te + " °C");
            slider.changed(() -> {
                te = slider.getValue();
                labels.setText("Температура " + te + " °C");
            });

            table.table(t -> {
                t.add(labels);
                t.row();
                t.add(slider);
            });
        }

        @Override
        public void updateTile() {
            for (int x = (int) (tileX() - range/16 + size/2); x < (int) (tileX() + range/16+size/2); x++) {
                for (int y = (int) (tileY() - range/16 + size/2); y < (int) (tileY() + range/16+size/2); y++) {
                    SVars.tempTemperatureController.temperature[x][y] = te-SVars.tempTemperatureController.normalTemp;
                }
            }


            super.updateTile();
        }

        public void addCeiling(){}

        @Override
        public void update() {
            super.update();
        }


        @Override
        public void drawSelect(){
            indexer.eachBlock(this.team, new Rect(x-range/2, y-range/2, range, range), other -> true, other -> Drawf.selected(other, Tmp.c1.set(Colorated.gradient(Color.cyan, Color.red, (te+300)/600)).a(Mathf.absin(4f, 1f))));
            Drawf.dashSquare(Colorated.gradient(Color.cyan, Color.red, (te+300)/600), x, y, range);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(range);
            write.f(te);
        }

        @Override
        public void read(Reads read) {
            super.read(read);
            range = read.f();
            te = read.f();
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            range = read.f();
            te = read.f();
        }
    }
}