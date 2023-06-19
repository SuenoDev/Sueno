package org.durmiendo.sueno.basic;

import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import org.durmiendo.sueno.SVars;

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

