package org.durmiendo.sueno.content.blocks.conveyors;

import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.*;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.input.Placement;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.Autotiler;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import org.durmiendo.sueno.mainContents.SuenoBlock;

public class SConveyor extends SuenoBlock implements Autotiler {
    private static final float itemSpace = 0.4F;
    private static final int capacity = 3;
    public TextureRegion[][] regions;
    public float speed = 0.0F;
    public float displayedSpeed = 0.0F;
    @Nullable
    public Block junctionReplacement;
    @Nullable
    public Block bridgeReplacement;

    public SConveyor(String name) {
        super(name);
    }


    public void setStats() {
        super.setStats();
        this.stats.add(Stat.itemsMoved, this.displayedSpeed, StatUnit.itemsSecond);
    }

    public void init() {
        super.init();
        if (this.junctionReplacement == null) {
            this.junctionReplacement = Blocks.junction;
        }

        if (this.bridgeReplacement == null || !(this.bridgeReplacement instanceof ItemBridge)) {
            this.bridgeReplacement = Blocks.itemBridge;
        }

    }

    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        int[] bits = this.getTiling(plan, list);
        if (bits != null) {
            TextureRegion region = this.regions[bits[0]][0];
            Draw.rect(region, plan.drawx(), plan.drawy(), (float)(region.width * bits[1]) * region.scl(), (float)(region.height * bits[2]) * region.scl(), (float)(plan.rotation * 90));
        }
    }

    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
        return (otherblock.outputsItems() || this.lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasItems) && this.lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    public boolean canReplace(Block other) {
        return super.canReplace(other) && !(other instanceof StackConveyor);
    }

    public void handlePlacementLine(Seq<BuildPlan> plans) {
        if (this.bridgeReplacement != null) {
            Placement.calculateBridges(plans, (ItemBridge)this.bridgeReplacement);
        }
    }

    public TextureRegion[] icons() {
        return new TextureRegion[]{this.regions[0][0]};
    }

    public boolean isAccessible() {
        return true;
    }

    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
        if (this.junctionReplacement == null) {
            return this;
        } else {
            Boolf<Point2> cont = (p) -> {
                return plans.contains((o) -> {
                    return o.x == req.x + p.x && o.y == req.y + p.y && (req.block instanceof Conveyor || req.block instanceof Junction);
                });
            };
            return (Block)(cont.get(Geometry.d4(req.rotation)) && cont.get(Geometry.d4(req.rotation - 2)) && req.tile() != null && req.tile().block() instanceof Conveyor && Mathf.mod(req.tile().build.rotation - req.rotation, 2) == 1 ? this.junctionReplacement : this);
        }
    }

    public class ConveyorBuild extends Building implements ChainedBuilding {
        public Item[] ids = new Item[3];
        public float[] xs = new float[3];
        public float[] ys = new float[3];
        public int len = 0;
        @Nullable
        public Building next;
        @Nullable
        public Conveyor.ConveyorBuild nextc;
        public boolean aligned;
        public int lastInserted;
        public int mid;
        public float minitem = 1.0F;
        public int blendbits;
        public int blending;
        public int blendsclx = 1;
        public int blendscly = 1;
        public float clogHeat = 0.0F;

        public ConveyorBuild() {
        }

        public void draw() {
            int frame = this.enabled && this.clogHeat <= 0.5F ? (int)(Time.time * SConveyor.this.speed * 8.0F * this.timeScale * this.efficiency % 4.0F) : 0;
            Draw.z(29.5F);

            float wheight;
            for(int i = 0; i < 4; ++i) {
                if ((this.blending & 1 << i) != 0) {
                    int dir = this.rotation - i;
                    wheight = i == 0 ? (float)(this.rotation * 90) : (float)(dir * 90);
                    Draw.rect(SConveyor.this.sliced(SConveyor.this.regions[0][frame], i != 0 ? SliceMode.bottom : SliceMode.top), this.x + (float)(Geometry.d4x(dir) * 8) * 0.75F, this.y + (float)(Geometry.d4y(dir) * 8) * 0.75F, wheight);
                }
            }

            Draw.z(29.8F);
            Draw.rect(SConveyor.this.regions[this.blendbits][frame], this.x, this.y, (float)(8 * this.blendsclx), (float)(8 * this.blendscly), (float)(this.rotation * 90));
            Draw.z(29.9F);
            float layer = 29.9F;
            float wwidth = (float)Vars.world.unitWidth();
            wheight = (float)Vars.world.unitHeight();
            float scaling = 0.01F;

            for(int ix = 0; ix < this.len; ++ix) {
                Item item = this.ids[ix];
                Tmp.v1.trns((float)(this.rotation * 90), 8.0F, 0.0F);
                Tmp.v2.trns((float)(this.rotation * 90), -4.0F, this.xs[ix] * 8.0F / 2.0F);
                float ixx = this.x + Tmp.v1.x * this.ys[ix] + Tmp.v2.x;
                float iy = this.y + Tmp.v1.y * this.ys[ix] + Tmp.v2.y;
                Draw.z(layer + (ixx / wwidth + iy / wheight) * scaling);
                Draw.rect(item.fullIcon, ixx, iy, 5.0F, 5.0F);
            }

        }

        public void payloadDraw() {
            Draw.rect(this.block.fullIcon, this.x, this.y);
        }

        public void drawCracks() {
            Draw.z(29.85F);
            super.drawCracks();
        }

        public void overwrote(Seq<Building> builds) {
            Object var3 = builds.first();
            if (var3 instanceof Conveyor.ConveyorBuild) {
                Conveyor.ConveyorBuild build = (Conveyor.ConveyorBuild)var3;
                this.ids = (Item[])build.ids.clone();
                this.xs = (float[])build.xs.clone();
                this.ys = (float[])build.ys.clone();
                this.len = build.len;
                this.clogHeat = build.clogHeat;
                this.lastInserted = build.lastInserted;
                this.mid = build.mid;
                this.minitem = build.minitem;
                this.items.add(build.items);
            }

        }

        public boolean shouldAmbientSound() {
            return this.clogHeat <= 0.5F;
        }

        public void onProximityUpdate() {
            super.onProximityUpdate();
            int[] bits = SConveyor.this.buildBlending(this.tile, this.rotation, (BuildPlan[])null, true);
            this.blendbits = bits[0];
            this.blendsclx = bits[1];
            this.blendscly = bits[2];
            this.blending = bits[4];
            this.next = this.front();
            this.nextc = this.next instanceof SConveyor.ConveyorBuild && this.next.team == this.team ? (Conveyor.ConveyorBuild)this.next : null;
            this.aligned = this.nextc != null && this.rotation == this.next.rotation;
        }

        public void unitOn(Unit unit) {
            if (!(this.clogHeat > 0.5F) && this.enabled) {
                this.noSleep();
                float mspeed = SConveyor.this.speed * 8.0F * 55.0F;
                float centerSpeed = 0.1F;
                float centerDstScl = 3.0F;
                float tx = (float)Geometry.d4x(this.rotation);
                float ty = (float)Geometry.d4y(this.rotation);
                float centerx = 0.0F;
                float centery = 0.0F;
                if (Math.abs(tx) > Math.abs(ty)) {
                    centery = Mathf.clamp((this.y - unit.y()) / centerDstScl, -centerSpeed, centerSpeed);
                    if (Math.abs(this.y - unit.y()) < 1.0F) {
                        centery = 0.0F;
                    }
                } else {
                    centerx = Mathf.clamp((this.x - unit.x()) / centerDstScl, -centerSpeed, centerSpeed);
                    if (Math.abs(this.x - unit.x()) < 1.0F) {
                        centerx = 0.0F;
                    }
                }

                if ((float)this.len * 0.4F < 0.9F) {
                    unit.impulse((tx * mspeed + centerx) * this.delta(), (ty * mspeed + centery) * this.delta());
                }

            }
        }

        public void updateTile() {
            this.minitem = 1.0F;
            this.mid = 0;
            if (this.len == 0) {
                this.clogHeat = 0.0F;
                this.sleep();
            } else {
                float nextMax = this.aligned ? 1.0F - Math.max(0.4F - this.nextc.minitem, 0.0F) : 1.0F;
                float moved = SConveyor.this.speed * this.edelta();

                for(int i = this.len - 1; i >= 0; --i) {
                    float nextpos = (i == this.len - 1 ? 100.0F : this.ys[i + 1]) - 0.4F;
                    float maxmove = Mathf.clamp(nextpos - this.ys[i], 0.0F, moved);
                    float[] var10000 = this.ys;
                    var10000[i] += maxmove;
                    if (this.ys[i] > nextMax) {
                        this.ys[i] = nextMax;
                    }

                    if ((double)this.ys[i] > 0.5 && i > 0) {
                        this.mid = i - 1;
                    }

                    this.xs[i] = Mathf.approach(this.xs[i], 0.0F, moved * 2.0F);
                    if (this.ys[i] >= 1.0F && this.pass(this.ids[i])) {
                        if (this.aligned) {
                            this.nextc.xs[this.nextc.lastInserted] = this.xs[i];
                        }

                        this.items.remove(this.ids[i], this.len - i);
                        this.len = Math.min(i, this.len);
                    } else if (this.ys[i] < this.minitem) {
                        this.minitem = this.ys[i];
                    }
                }

                if (this.minitem < 0.4F + (this.blendbits == 1 ? 0.3F : 0.0F)) {
                    this.clogHeat = Mathf.approachDelta(this.clogHeat, 1.0F, 0.016666668F);
                } else {
                    this.clogHeat = 0.0F;
                }

                this.noSleep();
            }
        }

        public boolean pass(Item item) {
            if (item != null && this.next != null && this.next.team == this.team && this.next.acceptItem(this, item)) {
                this.next.handleItem(this, item);
                return true;
            } else {
                return false;
            }
        }

        public int removeStack(Item item, int amount) {
            this.noSleep();
            int removed = 0;

            for(int j = 0; j < amount; ++j) {
                for(int i = 0; i < this.len; ++i) {
                    if (this.ids[i] == item) {
                        this.remove(i);
                        ++removed;
                        break;
                    }
                }
            }

            this.items.remove(item, removed);
            return removed;
        }

        public void getStackOffset(Item item, Vec2 trns) {
            trns.trns(this.rotdeg() + 180.0F, 4.0F);
        }

        public int acceptStack(Item item, int amount, Teamc source) {
            return Math.min((int)(this.minitem / 0.4F), amount);
        }

        public void handleStack(Item item, int amount, Teamc source) {
            amount = Math.min(amount, 3 - this.len);

            for(int i = amount - 1; i >= 0; --i) {
                this.add(0);
                this.xs[0] = 0.0F;
                this.ys[0] = (float)i * 0.4F;
                this.ids[0] = item;
                this.items.add(item, 1);
            }

            this.noSleep();
        }

        public boolean acceptItem(Building source, Item item) {
            if (this.len >= 3) {
                return false;
            } else {
                Tile facing = Edges.getFacingEdge(source.tile, this.tile);
                if (facing == null) {
                    return false;
                } else {
                    int direction = Math.abs(facing.relativeTo(this.tile.x, this.tile.y) - this.rotation);
                    return (direction == 0 && this.minitem >= 0.4F || direction % 2 == 1 && this.minitem > 0.7F) && (!source.block.rotate || this.next != source);
                }
            }
        }

        public void handleItem(Building source, Item item) {
            if (this.len < 3) {
                int r = this.rotation;
                Tile facing = Edges.getFacingEdge(source.tile, this.tile);
                int ang = facing.relativeTo(this.tile.x, this.tile.y) - r;
                float x = ang != -1 && ang != 3 ? (ang != 1 && ang != -3 ? 0.0F : -1.0F) : 1.0F;
                this.noSleep();
                this.items.add(item, 1);
                if (Math.abs(facing.relativeTo(this.tile.x, this.tile.y) - r) == 0) {
                    this.add(0);
                    this.xs[0] = x;
                    this.ys[0] = 0.0F;
                    this.ids[0] = item;
                } else {
                    this.add(this.mid);
                    this.xs[this.mid] = x;
                    this.ys[this.mid] = 0.5F;
                    this.ids[this.mid] = item;
                }

            }
        }

        public void write(Writes write) {
            super.write(write);
            write.i(this.len);

            for(int i = 0; i < this.len; ++i) {
                write.i(Pack.intBytes((byte)this.ids[i].id, (byte)((int)(this.xs[i] * 127.0F)), (byte)((int)(this.ys[i] * 255.0F - 128.0F)), (byte)0));
            }

        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int amount = read.i();
            this.len = Math.min(amount, 3);

            for(int i = 0; i < amount; ++i) {
                int val = read.i();
                short id = (short)((byte)(val >> 24) & 255);
                float x = (float)((byte)(val >> 16)) / 127.0F;
                float y = ((float)((byte)(val >> 8)) + 128.0F) / 255.0F;
                if (i < 3) {
                    this.ids[i] = Vars.content.item(id);
                    this.xs[i] = x;
                    this.ys[i] = y;
                }
            }

            this.updateTile();
        }

        public Object senseObject(LAccess sensor) {
            return sensor == LAccess.firstItem && this.len > 0 ? this.ids[this.len - 1] : super.senseObject(sensor);
        }

        public final void add(int o) {
            for(int i = Math.max(o + 1, this.len); i > o; --i) {
                this.ids[i] = this.ids[i - 1];
                this.xs[i] = this.xs[i - 1];
                this.ys[i] = this.ys[i - 1];
            }

            ++this.len;
        }

        public final void remove(int o) {
            for(int i = o; i < this.len - 1; ++i) {
                this.ids[i] = this.ids[i + 1];
                this.xs[i] = this.xs[i + 1];
                this.ys[i] = this.ys[i + 1];
            }

            --this.len;
        }

        @Nullable
        public Building next() {
            return this.nextc;
        }
    }

}
