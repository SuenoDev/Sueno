package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SAttributes;




public class SuenoBlock extends Block {


    public SuenoBlock(String name) {
        super(name);
        update = true;
        size = 1;
    }


    @Override
    public void setStats() {
        super.setStats();
        this.stats.add(SuenoStat.temperature, " from @ to @", this.attributes.get(SAttributes.temperatureMin), this.attributes.get(SAttributes.temperatureMax));
    }


    @Override
    public void setBars() {
        super.setBars();

        addBar("temperatureBar", (SuenoBlockBuild entity) -> new Bar(
                () -> "Temperature " + String.format("%.1f",entity.temperature),
                () -> Color.cyan,
                () -> ((entity.temperature - attributes.get(SAttributes.temperatureMin)) / (attributes.get(SAttributes.temperatureMax) - attributes.get(SAttributes.temperatureMin)))
        ));
    }

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;

        @Override
        public void update() {
            if ((temperature < attributes.get(SAttributes.temperatureMin)) || (temperature > attributes.get(SAttributes.temperatureMax))) {

                temp();
                temperature = Math.round(temperature);
            } else min();

        }

        public void temp() {
            health -= 50f / 60f * Time.delta;
            if (health < 0) kill();
        }
        public void min() {

            temperature -= 3.4f / 60f * Time.delta;
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