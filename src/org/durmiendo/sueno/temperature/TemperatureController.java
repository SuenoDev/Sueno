package org.durmiendo.sueno.temperature;

import arc.Events;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sueno.content.SPlanets;

public class TemperatureController {

    /**
     *  zone
     *
     *  main indicators of temperature
     *  all actions with temperature must be carried out taking it into account
     *  standard value 1 and always > 0
     */
    public final float tk = 1f;
    /**
     *  coefficient
     *
     *  main indicators of temperature
     *  all actions with temperature must be carried out taking it into account
     *  standard value 1 and always > 0
     */
    public final float tc = 1f;
    public final float freezingDamage = 0.35f;

    /**
     * constant temperature reduction factor
     */
    public final float freezingPower = 0; //-0.00375f;

    /**
     * standard temperature
     * if the temperature value is undefined then this value will be used
     */
    public final float standardTemperature = -tk;
    public final float minEffectivityTemperature = 0.5f;
    public final float minSafeTemperature = -0.5f;
    public final float minTemperatureDamage = 20;
    public final float maxSafeTemperature = 0.6f;
    public final float maxHeatDamage = 300;
    public final float maxBoost = 20;
    public final boolean isDevTemperature = true;
    public final float def = 30;

    // Теплопередача, TODO: костыль убрать
    public float dddd = 0.005f; //при большоих значениях tc, tk и dddd не работает адекватно

    /**
     * responsible for the temperature operating status
     * used for debugging only
     */
    public boolean stop = isDevTemperature;

    public TemperatureController instance;

    /**
     * temperature maps
     * the 'temperature' field is the current map, you should only use it
     */
    public float[][] temperature, prev;
    public ObjectMap<Integer, Float> unitsTemperature = new ObjectMap<>();

    public float normalTemp = -30f; // absolute only for GUI. DON'T USE IN MATH

    public int width, height;

    public long time;

    final float defaultTemperatureConductivity = 0.5f;
    final float unitsDefaultTemperatureConductivity = defaultTemperatureConductivity / 1.5f;
    public final ObjectMap<Object, Float> temperatureConductivity = ObjectMap.of( // TODO fill this list with unique values

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
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.Trigger.draw, this::draw);
    }

    public void init(int w, int h) {
        unitsTemperature.clear();
        temperature = new float[w][h];
        width = w;
        height = h;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                temperature[i][j] = standardTemperature;
            }
        }
        prev = new float[w][h];

    }

    public void draw() {

    }

    public void update() {
        if (Vars.state.isPaused() || Vars.state.isEditor()) return;
        if (!SPlanets.hielo.equals(Vars.state.rules.planet)) return;
        if (stop) return;

        long startTime = Time.millis();
        temperatureCalculate();
        time = Time.timeSinceMillis(startTime);
    }

    public void temperatureCalculate() {

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

                    float td = (prev[i][j] - prev[xx][yy]) * dddd * Time.delta;
                    at(i, j, -td);
                    at(xx, yy, td);
                }
                if (check(i,j)) at(i,j, freezingPower * Time.delta);
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
                    unitsTemperature.put(unit.id, standardTemperature);
                else {
                    float td = (unitsTemperature.get(unit.id) - prev[ux][uy]) * dddd * Time.delta/2f;
                    at(unit, -td);
                    above(unit, td);

                    td = (prev[ux][uy] - unitsTemperature.get(unit.id)) * dddd * Time.delta/2f;
                    above(unit, -td);
                    at(unit, td);
                }
            }
        });

    }

    /** Returns relative temperature. Use it in math. **/
    public float at(int x, int y) {
        return check(x, y) ? temperature[x][y] : standardTemperature;
    }

    /** Increments relative temperature. Use it in math. **/
    public void at(int x, int y, float increment) {
        if (!check(x, y)) return;
        float t = temperature[x][y] + increment*tc;
        if (t <= -tk) {
            temperature[x][y] = -tk;
            return;
        } else if (t >= tk) {
            temperature[x][y] = tk;
            return;
        }
        temperature[x][y] = t;
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(int x, int y) {
        if (isDevTemperature) return at(x, y);
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
        return unitsTemperature.get(u.id, standardTemperature);
    }

    /** Increments relative temperature. Use it in math. **/
    public void at(Unit u, float increment) {
        Float t = unitsTemperature.get(u.id);
        increment*=tc;
        if (t == null) {
            if (increment <= -tk) {
                unitsTemperature.put(u.id, -tk);
                return;
            } else if (increment >= tk) {
                unitsTemperature.put(u.id, tk);
                return;
            }

            unitsTemperature.put(u.id, increment);
            return;
        }
        float a = t + increment;
        if (a <= -tk) {
            unitsTemperature.put(u.id, -tk);
            return;
        } else if (a >= tk) {
            unitsTemperature.put(u.id, tk);
            return;
        }
        unitsTemperature.put(u.id, a);
    }

    /** Returns absolute temperature. USE IT ONLY IN GUI, NOT IN MATH!!! **/
    public float temperatureAt(Unit u) {
        if (u == null) return standardTemperature;
        if (isDevTemperature) return at(u);
        return at(u) + normalTemp;
    }

    public boolean check(int x, int y) {
        return x > 0 && x < width && y > 0 && y < height;
    }

    public void set(int x, int y, float f) {
        if (!check(x, y)) return;
        temperature[x][y] = f;
    }
}
