package org.durmiendo.sueno.basic;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

// TODO this poor too

public class SBuilding extends Building {
    public boolean isHeated = false;
    public float temperature = 0;




    @Override
    public void readBase(Reads read) {
        this.temperature = read.f();
        super.readBase(read);
    }

    @Override
    public void writeBase(Writes write) {
        write.f(temperature);
        super.writeBase(write);
    }
}

