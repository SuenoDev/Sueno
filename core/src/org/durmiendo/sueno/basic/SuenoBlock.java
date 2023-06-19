package org.durmiendo.sueno.basic;


import mindustry.type.Category;
import mindustry.world.Block;

// TODO remove this poor

public class SuenoBlock extends Block {

    public float min = -200;
    public float max = 300;
    public Category category = Category.defense;
    public SuenoBlock(String name) {
        super(name);
        update = true;
        size = 1;
    }
}