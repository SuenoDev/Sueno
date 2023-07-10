package org.durmiendo.sueno.minerals;

import arc.struct.Seq;
import mindustry.type.Item;
import org.durmiendo.sueno.content.SItems;

public class Minerals {
    public Seq<Item> minerals;


    public void init() {
        minerals.add(SItems.avilium);
        minerals.add(SItems.indiganit);
        minerals.add(SItems.silitranium);
    }

    public int getMineralIndex(Item  item) {
        return minerals.indexOf(item);
    }
}
