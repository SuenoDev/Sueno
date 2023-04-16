package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SAttributes;

import java.awt.*;

public class SuenoBlock extends Block  {



    public SuenoBlock(String name) {
        super(name);
        size = 1;
    }

    @Override
    public void setStats(){
        super.setStats();
        this.stats.add(SuenoStat.temperature, " от @ до @",  new Object[]{(Object) this.attributes.get(SAttributes.temperatureMax), (Object) this.attributes.get(SAttributes.temperatureMin)});
    }


    @Override
    public void setBars(){
        super.setBars();

        addBar("temperature", entity -> new Bar(
                () -> "Temperature",
                () -> Color.white,
                () -> (float)this.attributes.get(SAttributes.temperature))
        );
    }
}
