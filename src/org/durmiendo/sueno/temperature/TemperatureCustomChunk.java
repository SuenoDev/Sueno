package org.durmiendo.sueno.temperature;

import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.io.SaveFileReader;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.utils.SLog;

import java.io.DataInput;
import java.io.DataOutput;

public class TemperatureCustomChunk implements SaveFileReader.CustomChunk {

    @Override
    public void write(DataOutput stream) {
        Writes writes = new Writes(stream);

        try {
            if (SVars.temperatureController == null || Vars.state.isEditor()) {
                writes.bool(false);
                writes.close();
                SLog.info("writing Temperature chunk: false");
                return;
            } else {
                SLog.info("writing Temperature chunk: true");
                writes.bool(true);
            }
        } catch (Exception e) {
            SLog.error("writing save bool variable");
            throw new RuntimeException(e);
        }

        try {
            baseWrite(writes);
        } catch (Exception e) {
            SLog.error("writing Temperature chunk");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInput stream) {
        Reads reads = new Reads(stream);

        try {
            boolean t = reads.bool();
            SLog.info("Temperature chunk loaded: " + t);
            if (t == false) {
                reads.close();
                SVars.temperatureController = new TemperatureController();
                SVars.temperatureController.init(Vars.world.width(), Vars.world.height());
                return;
            }
        } catch (Exception e) {
            SLog.error("reading save bool variable");
            throw new RuntimeException(e);
        }

        try {
            baseRead(reads);
        } catch (Exception e) {
            SLog.error("reading Temperature chunk");
            throw new RuntimeException(e);
        }

    }

    public void baseWrite(Writes writes) {
        writes.i(Vars.world.width());
        writes.i(Vars.world.height());

        for (float[] i : SVars.temperatureController.temperature) {
            for (float j : i) {
                writes.f(j);
            }
        }

        writes.i(SVars.temperatureController.unitsTemperature.size);
        SVars.temperatureController.unitsTemperature.each((u ,t) -> {
            writes.i(u);
            writes.f(t);
        });

        writes.close();
    }

    public void baseRead(Reads reads) {
        int w = reads.i();
        int h = reads.i();

        SVars.temperatureController = new TemperatureController();
        SVars.temperatureController.init(w, h);

        if (Vars.state.isEditor()) {
            reads.close();
            return;
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                SVars.temperatureController.set(i, j, reads.f());
            }
        }

        int size = reads.i();
        for (int i = 0; i < size; i++) {
            int id = reads.i();
            float t = reads.f();
            SVars.temperatureController.unitsTemperature.put(id, t);
        }

        reads.close();
    }
}
