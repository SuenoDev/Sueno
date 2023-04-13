package org.durmiendo.sueno.extend;

import arc.Core;
import arc.func.Func;
import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.core.UI;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquids;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

import java.awt.*;

public class SuenoBlock extends Block  {

    public int sizeX = 1;
    public int sizeY = 1;

    public float temperature = 0;
    public float temperatureMax = 200;
    public float temperatureMin = -200;
    public int size = (int) Math.sqrt(sizeX*sizeY);



    public SuenoBlock(String name) {
        super(name);

    }

    @Override
    public void setStats(){

        this.stats.add(Stat.size, "@x@", new Object[]{(Object) sizeX, (Object) sizeY});
        this.stats.add(SuenoStat.temperature, "@/@",  new Object[]{(Object) this.temperatureMax, (Object) this.temperatureMin});

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
