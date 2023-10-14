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
        for (int p = 0; p < SVars.temperatureController.size; p++) {
            stream.writeFloat(SVars.temperatureController.tMap.geti(p));
            stream.writeFloat(SVars.temperatureController.fMap.geti(p));
            stream.writeFloat(SVars.temperatureController.cMap.geti(p));
        }
    }


    @Override
    public void read(DataInput stream) throws IOException {
        for (int p = 0; p < SVars.temperatureController.size; p++) {
            SVars.temperatureController.tMap.seti(p, stream.readFloat());
            SVars.temperatureController.fMap.seti(p, stream.readFloat());
            SVars.temperatureController.cMap.seti(p, stream.readFloat());
        }
    }
}