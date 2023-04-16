package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import org.durmiendo.sueno.content.SAttributes;

import java.awt.*;

public class SuenoBlock extends Block  {

    public int size = 1;



    public SuenoBlock(String name) {
        super(name);

    }

    @Override
    public void setStats(){

        this.stats.add(SuenoStat.temperature, "@/@",  new Object[]{(Object) this.attributes.get(SAttributes.temperatureMax), (Object) this.attributes.get(SAttributes.temperatureMin)});

        if(synthetic()){
            stats.add(Stat.health, health, StatUnit.none);
            if(armor > 0){
                stats.add(Stat.armor, armor, StatUnit.none);
            }
        }


        if(canBeBuilt() && requirements.length > 0){
            stats.add(Stat.buildTime, buildCost / 60, StatUnit.seconds);
            stats.add(Stat.buildCost, StatValues.items(false, requirements));
        }

        if(instantTransfer){
            stats.add(Stat.maxConsecutive, 2, StatUnit.none);
        }

        for(var c : consumers){
            c.display(stats);
        }

        if(hasLiquids) stats.add(Stat.liquidCapacity, liquidCapacity, StatUnit.liquidUnits);
        if(hasItems && itemCapacity > 0) stats.add(Stat.itemCapacity, itemCapacity, StatUnit.items);
    }


    @Override
    public void setBars(){
        super.setBars();

        addBar("temperature", entity -> new Bar(
                () -> "Temperature",
                () -> Color.white,
                () -> (float)this.temperature)
        );
    }
}
