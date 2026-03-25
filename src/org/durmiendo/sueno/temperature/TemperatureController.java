package org.durmiendo.sueno.temperature;

import arc.Events;
import arc.math.Mathf;
import arc.struct.IntMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sap.SuenoSettings;
import org.durmiendo.sueno.utils.SLog;

public class TemperatureController {
    public boolean showHeatMap;
    
    public final float freezingDamage = 0.35f;
    public static float minSafeTemperature = -0.5f;
    public static float minTemperatureDamage = 20;
    public static float maxSafeTemperature = 0.6f;
    public static float maxHeatDamage = 300;
    
    public final float ambientTemperatureChangeRate = 0f;
    
    @SuenoSettings(min = -1f, max = 1f, steep = 0.01f, accuracy = 2, def = -1f, priority = 0)
    public static float defaultRelativeTemperature = -1f;
    
    public float absoluteTemperatureOffset = 30f;
    
    @SuenoSettings(min = 0f, max = 0.025f, steep = 0.001f, accuracy = 6, def = 0.005f, priority = 1)
    public static float heatTransferCoefficient = 0.005f;
    
    @SuenoSettings(def = 0)
    public static boolean simulationPaused = false;
    
    @SuenoSettings(def = 1)
    public static boolean displayRelativeTemperature = true;
    
    private int width;
    private int height;
    private long lastCalculationTimeMs;
    
    private float[][] javaGrid;
    private float[][] javaPrevGrid;
    
    private final IntMap<Float> unitTemperatures = new IntMap<>();
    
    public TemperatureController() {
        NativeTemperature.load();
        
        Events.run(EventType.Trigger.update, this::update);
    }
    
    public void init(int worldWidth, int worldHeight) {
        if (worldWidth <= 0 || worldHeight <= 0) {
            SLog.err("TemperatureController init skipped: Invalid world dimensions");
            return;
        }
        
        this.width = worldWidth;
        this.height = worldHeight;
        this.unitTemperatures.clear();
        this.lastCalculationTimeMs = 0;
        simulationPaused = false;
        
        if (NativeTemperature.isLoaded) {
            NativeTemperature.init(width, height);
            
            for (int i = 0; i < width * height; i++) {
                NativeTemperature.dataBuffer.put(i, defaultRelativeTemperature);
            }
            
            SLog.info("TemperatureController initialized (NATIVE) " + width + "x" + height);
        } else {
            SLog.info("TemperatureController initialized (JAVA FALLBACK) " + width + "x" + height);
            javaGrid = new float[width][height];
            javaPrevGrid = new float[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    javaGrid[i][j] = defaultRelativeTemperature;
                }
            }
        }
    }
    
    /**
     * Возвращает копию сетки температур.
     * В Native режиме собирает данные из прямого буфера.
     */
    public float[][] getCurrentTemperatureGrid() {
        
        float[][] tempGrid = new float[width][height];
        
        if (NativeTemperature.isLoaded && NativeTemperature.dataBuffer != null) {
            try {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        tempGrid[x][y] = NativeTemperature.dataBuffer.get(y * width + x);
                    }
                }
            } catch (Exception e) {
            }
        } else {
            return javaGrid;
        }
        return tempGrid;
    }
    
    public void update() {
        if (Vars.state.isPaused() || simulationPaused) return;
        
        if (width != Vars.world.width() || height != Vars.world.height()) {
            if (Vars.world.width() > 0 && Vars.world.height() > 0) {
                init(Vars.world.width(), Vars.world.height());
            }
            return;
        }
        
        long startTime = Time.millis();
        
        if (NativeTemperature.isLoaded) {
            // Тяжелая физика считается в C++ (OpenMP + SIMD)
            // Результат сразу доступен в буфере, ничего копировать не надо
            NativeTemperature.update(heatTransferCoefficient * Time.delta, ambientTemperatureChangeRate * Time.delta);
        } else {
            updateJavaFallback();
        }
        
        updateUnitTemperatures();
        
        lastCalculationTimeMs = Time.timeSinceMillis(startTime);
    }
    
    private final int[] tmpIds = new int[1000];
    
    private void updateUnitTemperatures() {
        final float delta = Time.delta;
        final float transferCoeffUnit = heatTransferCoefficient * delta / 2f;
        
        if (!unitTemperatures.isEmpty()) {
            int removeCount = 0;
            for (IntMap.Entry<Float> entry : unitTemperatures.entries()) {
                if (Groups.unit.getByID(entry.key) == null) {
                    if (removeCount < tmpIds.length) tmpIds[removeCount++] = entry.key;
                }
            }
            for (int i = 0; i < removeCount; i++) unitTemperatures.remove(tmpIds[i]);
        }
        
        for (Unit unit : Groups.unit) {
            if (!unit.isAdded() || unit.isNull()) continue;
            
            int tx = unit.tileX();
            int ty = unit.tileY();
            
            if (isWithinBounds(tx, ty)) {
                float tileTemp = getRelativeTemperatureAt(tx, ty);
                float unitTemp = getRelativeTemperatureOf(unit);
                
                float deltaTemp = (tileTemp - unitTemp) * transferCoeffUnit;
                
                incrementRelativeTemperatureOf(unit, deltaTemp);
                incrementRelativeTemperatureAt(tx, ty, -deltaTemp);
            }
        }
    }
    
    
    public float getRelativeTemperatureAt(int x, int y) {
        if (!isWithinBounds(x, y)) return defaultRelativeTemperature;
        
        if (NativeTemperature.isLoaded) {
            return NativeTemperature.getTemperature(x, y);
        } else {
            return javaGrid[x][y];
        }
    }
    
    public void incrementRelativeTemperatureAt(int x, int y, float increment) {
        if (!isWithinBounds(x, y)) return;
        
        if (NativeTemperature.isLoaded) {
            float current = NativeTemperature.getTemperature(x, y);
            NativeTemperature.setTemperature(x, y, Mathf.clamp(current + increment, -1f, 1f));
        } else {
            javaGrid[x][y] = Mathf.clamp(javaGrid[x][y] + increment, -1f, 1f);
        }
    }
    
    public void setRelativeTemperatureAt(int x, int y, float value) {
        if (!isWithinBounds(x, y)) return;
        
        if (NativeTemperature.isLoaded) {
            NativeTemperature.setTemperature(x, y, Mathf.clamp(value, -1f, 1f));
        } else {
            javaGrid[x][y] = Mathf.clamp(value, -1f, 1f);
        }
    }
    
    
    public float getAbsoluteTemperatureAt(int x, int y) {
        float relativeTemp = getRelativeTemperatureAt(x, y);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }
    
    public float getRelativeTemperatureOf(Unit unit) {
        return unitTemperatures.get(unit.id, defaultRelativeTemperature);
    }
    
    public float getRelativeTemperatureAbove(Unit unit) {
        return getRelativeTemperatureAt(unit.tileX(), unit.tileY());
    }
    
    public void incrementRelativeTemperatureOf(Unit unit, float increment) {
        float currentTemp = getRelativeTemperatureOf(unit);
        unitTemperatures.put(unit.id, Mathf.clamp(currentTemp + increment, -1f, 1f));
    }
    
    public void setRelativeTemperatureOf(Unit unit, float value) {
        unitTemperatures.put(unit.id, Mathf.clamp(value, -1f, 1f));
    }
    
    public float getAbsoluteTemperatureOf(Unit unit) {
        float relativeTemp = getRelativeTemperatureOf(unit);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }
    
    public float getAbsoluteTemperatureAbove(Unit unit) {
        float relativeTemp = getRelativeTemperatureAbove(unit);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }
    
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public Block getBlockAt(int x, int y) {
        Tile tile = Vars.world.tile(x, y);
        if (tile == null) return Blocks.air;
        return tile.build != null ? tile.build.block : tile.floor();
    }
    
    public long getLastCalculationTimeMs() {
        return lastCalculationTimeMs;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public IntMap<Float> getUnitTemperatures() {
        return unitTemperatures;
    }
    
    public static void setSimulationPaused(boolean paused) {
        if(simulationPaused != paused) {
            SLog.info("Temperature simulation " + (paused ? "paused" : "resumed"));
            simulationPaused = paused;
        }
    }
    
    
    private void updateJavaFallback() {
        for(int x=0; x<width; x++) {
            System.arraycopy(javaGrid[x], 0, javaPrevGrid[x], 0, height);
        }
        
        float delta = Time.delta;
        float transferCoeff = heatTransferCoefficient * delta;
        float ambientChange = ambientTemperatureChangeRate * delta;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float prev = javaPrevGrid[x][y];
                float totalChange = 0f;
                
                if (x > 0) totalChange += javaPrevGrid[x-1][y] - prev;
                if (x < width-1) totalChange += javaPrevGrid[x+1][y] - prev;
                if (y > 0) totalChange += javaPrevGrid[x][y-1] - prev;
                if (y < height-1) totalChange += javaPrevGrid[x][y+1] - prev;
                
                totalChange *= transferCoeff;
                totalChange += ambientChange;
                
                javaGrid[x][y] = Mathf.clamp(prev + totalChange, -1f, 1f);
            }
        }
    }
}