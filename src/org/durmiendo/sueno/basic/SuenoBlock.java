package org.durmiendo.sueno.basic;


import arc.graphics.Color;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.ui.Bar;
import mindustry.world.Block;

import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.content.SAttributes;
import org.durmiendo.sueno.content.blocks.Heater;


public class SuenoBlock extends Block {

    public float min = -200;
    public Category category = Category.defense;
    public SuenoBlock(String name) {
        super(name);
        update = true;
        size = 1;
    }
}