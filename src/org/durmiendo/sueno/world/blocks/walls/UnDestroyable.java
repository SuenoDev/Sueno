package org.durmiendo.sueno.world.blocks.walls;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Strings;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.graphics.Layer;
import mindustry.logic.LAccess;
import mindustry.type.Category;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.BuildVisibility;
import org.durmiendo.sueno.core.SVars;

public class UnDestroyable extends Wall {
    protected final static Rect rect = new Rect();
    protected final static Queue<UDBuild> doorQueue = new Queue<>();

    public UnDestroyable(String name) {
        super(name);
        health = Integer.MAX_VALUE;
        buildVisibility = BuildVisibility.sandboxOnly;
        configurable = true;
        update = true;
        category = Category.effect;

        config(Boolean.class, (UDBuild base, Boolean isDraw) -> {
            for(UDBuild entity : base.chained){
                if(entity.isDraw == isDraw){
                    continue;
                }

                entity.isDraw = isDraw;
            }
        });
    }

    public class UDBuild extends Building {
        public boolean isDraw = true;
        public boolean tips = true;
        public float damage = 0;
        public float maxDamage = 0;
        public float dps = 0;
        public float dm = 0;
        public boolean time = false;
        public float p = 0;
        public Vec2 pos = new Vec2(0, 2);

        public Seq<UDBuild> chained = new Seq<>();

        @Override
        public void onProximityAdded(){
            super.onProximityAdded();
            updateChained();
        }

        @Override
        public void onProximityRemoved(){
            super.onProximityRemoved();

            for(Building b : proximity){
                if(b instanceof UDBuild d){
                    d.updateChained();
                }
            }
        }

        public void updateChained(){
            chained = new Seq<>();
            doorQueue.clear();
            doorQueue.add(this);

            while(!doorQueue.isEmpty()){
                var next = doorQueue.removeLast();
                chained.add(next);

                for(var b : next.proximity){
                    if(b instanceof UDBuild d && d.chained != chained){
                        d.chained = chained;
                        doorQueue.addFirst(d);
                    }
                }
            }
        }

        @Override
        public Boolean config(){
            return isDraw;
        }

        @Override
        public void update() {
            super.update();
            health = Integer.MAX_VALUE;
            if (p >= 60) {
//                Log.info("dps " + dps);
//                Log.info("dm " + dm);
//                Log.info("p " + p);
                dps = dm;
                dm = 0;
                p = 0;
            }
            p += Time.delta;
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.setBackground(Core.atlas.drawable("sueno-black75"));
            table.button("hide/un", Styles.flatBordert, () -> configure(!isDraw)).growX().size(180f, 50f);
            table.row();
            table.check("draw", (b) -> isDraw = b).get().setChecked(isDraw);
            table.row();
            table.check("tips", (b) -> tips=b).get().setChecked(tips);
            table.row();
            table.label(() -> "x " + pos.x);
            table.slider(-10, 10, 0.5f, pos.x, s -> pos.x = s);
            table.row();
            table.label(() -> "y " + pos.y);
            table.slider(-10, 10, 0.5f, pos.y, s -> pos.y = s);
        }

        @Override
        public void damage(float damage) {
            this.damage = damage;
            if (damage > maxDamage) {
                maxDamage = damage;
            }
            dm += damage;
        }

        @Override
        public void kill() {

        }

        @Override
        public void draw() {
            super.draw();
            if (isDraw && SVars.dataVisible) {
                Draw.draw(Layer.end, () -> {
                    if (tips) {
                        Fonts.def.draw("damage: " + Strings.fixed(damage, 2), x+pos.x*8, y+(pos.y+2)*8, Color.red, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                        Fonts.def.draw("max damage: " + Strings.fixed(maxDamage, 2), x+pos.x*8, y+(pos.y+1)*8, Color.green, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                        Fonts.def.draw("dps: " + Strings.fixed(dps, 2), x+pos.x*8, y+pos.y*8, Color.blue, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                    } else {
                        Fonts.def.draw(Strings.fixed(damage, 2), x+pos.x*8, y+(pos.y+2)*8, Color.red, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                        Fonts.def.draw(Strings.fixed(maxDamage, 2), x+pos.x*8, y+(pos.y+1)*8, Color.green, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                        Fonts.def.draw(Strings.fixed(dps, 2), x+pos.x*8, y+pos.y*8, Color.blue, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                    }
                });

            }
        }


        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(isDraw);
            write.bool(tips);
            write.f(pos.x);
            write.f(pos.y);
        }

        @Override
        public void read(Reads read) {
            super.read(read);
            isDraw = read.bool();
            tips = read.bool();
            pos.x = read.f();
            pos.y = read.f();
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            isDraw = read.bool();
            tips = read.bool();
            pos.x = read.f();
            pos.y = read.f();
        }
    }
}
