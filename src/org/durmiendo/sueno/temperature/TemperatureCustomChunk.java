package org.durmiendo.sueno.temperature;

import arc.struct.IntMap;
import arc.struct.ObjectIntMap;
import arc.util.Time;
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

        try {
            if (SVars.temperatureController == null || !SPlanets.hielo.equals(Vars.state.rules.planet)) {
                writes.bool(false);
                writes.close();
                //SLog.info("writing Temperature chunk: false");
                return;
            } else {
                //SLog.info("writing Temperature chunk: true");
                writes.bool(true);
            }
        } catch (Exception e) {
            //SLog.error("writing save bool variable");
            throw new RuntimeException(e);
        }

        try {
            baseWrite(writes);
        } catch (Exception e) {
            //SLog.error("writing Temperature chunk");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInput stream) {
        Reads reads = new Reads(stream);

        try {
            boolean t = reads.bool();
            //SLog.info("Temperature chunk loaded: " + t);
            if (!t) {
                reads.close();
                return;
            }
        } catch (Exception e) {
            //SLog.error("reading save bool variable");
            throw new RuntimeException(e);
        }

        try {
            baseRead(reads);
        } catch (Exception e) {
            //SLog.error("reading Temperature chunk");
            throw new RuntimeException(e);
        }

    }

    public void baseWrite(Writes writes) {
        //SLog.mark();
        writes.i(SVars.temperatureController.getWidth());
        writes.i(SVars.temperatureController.getHeight());
        //SLog.einfo("writing w and h");

        Time.mark();
        for (float[] i : SVars.temperatureController.getCurrentTemperatureGrid()) {
            for (float j : i) {
                writes.f(j);
            }
        }
        //SLog.einfoElapsed("writing temperature");

        IntMap<Float> unitMap = SVars.temperatureController.getUnitTemperatures();
        writes.i(unitMap.size);
        //SLog.einfo("writing units save size");

        //SLog.mark();
        IntMap.Keys keys = unitMap.keys();
        while (keys.hasNext) {
            int key = keys.next();
            writes.i(key);
            writes.f(unitMap.get(key));
        }
        //SLog.einfoElapsed("writing units save");

        writes.close();
        //SLog.einfoElapsed("write temperature chunk");
    }

    public void baseRead(Reads reads) {
        //SLog.mark();
        int w = reads.i();
        int h = reads.i();
        //SLog.einfo("reading w = @ and h = @", w, h);

//        if (SVars.temperatureController == null) SVars.temperatureController = new TemperatureController();
        SVars.temperatureController.init(w, h);
        //SLog.einfo("temperature controller inited");

        if (Vars.state.isEditor()) {
            reads.close();
            //SLog.einfo("not reading temperature chunk in editor");
            return;
        }

        //SLog.mark();
        float[][] grid = SVars.temperatureController.getCurrentTemperatureGrid();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                grid[i][j] = reads.f();
            }
        }
        //SLog.einfoElapsed("setting temperature chunk");

        int size = reads.i();
        //SLog.einfo("reading units save size = @", size);

        //SLog.mark();
        IntMap<Float> unitMap = SVars.temperatureController.getUnitTemperatures();
        for (int i = 0; i < size; i++) {
            int id = reads.i();
            float t = reads.f();
            unitMap.put(id, t);
        }
        //SLog.einfoElapsed("reading units save");

        reads.close();
        //SLog.einfoElapsed("read temperature chunk");
    }
}
