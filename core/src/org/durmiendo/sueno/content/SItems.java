package org.durmiendo.sueno.content;

import mindustry.type.Item;

public class SItems {

    public static Item
    nickel,
    magnetite,

    invar,
    vanadium,
    kalite,
    nitium,
    realite;



    public static void load() {
        nickel = new Item("nickel");
        magnetite = new Item("magnetite");
        invar = new Item("invar");
        kalite = new Item("kalite");
        nitium = new Item("nitium");
        realite = new Item("realite");
    }
}
