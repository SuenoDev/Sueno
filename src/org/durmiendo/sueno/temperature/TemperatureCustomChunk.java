package org.durmiendo.sueno.temperature;

import arc.util.Log;
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
        if (Vars.state.rules.planet != SPlanets.hielo) {
            writes.close();
            return;
        }

        try {
            writes.bool(true);
            Log.info("Error writing save bool variable");
        } catch (Exception e) {
            return;
        }
        try {
            baseWrite(writes);
        } catch (Exception e) {
            writes.close();
            Log.info("Error writing temperature custom chunk");
        }

    }

    @Override
    public void read(DataInput stream) {
        Reads reads = new Reads(stream);

        if (Vars.state.rules.planet != SPlanets.hielo) {
            reads.close();
            return;
        }

        SVars.temperatureController.init();

        try {
            boolean t = reads.bool();
            Log.info("Temperature custom chunk loaded: " + t);
            if (t == false) {
                reads.close();
                return;
            }
        } catch (Exception e) {
            reads.close();
            Log.info("Error reading save bool variable");
            return;
        }

        try {
            baseRead(reads);
        } catch (Exception e) {
            reads.close();
            Log.info("Error reading temperature custom chunk");
        }
    }

    public void baseWrite(Writes writes) {
        if (SVars.temperatureController.temperature == null) SVars.temperatureController.init();

        writes.i(SVars.TC.width);
        writes.i(SVars.TC.height);


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

    public void baseRead(Reads reads) {
        int w = reads.i();
        int h = reads.i();

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
