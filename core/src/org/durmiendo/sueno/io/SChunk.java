package org.durmiendo.sueno.io;

import mindustry.Vars;
import mindustry.io.SaveFileReader;
import org.durmiendo.sueno.core.SVars;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SChunk implements SaveFileReader.CustomChunk {
    @Override
    public void write(DataOutput stream) throws IOException {
        for (int p = 0; p < SVars.temperatureController.tMap.value.size; p++) {
            stream.writeFloat(SVars.temperatureController.tMap.getUnit(p));
            stream.writeFloat(SVars.temperatureController.fMap.getUnit(p));
            stream.writeFloat(SVars.temperatureController.cMap.getUnit(p));
        }
    }


    @Override
    public void read(DataInput stream) throws IOException {
        for (int p = 0; p < SVars.temperatureController.tMap.value.size; p++) {
            SVars.temperatureController.tMap.setValue(p, stream.readFloat());
            SVars.temperatureController.fMap.setValue(p, stream.readFloat());
            SVars.temperatureController.cMap.setValue(p, stream.readFloat());
        }
    }
}