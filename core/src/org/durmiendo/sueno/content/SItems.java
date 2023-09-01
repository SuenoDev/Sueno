package org.durmiendo.sueno.content;

import mindustry.type.Item;

public class SItems {

    public static Item
    nickel,
    magnetite,
    invar,
    superInvar,
    cobalt,
    ice;


    public static void load() {
        nickel = new Item("nickel");
        magnetite = new Item("magnetite");
        invar = new Item("invar");
        superInvar = new Item("superInvar");
        cobalt = new Item("cobalt");
        ice = new Item("ice");
    }
}
