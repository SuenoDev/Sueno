package org.durmiendo.sueno.temperature;

import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.IntMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Threads;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sap.SuenoSettings;
import org.durmiendo.sueno.utils.SLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Управляет симуляцией температуры на карте включая юнитов.
 * Использует относительную шкалу температур [-1, 1] для расчетов
 */
public class TemperatureController {
    // Настройки и Константы
    public final float freezingDamage = 0.35f;
    public static float minSafeTemperature = -0.5f;
    public static float minTemperatureDamage = 20;
    public static float maxSafeTemperature = 0.6f;
    public static float maxHeatDamage = 300;

    /** Постоянное изменение температуры окружающей среды за тик (0 = нет изменения). */
    public final float ambientTemperatureChangeRate = 0f;

    /** Относительная температура по умолчанию для новых ячеек/юнитов [-1, 1]. */
    @SuenoSettings(min = -1f, max = 1f, steep = 0.01f, accuracy = 2, def = -1f, priority = 0)
    public static float defaultRelativeTemperature = -1f;

    /** Смещение для преобразования относительной температуры в абсолютную (для GUI). */
    public float absoluteTemperatureOffset = 30f;

    /** Коэффициент скорости теплопередачи. */
    @SuenoSettings(min = 0f, max = 0.025f, steep = 0.001f, accuracy = 6, def = 0.005f, priority = 1)
    public static float heatTransferCoefficient = 0.005f;

    /** Приостановить симуляцию температуры. */
    @SuenoSettings(def = 0)
    public static boolean simulationPaused = false;

    /** Отображать относительные значения [-1, 1] вместо абсолютных (для отладки). */
    @SuenoSettings(def = 1)
    public static boolean displayRelativeTemperature = true;

    public float[][] getCurrentTemperatureGrid() {
        return currentTemperatureGrid;
    }

    public IntMap<Float> getUnitTemperatures() {
        return unitTemperatures;
    }

    // --- Состояние ---
    private float[][] currentTemperatureGrid; // Текущая температура сетки
    private float[][] previousTemperatureGrid; // Температура сетки на предыдущем шаге
    private final IntMap<Float> unitTemperatures = new IntMap<>(); // Температура юнитов [id -> temp]

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int width;
    private int height;
    private long lastCalculationTimeMs;

    // --- Многопоточность ---
    private final ExecutorService threadPool;
    private final int numberOfThreads;
    private final Seq<Future<?>> gridCalculationFutures = new Seq<>();


    public TemperatureController() {
        numberOfThreads = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
        SLog.info("TemperatureController initialized with " + numberOfThreads + " threads.");

        Events.run(EventType.Trigger.update, this::update);
//        Events.on(EventType.class, event -> shutdown());
    }

    /**
     * Инициализирует или сбрасывает контроллер для карты указанных размеров.
     */
    public void init(int worldWidth, int worldHeight) {
        if (worldWidth <= 0 || worldHeight <= 0) {
            SLog.err("TemperatureController init skipped: Invalid world dimensions (" + worldWidth + "x" + worldHeight + ")");
            return;
        }
        SLog.info("TemperatureController initializing map: width = " + worldWidth + ", height = " + worldHeight);
        this.width = worldWidth;
        this.height = worldHeight;

        unitTemperatures.clear();
        currentTemperatureGrid = new float[width][height];
        previousTemperatureGrid = new float[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentTemperatureGrid[i][j] = defaultRelativeTemperature;
            }
        }
        lastCalculationTimeMs = 0;
        simulationPaused = false;
    }


    /**
     * Вызывается каждый игровой тик для обновления состояния температуры.
     */
    public void update() {
        if (Vars.state.isPaused() || simulationPaused) return;

        if (currentTemperatureGrid == null || width != Vars.world.width() || height != Vars.world.height()) {
            if (Vars.world.width() > 0 && Vars.world.height() > 0) {
                init(Vars.world.width(), Vars.world.height());
                if (currentTemperatureGrid == null) return;
            } else {
                return;
            }
        }

        long startTime = Time.millis();

        copyCurrentToPreviousGrid();

        updateGridTemperaturesParallelAndWait();

        updateUnitTemperatures();

        lastCalculationTimeMs = Time.timeSinceMillis(startTime);
    }

    private void copyCurrentToPreviousGrid() {
        for (int i = 0; i < width; i++) {
            System.arraycopy(currentTemperatureGrid[i], 0, previousTemperatureGrid[i], 0, height);
        }
    }

    /**
     * Запускает параллельный расчет сетки и ожидает завершения.
     */
    private void updateGridTemperaturesParallelAndWait() {
        int rowsPerThread = Math.max(1, (height + numberOfThreads - 1) / numberOfThreads);
        gridCalculationFutures.clear();

        for (int i = 0; i < numberOfThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = Math.min(startRow + rowsPerThread, height);
            if (startRow < endRow) {
                Future<?> future = threadPool.submit(() -> calculateGridTemperatureChunk(startRow, endRow));
                gridCalculationFutures.add(future);
            }
        }

        // Ожидаем завершения всех задач расчета сетки
        try {
            for (Future<?> future : gridCalculationFutures) {
                if (future != null) future.get();
            }
        } catch (Exception e) {
            SLog.err("Error during parallel grid temperature calculation:\n\n" + e);
            simulationPaused = true;
        }
    }

    /**
     * Рассчитывает обновление температуры для части сетки (выполняется в отдельном потоке).
     * Читает из `previousTemperatureGrid`, пишет в `currentTemperatureGrid`.
     */
    private void calculateGridTemperatureChunk(int startRow, int endRow) {
        final float delta = Time.delta;
        final float transferCoeff = heatTransferCoefficient * delta;
        final float ambientChange = ambientTemperatureChangeRate * delta;

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                float previousCellTemp = previousTemperatureGrid[x][y];
                float totalTemperatureChange = 0f;

                // Теплообмен с 4 соседями (читаем из previousTemperatureGrid)
                for (int neighborIndex = 0; neighborIndex < 4; neighborIndex++) {
                    Point2 offset = Geometry.d4[neighborIndex];
                    int neighborX = x + offset.x;
                    int neighborY = y + offset.y;
                    float neighborTemp = isWithinBounds(neighborX, neighborY)
                            ? previousTemperatureGrid[neighborX][neighborY]
                            : defaultRelativeTemperature; // Граничное условие
                    totalTemperatureChange += (neighborTemp - previousCellTemp);
                }
                totalTemperatureChange *= transferCoeff;

                // Добавляем изменение среды
                totalTemperatureChange += ambientChange;

                // Применяем изменение и ограничиваем [-1, 1]
                float newTemperature = Mathf.clamp(previousCellTemp + totalTemperatureChange, -1f, 1f);
                currentTemperatureGrid[x][y] = newTemperature; // Пишем в текущую сетку
            }
        }
    }

    /**
     * Обновляет температуру юнитов и их взаимодействие с сеткой (последовательно).
     */
    int[] tmp = new int[600];
    private void updateUnitTemperatures() {
        final float delta = Time.delta;
        final float transferCoeffUnit = heatTransferCoefficient * delta / 2f; // Коэфф. для юнитов

        // Удаление юнитов, которых больше нет в игре
        if (!unitTemperatures.isEmpty()) {
            int[] unitIdsToRemove = tmp;
            int removeCount = 0;
            for (IntMap.Entry<Float> entry : unitTemperatures.entries()) {
                if (Groups.unit.getByID(entry.key) == null) {
                    if (removeCount < unitIdsToRemove.length) { // Предохранитель
                        unitIdsToRemove[removeCount++] = entry.key;
                    } else {
                        // Если временный массив слишком мал (маловероятно), удаляем сразу
                        unitTemperatures.remove(entry.key);
                    }
                }
            }
            for (int i = 0; i < removeCount; i++) {
                unitTemperatures.remove(unitIdsToRemove[i]);
            }
        }


        // Обновление живых юнитов
        for (Unit unit : Groups.unit) {
            if (!unit.isAdded() || unit.isNull()) continue;

            int tileX = unit.tileX();
            int tileY = unit.tileY();

            if (isWithinBounds(tileX, tileY)) {
                float tileTemp = previousTemperatureGrid[tileX][tileY];
                float unitTemp = getRelativeTemperatureOf(unit);
                float deltaTemperatureForUnit = (tileTemp - unitTemp) * transferCoeffUnit;


                incrementRelativeTemperatureOf(unit, deltaTemperatureForUnit);
                incrementRelativeTemperatureAt(tileX, tileY, -deltaTemperatureForUnit);
            }
        }
    }

    // --- Методы доступа к температуре (Getters/Setters) ---

    /** Возвращает РЕАЛЬНУЮ температуру [-1, 1] ячейки сетки. Для расчетов. */
    public float getRelativeTemperatureAt(int x, int y) {
        if (isWithinBounds(x, y)) {
            return currentTemperatureGrid != null ? currentTemperatureGrid[x][y] : defaultRelativeTemperature;
        }
        return defaultRelativeTemperature;
    }

    /** Увеличивает РЕАЛЬНУЮ температуру ячейки сетки на increment (с ограничением [-1, 1]). */
    public void incrementRelativeTemperatureAt(int x, int y, float increment) {
        if (isWithinBounds(x, y) && currentTemperatureGrid != null) {
            float currentTemp = currentTemperatureGrid[x][y];
            currentTemperatureGrid[x][y] = Mathf.clamp(currentTemp + increment, -1f, 1f);
        }
    }

    /** Устанавливает ОТНОСИТЕЛЬНУЮ температуру ячейки сетки (с ограничением [-1, 1]). */
    public void setRelativeTemperatureAt(int x, int y, float value) {
        if (isWithinBounds(x, y) && currentTemperatureGrid != null) {
            currentTemperatureGrid[x][y] = Mathf.clamp(value, -1f, 1f);
        }
    }

    /** Возвращает АБСОЛЮТНУЮ температуру ячейки сетки. Только для GUI/эффектов! */
    public float getAbsoluteTemperatureAt(int x, int y) {
        float relativeTemp = getRelativeTemperatureAt(x, y);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }

    /** Возвращает ОТНОСИТЕЛЬНУЮ температуру [-1, 1] юнита. Для расчетов. */
    public float getRelativeTemperatureOf(Unit unit) {
        return unitTemperatures.get(unit.id, defaultRelativeTemperature);
    }

    /** Возвращает ОТНОСИТЕЛЬНУЮ температуру [-1, 1] тайла под юнитом. */
    public float getRelativeTemperatureAbove(Unit unit) {
        return getRelativeTemperatureAt(unit.tileX(), unit.tileY());
    }

    /** Увеличивает ОТНОСИТЕЛЬНУЮ температуру юнита на increment (с ограничением [-1, 1]). */
    public void incrementRelativeTemperatureOf(Unit unit, float increment) {
        float currentTemp = getRelativeTemperatureOf(unit);
        unitTemperatures.put(unit.id, Mathf.clamp(currentTemp + increment, -1f, 1f));
    }

    /** Устанавливает ОТНОСИТЕЛЬНУЮ температуру юнита (с ограничением [-1, 1]). */
    public void setRelativeTemperatureOf(Unit unit, float value) {
        unitTemperatures.put(unit.id, Mathf.clamp(value, -1f, 1f));
    }

    /** Возвращает АБСОЛЮТНУЮ температуру юнита. Только для GUI/эффектов! */
    public float getAbsoluteTemperatureOf(Unit unit) {
        float relativeTemp = getRelativeTemperatureOf(unit);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }

    /** Возвращает АБСОЛЮТНУЮ температуру тайла под юнитом. Только для GUI/эффектов! */
    public float getAbsoluteTemperatureAbove(Unit unit) {
        float relativeTemp = getRelativeTemperatureAbove(unit);
        return displayRelativeTemperature ? relativeTemp : relativeTemp + absoluteTemperatureOffset;
    }

    // --- Вспомогательные методы ---

    /** Проверяет, находятся ли координаты (x, y) в пределах актуальной карты. */
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && width > 0 && height > 0;
    }

    /** Возвращает блок в ячейке (здание или пол). */
    public Block getBlockAt(int x, int y) {
        Tile tile = Vars.world.tile(x, y);
        if (tile == null) return Blocks.air;
        return tile.build != null ? tile.build.block : tile.floor();
    }

    /** Возвращает время последнего полного цикла расчета температуры в мс. */
    public long getLastCalculationTimeMs() {
        return lastCalculationTimeMs;
    }

    /** Принудительно приостанавливает/возобновляет симуляцию. */
    public static void setSimulationPaused(boolean paused) {
        if(simulationPaused != paused) {
            SLog.info("Temperature simulation " + (paused ? "paused" : "resumed"));
            simulationPaused = paused;
        }
    }
}