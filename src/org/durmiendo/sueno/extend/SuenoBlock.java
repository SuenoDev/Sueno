package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SAttributes;

import static java.lang.Math.round;


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

        addBar("temperature", (SuenoBlockBuild entity) -> new Bar(
                () -> "Temperature " + String.format("%.1f",entity.temperature),
                () -> Color.cyan,
                () -> ((entity.temperature - attributes.get(SAttributes.temperatureMin)) / (attributes.get(SAttributes.temperatureMax) - attributes.get(SAttributes.temperatureMin)))
        ));
    }

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;

        @Override
        public void update() {
            temperature -= 1.3f / 60f * Time.delta;
        }
    }
}