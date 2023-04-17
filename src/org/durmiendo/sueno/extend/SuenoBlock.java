package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import arc.util.Time;
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

        public float temperature = 0;

        @Override
        public void update() {
            if (temperature < attributes.get(SAttributes.temperatureMin)) {
                temp();
            } else min();
            if (temperature > attributes.get(SAttributes.temperatureMax)) {
                temp();
            } else min();

        }

        public void temp() {
            health -= 50f / 60f * Time.delta;
            if (health < 0) kill();
            temperature = attributes.get(SAttributes.temperatureMax);
        }
        public void min() {
            temperature -= 3.4f / 60f * Time.delta;
        }
    }
}