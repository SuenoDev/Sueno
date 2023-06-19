package org.durmiendo.sueno.content.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.ui.Bar;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.basic.SBuilding;
import org.durmiendo.sueno.basic.SuenoBlock;
import org.durmiendo.sueno.basic.SuenoStat;
import org.durmiendo.sueno.content.SAttributes;



public class Heater extends SuenoBlock {
    public Heater(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        priority = TargetPriority.core;
        category = Category.defense;
        //it's a wall of course it's supported everywhere
        //envEnabled = Env.any;
    }
    @Override
    public void setStats() {
        super.setStats();
        this.stats.add(SuenoStat.temperature, "minimum @", this.attributes.get(SAttributes.temperatureMin));
    }


    @Override
    public void setBars() {
        super.setBars();

        addBar("temperatureBar", (SuenoBlockBuild entity) -> new Bar(
                () -> "Temperature " + String.format("%.1f",entity.temperature),
                () -> Color.cyan,
                () -> ((entity.temperature - min) / (30 - min))
        ));
    }

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;
        public int rad = 60;


        @Override
        public void update() {

            tile.circle((int) (rad / 7.501f), (b) -> {
                if(b.build instanceof SBuilding sb){
                    sb.isHeated = true;
                }
            });

            temp();
        }

        public void temp() {
            //int damage = 0;
            if (temperature <= min) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }

            if (temperature >= max) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }
            //return damage;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.z(Layer.end);

            Draw.color(new Color(0xdddd7933));

            Fill.circle(x, y, rad);

            Draw.reset();
        }

        @Override
        public void readBase(Reads read) {
            this.temperature = read.f();
            super.readBase(read);
        }

        @Override
        public void writeBase(Writes write) {
            write.f(temperature);
            super.writeBase(write);
        }
    }
}