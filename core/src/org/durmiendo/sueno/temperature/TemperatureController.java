package org.durmiendo.sueno.temperature;

import arc.Events;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.ObjectMap;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sueno.content.SPlanets;

import java.io.DataInput;
import java.io.DataOutput;

public class TemperatureController implements SaveFileReader.CustomChunk {
    //temperature params
    // TODO а чо они статичные???
    // TODO хз, 1 температура на 1 миндаль
    // TODO точно я же их из варсов спиздил, и забил хуй на модификаторы
    public static float freezingDamage = 0.35f;
    public static float freezingPower = -1.4f;
    public static float startTemperature = 0;
    public static float minEffectivityTemperature = 100;
    public static float minSafeTemperature = -100;
    public static float minTemperatureDamage = 20;
    public static float maxFreezingSpeed = 2f;
    public static float maxSafeTemperature = 120;
    public static float maxHeatDamage = 300;
    public static float maxBoost = 20;
    public static boolean isDevTemperature = true;
    public static float def = 30;
    public boolean stop = false;

    public static TemperatureController instance;

    public float[][] temperature, prev;
    public ObjectMap<Integer, Float> unitsTemperature = new ObjectMap<>();

    public float normalTemp = -30f; // absolute only for GUI. DON'T USE IN MATH

    public int width, height;

    public long time;

    float defaultTemperatureConductivity = 0.5f;
    float unitsDefaultTemperatureConductivity = defaultTemperatureConductivity / 1.5f;
    public ObjectMap<Object, Float> temperatureConductivity = ObjectMap.of( // TODO fill this list with unique values

    );
    public float getTemperatureConductivity(Object obj) {
        if (temperatureConductivity.containsKey(obj))
            return temperatureConductivity.get(obj);
        else {
            if (obj instanceof Block block) {
                return defaultTemperatureConductivity;
            } else if (obj instanceof Unit unit) {
                return unitsDefaultTemperatureConductivity;
            }
        }
        return 0f;
    }

    public Block getBlockAt(int x, int y) {
        if (!check(x, y)) return Blocks.air;
        Tile tileAt = Vars.world.tile(x, y);
        return tileAt.build != null ? tileAt.build.block() : tileAt.floor();
    }

    public TemperatureController() {
        instance = this;
        SaveVersion.addCustomChunk("sueno-temperature-chunk", this);
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.Trigger.draw, this::draw);
        Events.on(EventType.WorldLoadEvent.class, e -> {
            if(Vars.state.isEditor()) return;
            init();
        });
    }

    public void init() {
        unitsTemperature.clear();
        temperature = new float[Vars.world.width()][Vars.world.height()];
        prev = new float[Vars.world.width()][Vars.world.height()];
        width = Vars.world.width();
        height = Vars.world.height();
    }

    public void draw() {

    }

    public void update() {
        if (!Vars.state.isPlaying()) return;
        if (Vars.state.rules.planet != SPlanets.hielo) return;
        if (stop) return;

        long startTime = Time.millis();

        for (int i = 0; i < width; i++) {
            System.arraycopy(temperature[i], 0, prev[i], 0, height);
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int z = 0; z < 4; z++) {
                    Point2 offset = Geometry.d4(z);
                    int xx = i + offset.x;
                    int yy = j + offset.y;

                    if (!check(xx, yy)) continue;

                    float td = (prev[i][j] - prev[xx][yy]) * 0.01f * Time.delta;
                    at(i, j, -td);
                    at(xx, yy, td);
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                at(i,j, freezingPower*Time.delta*at(i,j)/ maxSafeTemperature/8f);
            }
        }

        if (Groups.unit.size() < unitsTemperature.size) {
            unitsTemperature.each((i, t) -> {
                if (!Groups.unit.contains(u -> u.id == i))
                    unitsTemperature.remove(i);
            });
        }

        Groups.unit.each(unit -> {
            int ux = unit.tileX(), uy = unit.tileY();
            if (check(ux, uy)) {
                if (!unitsTemperature.containsKey(unit.id))
                    unitsTemperature.put(unit.id, at(ux, uy));
                else {
                    float td = (unitsTemperature.get(unit.id) - prev[ux][uy]) * 0.01f * Time.delta/2f;
                    at(unit, -td);
                    above(unit, td);

                    td = (prev[ux][uy] - unitsTemperature.get(unit.id)) * 0.01f * Time.delta/2f;
                    above(unit, -td);
                    at(unit, td);
                }
            }
        });

        time = Time.timeSinceMillis(startTime);
    }

    /** Returns relative temperature. Use it in math. **/
    public float at(int x, int y) {
        return check(x, y) ? temperature[x][y] : 0;
    }

    /** Increments relative temperature. Use it in math. **/
    public void at(int x, int y, float increment) {
        if (!check(x, y)) return;
        temperature[x][y] += increment;
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(int x, int y) {
        if(isDevTemperature) return at(x, y);
        return at(x, y) + normalTemp;
    }

    /** Returns relative temperature. Use it in math. **/
    public float above(Unit unit) {
        return at(unit.tileX(), unit.tileY());
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAbove(Unit unit) {
        return temperatureAt(unit.tileX(), unit.tileY());
    }

    /** Increments relative temperature. Use it in math. **/
    public void above(Unit unit, float increment) {
        at(unit.tileX(), unit.tileY(), increment);
    }

    /** Returns relative temperature. Use it in math. **/
    public float at(Unit u) {
        return unitsTemperature.get(u.id, 0f);
    }

    /** Increments relative temperature. Use it in math. **/
    public void at(Unit u, float increment) {
        Float t = unitsTemperature.get(u.id);
        if (t == null) {
            unitsTemperature.put(u.id, increment);
            return;
        }
        unitsTemperature.put(u.id, t + increment);
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(Unit u) {
        if (u == null) return 0;
        if (isDevTemperature) return at(u);
        return at(u) + normalTemp;
    }

    public boolean check(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height;
    }

    @Override
    public void write(DataOutput stream) {
//        if(Vars.state.isEditor()) {
//            return;
//        }
        Writes writes = new Writes(stream);

        writes.i(width);
        writes.i(height);
        for (float[] i : temperature) {
            for (float j : i) {
                writes.f(j);
            }
        }

        writes.i(unitsTemperature.size);
        unitsTemperature.each((u ,t) -> {
            writes.i(u);
            writes.f(t);
        });

        writes.close();
    }

    @Override
    public void read(DataInput stream) {
//        if(Vars.state.isEditor()) {
//            return;
//        }
        Reads reads = new Reads(stream);

        int w = reads.i();
        int h = reads.i();
        temperature = new float[w][h];
        for (int i = 0; i < w; i++) {
            for (int z = 0; z < h; z++) {
                temperature[i][z] = reads.f();
            }
        }

        int size = reads.i();
        for (int i = 0; i < size; i++) {
            int id = reads.i();
            float t = reads.f();
            unitsTemperature.put(id, t);
        }

        reads.close();
    }
}
