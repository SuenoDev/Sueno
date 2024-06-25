package org.durmiendo.sueno.meta;


import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class SStat extends Stat {
    // public static final Seq<Stat> all = new Seq<>(); not used
    public static final Stat

            temperature = new Stat("temperature", StatCat.general);

    public SStat(String name) {
        super(name);
    }
}
