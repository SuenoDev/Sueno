package org.durmiendo.sueno.world.blocks.production;

import arc.Core;
import arc.func.Cons2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.EnumSet;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectMap;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class WallDrill extends Block {
    static int idx;
    static ObjectMap<Item, Integer> itemsAmounts = new ObjectMap<>();
    static int maxAmount;
    static Item maxItem;

    public TextureRegion topRegion;
    public TextureRegion rotatorBottomRegion;
    public TextureRegion rotatorRegion;

    public float drillTime = 60f;
    public int tier = 1;
    /** How many times faster the drill will progress when boosted by an optional consumer. */
    public float optionalBoostIntensity = 2.5f;

    public int rotators = 2;
    public float rotatorsSideSpace = 0.8f;

    /** Multipliers of drill speed for each item. Defaults to 1. */
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public WallDrill(String name){
        super(name);

        hasItems = true;
        rotate = true;
        update = true;
        solid = true;
        drawArrow = false;
        regionRotated1 = 1;
        ambientSoundVolume = 0.05f;
        ambientSound = Sounds.drill;

        envEnabled |= Env.space;
        flags = EnumSet.of(BlockFlag.drill);
    }

    @Override
    public void load() {
        topRegion = Core.atlas.find(name + "-top");
        rotatorBottomRegion = Core.atlas.find(name + "-rotator-bottom");
        rotatorRegion = Core.atlas.find(name + "-rotator");

        super.load();
    }

    @Override
    public void init(){
        updateClipRadius(size * tilesize * 1.5f);
        super.init();
    }

    @Override
    public void setBars(){
        super.setBars();

        addBar("drillspeed", (WallDrillBuild e) ->
                new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60, 2)), () -> Pal.ammo, () -> e.warmup));
    }

    @Override
    public boolean outputsItems(){
        return true;
    }

    public void drawRotators(float buildX, float buildY, int rotation, Cons2<Float, Float> drawer) {
        float x = Geometry.d4x(rotation) / 2f * size * tilesize + buildX;
        float y = Geometry.d4y(rotation) / 2f * size * tilesize + buildY;

        float rotatorsOffsetX = Geometry.d4x(rotation-1);
        float rotatorsOffsetY = Geometry.d4y(rotation-1);

        float space = (size * rotatorsSideSpace) / rotators;
        for (int i = 0; i < rotators; i++) {
            float rx = x + rotatorsOffsetX * space * (i - (rotators - 1) / 2f) * tilesize;
            float ry = y + rotatorsOffsetY * space * (i - (rotators - 1) / 2f) * tilesize;

            drawer.get(rx, ry);
        }
    }

    @Override
    public boolean rotatedOutput(int x, int y){
        return false;
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, topRegion, rotatorBottomRegion, rotatorRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(topRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        drawRotators(plan.drawx(), plan.drawy(), plan.rotation, (x, y) -> {
            Draw.rect(rotatorBottomRegion, x, y);
            Draw.rect(rotatorRegion, x, y);
        });
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.drillTier, StatValues.drillables(drillTime, 0f, size, drillMultipliers, b -> (b instanceof Floor f && f.wallOre && f.itemDrop != null && f.itemDrop.hardness <= tier) || (b instanceof StaticWall w && w.itemDrop != null && w.itemDrop.hardness <= tier)));

        stats.add(Stat.drillSpeed, 60f / drillTime * size, StatUnit.itemsSecond);

        if (optionalBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase) {
            stats.remove(Stat.booster);
            stats.add(Stat.booster,
                    StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
                            consBase.amount, optionalBoostIntensity, false,
                            l -> (consumesLiquid(l) && (findConsumer(f -> f instanceof ConsumeLiquid).booster || ((ConsumeLiquid)findConsumer(f -> f instanceof ConsumeLiquid)).liquid != l)))
            );
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Item item, invalidItem = null;
        int count;

        for (int j = 0; j < size; j++) {
            nearbySide(x, y, rotation, j, Tmp.p1);
            Tile other = world.tile(Tmp.p1.x, Tmp.p1.y);
            if (other.wallDrop() != null) {
                if (other.wallDrop().hardness <= tier) {
                    Item i = other.wallDrop();
                    itemsAmounts.put(i, itemsAmounts.get(i, 0) + 1);
                } else {
                    invalidItem = other.wallDrop();
                }
            }
        }

        maxAmount = 0;
        maxItem = null;
        itemsAmounts.forEach(e -> {
            if (e.value > maxAmount) {
                maxItem = e.key;
                maxAmount = e.value;
            }
        });

        item = maxItem;
        count = maxAmount;

        itemsAmounts.clear();

        if (item != null) {
            float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / getDrillTime(item) * count, 2), x, y, valid);
            float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
            Draw.mixcol(Color.darkGray, 1f);
            Draw.rect(item.fullIcon, dx, dy - 1, s, s);
            Draw.reset();
            Draw.rect(item.fullIcon, dx, dy, s, s);
        } else if (invalidItem != null) {
            drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, false);
        }
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        for (int i = 0; i < size; i++) {
            nearbySide(tile.x, tile.y, rotation, i, Tmp.p1);
            Tile other = world.tile(Tmp.p1.x, Tmp.p1.y);
            if (other != null && other.solid()) {
                Item drop = other.wallDrop();
                if (drop != null && drop.hardness <= tier) {
                    return true;
                }
            }
        }

        return false;
    }

    public float getDrillTime(Item item){
        return drillTime / drillMultipliers.get(item, 1f);
    }

    public class WallDrillBuild extends Building {
        public Tile[] facing = new Tile[size];
        public Item output;
        public int outputAmount;

        public float time;
        public float warmup;
        public float lastDrillSpeed;

        @Override
        public void drawSelect(){
            if(output != null){
                float dx = x - size * tilesize/2f, dy = y + size * tilesize/2f, s = iconSmall / 4f;
                Draw.mixcol(Color.darkGray, 1f);
                Draw.rect(output.fullIcon, dx, dy - 1, s, s);
                Draw.reset();
                Draw.rect(output.fullIcon, dx, dy, s, s);
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();

            warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 60f);

            float multiplier = Mathf.lerp(1f, optionalBoostIntensity, optionalEfficiency);
            float drillTime = getDrillTime(output);

            lastDrillSpeed = multiplier * timeScale * outputAmount / drillTime;

            time += edelta() * multiplier;

            if(time >= drillTime){
                items.add(output, outputAmount);
                produced(output, outputAmount);
                time %= drillTime;
            }

            if(timer(timerDump, dumpTime)){
                dump();
            }
        }

        @Override
        public boolean shouldConsume(){
            return items.total() < itemCapacity && output != null && enabled;
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);
            Draw.rect(topRegion, x, y, rotdeg());

            if(isPayload()) return;

            idx = 0;
            drawRotators(tile.drawx(), tile.drawy(), rotation, (rx, ry) -> {
                idx += 157f;
                Draw.rect(rotatorBottomRegion, rx, ry, time * lastDrillSpeed * 360 + idx);
                Draw.rect(rotatorRegion, rx, ry);
            });
        }

        @Override
        public void onProximityUpdate(){
            updateFacing();
        }

        public void updateFacing() {
            for (int i = 0; i < size; i++) {
                nearbySide(tile.x, tile.y, rotation, i, Tmp.p1);
                Tile other = world.tile(Tmp.p1.x, Tmp.p1.y);
                facing[i] = other;
            }

            for (Tile t : facing) {
                if (t.wallDrop() != null && t.wallDrop().hardness <= tier) {
                    Item i = t.wallDrop();
                    itemsAmounts.put(i, itemsAmounts.get(i, 0) + 1);
                }
            }

            maxAmount = 0;
            maxItem = null;
            itemsAmounts.forEach(e -> {
                if (e.value > maxAmount) {
                    maxItem = e.key;
                    maxAmount = e.value;
                }
            });

            output = maxItem;
            outputAmount = maxAmount;

            itemsAmounts.clear();
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(time);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 1){
                time = read.f();
                warmup = read.f();
            }
        }
    }
}
