package org.durmiendo.sueno.mainContents;

import arc.struct.Seq;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class SuenoStat extends Stat {

    public static final Seq<Stat> all = new Seq<>();

    public static final Stat

    temperature = new Stat("temperature", StatCat.general);
    //voltage = new Stat("Voltage", StatCat.power),
    //amperage = new Stat("amperage", StatCat.power);

    public SuenoStat(String name) {
        super(name);
    }

}
