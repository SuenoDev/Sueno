package org.durmiendo.sueno.io;

import mindustry.io.SaveFileReader;
import org.durmiendo.sueno.core.SVars;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SChunk implements SaveFileReader.CustomChunk {
    @Override
    public void write(DataOutput stream) throws IOException {
        int size = SVars.mineralController.getMinSeq().size;
        stream.write(size);

        for (int i = 0; size > i; i++) {
            stream.writeShort(SVars.mineralController.getMinIndex(i));
        }
    }


    @Override
    public void read(DataInput stream) throws IOException {
        int size = stream.readInt();

        for (int i = 0; size > i; i++) {
            SVars.mineralController.setMinOnIndex(i, stream.readShort());
        }
    }
}