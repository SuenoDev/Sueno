package org.durmiendo.sueno.temperature;

import arc.Core;
import arc.Events;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Tile;
import org.durmiendo.sueno.content.SPlanets;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.Objects;

public class Controller implements SaveFileReader.CustomChunk {
    public static Controller instance;
    public boolean stop = false;
    public FrameBuffer buffer = new FrameBuffer(Core.graphics.getWidth(), Core.graphics.getHeight());
    public Shader shader = createShader();

    public float[][] temperature, prev;
    public HashMap<Unit, Float> units;
    public float normalTemp = -30f; // absolute
    public float maxTemp = 30f; // relative
    public int width, height;
    public int unitsAmount = 0;

    public long time;

    public Controller() {
        instance = this;
        SaveVersion.addCustomChunk("sueno-temperature-chunk", this);
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.Trigger.draw, this::draw);
        Events.on(EventType.WorldLoadEvent.class, e -> {
            if(Vars.state.isEditor()) return;
            init();
        });

        Events.on(EventType.UnitSpawnEvent.class, e -> {
            units.put(e.unit, normalTemp);
            unitsAmount++;
        });

        Events.on(EventType.UnitDestroyEvent.class, e -> {
            units.remove(e.unit);
            unitsAmount--;
        });
    }

    public void init() {
        temperature = new float[Vars.world.width()][Vars.world.height()];
        prev = new float[Vars.world.width()][Vars.world.height()];
        width = Vars.world.width();
        height = Vars.world.height();
        Groups.unit.each(u -> units.put(u, normalTemp));
        Vars.content.units().each(ut -> ut.abilities.add(new Ability()));
        unitsAmount = Groups.unit.size();
    }

    public void draw() {
        // TODO not works
        /*if (!Vars.state.isGame() || Reflect.<Float>get(Vars.renderer, "landTime") > 0) return;

        float z = Draw.z();
        Draw.z(Layer.flyingUnit+0.2f);

        Rect bounds = Core.camera.bounds(new Rect()).grow(1 / 8f);
        int cx = (int) bounds.x;
        int cy = (int) bounds.y;
        int cw = (int) bounds.width;
        int ch = (int) bounds.height;

        for (int x = cx; x < cx + cw; x++) {
            for (int y = cy; y < cy + ch; y++) {
                float v = at(x, y);
                v = Mathf.clamp(v / maxTemp);
                Draw.color(v, 0, 1 - v, 0.2f);
                Fill.rect(x, y, 1, 1);
            }
        }

        Draw.z(z);*/
    }

    public void update() {
        if (!Vars.state.isPlaying()) return;
        if (!Objects.equals(Vars.state.rules.planet.name, SPlanets.hielo.name)) return;
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
        if (unitsAmount > 0) {
            for (Unit u : units.keySet()) {
                float t = at(u);
                Tile tile = u.tileOn();
                if (tile == null) continue;
                float td = (t - at(tile.x, tile.y)) * 0.01f * Time.delta;
                at(u, td);
                at(tile.x, tile.y, -td);
            }
        }

        time = Time.timeSinceMillis(startTime);
    }

    /** Returns relative temperature. Use it in math. **/
    public float at(int x, int y) {
        return check(x, y) ? temperature[x][y] : 0;
    }

    /** Returns relative temperature. Use it in math. **/
    public void at(int x, int y, float increment) {
        if (!check(x, y)) return;
        temperature[x][y] += increment;
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(int x, int y) {
        return (check(x, y) ? temperature[x][y] : 0) + normalTemp;
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public void temperatureAt(int x, int y, float increment) {
        if (!check(x, y)) return;
        temperature[x][y] += increment;
    }

    public float at(Unit u) {
        return units.get(u);
    }

    /** Returns relative temperature. Use it in math. **/
    public void at(Unit u, float increment) {
        units.put(u, units.get(u) + increment);
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(Unit u) {
        return units.get(u);
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public void temperatureAt(Unit u, float increment) {
        units.put(u, units.get(u) + increment);
    }


    public boolean check(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height;
    }

    @Override
    public void write(DataOutput stream) {
        Writes writes = new Writes(stream);

        writes.i(width);
        writes.i(height);

        for (float[] i : temperature) {
            for (float j : i) {
                writes.f(j);
            }
        }

        writes.i(unitsAmount);
        if (unitsAmount > 0) {
            units.forEach((u, t) -> {
                writes.i(u.id());
                writes.f(t);
            });
        }
        writes.close();
    }

    @Override
    public void read(DataInput stream) {
        Reads reads = new Reads(stream);

        int w = reads.i();
        int h = reads.i();

        temperature = new float[w][h];

        for (int i = 0; i < w; i++) {
            for (int z = 0; z < h; z++) {
                temperature[i][z] = reads.f();
            }
        }

        unitsAmount = reads.i();
        if (unitsAmount > 0) {
            units = new HashMap<>();
            for (int i = 0; i < unitsAmount; i++) {
                units.put(Groups.unit.getByID(reads.i()), reads.f());
            }
        }

        reads.close();
    }

    public static Shader createShader(){
        return new Shader(
                "attribute vec4 a_position;\n" +
                        "attribute vec2 a_texCoord0;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "   v_texCoords = a_texCoord0;\n" +
                        "   gl_Position = a_position;\n" +
                        "}",
                "uniform sampler2D u_texture;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "void main(){\n" +
                        "  float c = texture2D(u_texture, v_texCoords).x;\n" +
                        "  gl_FragColor = vec4(c, 0.0, 1.0 - c, 0.5f);\n" +
                        "}"
        );
    }
}
