package org.durmiendo.sueno.temperature;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.io.SaveFileReader;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.core.SVars;

import java.io.DataInput;
import java.io.DataOutput;

public class TemperatureCustomChunk implements SaveFileReader.CustomChunk {

    @Override
    public void write(DataOutput stream) {
        Writes writes = new Writes(stream);
        writes.bool(true);
        writes.i(SVars.TC.width);
        writes.i(SVars.TC.height);
        if (!Vars.state.isPlaying()) {
            writes.close();
            return;
        }
        if (Vars.state.rules.planet != SPlanets.hielo) {
            writes.close();
            return;
        }
        if (SVars.TC.stop) {
            writes.close();
            return;
        }

        for (float[] i : SVars.TC.temperature) {
            for (float j : i) {
                writes.f(j);
            }
        }

        writes.i(SVars.TC.unitsTemperature.size);
        SVars.TC.unitsTemperature.each((u ,t) -> {
            writes.i(u);
            writes.f(t);
        });

        writes.close();
    }

    @Override
    public void read(DataInput stream) {
        Reads reads = new Reads(stream);
        try {
            reads.bool();
        } catch (RuntimeException e) {
            reads.close();
            return;
        }
        int w = reads.i();
        int h = reads.i();
        if (!Vars.state.isPlaying()) {
            reads.close();
            return;
        }
        if (Vars.state.rules.planet != SPlanets.hielo) {
            reads.close();
            return;
        }
        if (SVars.TC.stop) {
            reads.close();
            return;
        }



        SVars.TC.temperature = new float[w][h];
        for (int i = 0; i < w; i++) {
            for (int z = 0; z < h; z++) {
                SVars.TC.temperature[i][z] = reads.f();
            }
        }

        int size = reads.i();
        for (int i = 0; i < size; i++) {
            int id = reads.i();
            float t = reads.f();
            SVars.TC.unitsTemperature.put(id, t);
        }

        reads.close();
    }
}
