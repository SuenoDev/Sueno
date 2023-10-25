package org.durmiendo.sueno.temperature;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.ScissorStack;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Rect;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.graphics.Layer;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Controller implements SaveFileReader.CustomChunk {
    public static Controller instance;

    // TODO we really need it?
    public static void addBars() {

    }

    public FrameBuffer buffer = new FrameBuffer(Core.graphics.getWidth(), Core.graphics.getHeight());
    public Shader shader = createShader();

    public float[][] temperature, prev;
    public float normalTemp = -30f; // absolute
    public float maxTemp = 30f; // relative
    public int width, height;

    public long time;

    public Controller() {
        instance = this;
        SaveVersion.addCustomChunk("sueno-temperature-chunk", this);
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.Trigger.draw, this::draw);
        Events.on(EventType.WorldLoadEvent.class, e -> {
            temperature = new float[Vars.world.width()][Vars.world.height()];
            prev = new float[Vars.world.width()][Vars.world.height()];
            width = Vars.world.width();
            height = Vars.world.height();
        });
    }

    public void draw() {

    }

    public void update() {
        if (!Vars.state.isPlaying()) return;

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

        Tile t = Vars.player.team().core().tileOn();
        at(t.x, t.y, 3.5f * Time.delta);

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

    public boolean check(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height;
    }

    @Override
    public void write(DataOutput stream) {
        Writes writes = new Writes(stream);

        writes.i(width);
        writes.i(height);

        for (int i = 0; i < temperature.length; i++) {
            for (int j = 0; j < temperature[i].length; j++) {
                writes.f(temperature[i][j]);
            }
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
