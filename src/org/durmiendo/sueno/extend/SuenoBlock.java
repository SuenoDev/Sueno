package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import arc.util.Timer;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SAttributes;



public class SuenoBlock extends Block  {



    public SuenoBlock(String name) {
        super(name);
        size = 1;
    }




    @Override
    public void setStats(){
        super.setStats();
        this.stats.add(SuenoStat.temperature, " from @ to @", this.attributes.get(SAttributes.temperatureMin), this.attributes.get(SAttributes.temperatureMax));
    }


    @Override
    public void setBars(){
        super.setBars();

        addBar("temperature", entity -> new Bar(
                () -> "Temperature",
                () -> Color.white,
                () -> (attributes.get(SAttributes.temperature) + Math.abs(attributes.get(SAttributes.temperatureMin)))/((attributes.get(SAttributes.temperatureMax) + Math.abs(attributes.get(SAttributes.temperatureMin)))/100)/100
        ));

    }

    @Override
    public void init() {
        Timer.schedule(
                () -> attributes.set(SAttributes.temperature, attributes.get(SAttributes.temperature) - 0.1f), 0.2f
        );
    }

}
