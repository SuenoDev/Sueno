package org.durmiendo.sueno.meta;


import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class SStat extends Stat {

    // public static final Seq<Stat> all = new Seq<>(); not usege

    public static final Stat

    temperature = new Stat("temperature", StatCat.general);
    //voltage = new Stat("Voltage", StatCat.power),
    //amperage = new Stat("amperage", StatCat.power);

    public SStat(String name) {
        super(name);
    }
}
