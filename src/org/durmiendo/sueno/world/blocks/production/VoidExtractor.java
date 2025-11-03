package org.durmiendo.sueno.world.blocks.production;

import arc.Core;
import arc.Graphics;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Shaders;
import mindustry.logic.Ranged;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.production.GenericCrafter;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.utils.SInterp;
import org.durmiendo.sueno.utils.SLog;

import static arc.graphics.g2d.Lines.*;
import static arc.math.Mathf.*;
import static mindustry.Vars.indexer;

public class VoidExtractor extends Block {
    public float range = 90f;
    public float voidCap = 201f;
    public float voidSpeed = 10f;

    public static ObjectMap<Float, Vec2> ports = new ObjectMap<>(){{
        put(10f, new Vec2(16f, 0f).rotate(30f));
        put(80f, new Vec2(16f, 0f).rotate(60f));

        put(100f, new Vec2(16f, 0f).rotate(120f));
        put(170f, new Vec2(16f, 0f).rotate(150f));

        put(190f, new Vec2(16f, 0f).rotate(210f));
        put(260f, new Vec2(16f, 0f).rotate(240f));

        put(280f, new Vec2(16f, 0f).rotate(300f));
        put(350f, new Vec2(16f, 0f).rotate(330f));
    }};

    public TextureRegion up, down, left, right;
    public TextureRegion upleft, downleft, upright, downright;

    public VoidExtractor(String name) {
        super(name);
        update = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("void substance", entity -> new Bar(
                () -> "Вещество пустоты",
                () -> Color.purple,
                () -> entity instanceof VoidExtractorBuild build ? build.voidSubstance / voidCap : 0
        ));
    }

    @Override
    public void load() {
        super.load();
        right = Core.atlas.find("sueno-void_extractor_1");
        up = Core.atlas.find("sueno-void_extractor_2");
        left = Core.atlas.find("sueno-void_extractor_3");
        down = Core.atlas.find("sueno-void_extractor_4");

        upleft = Core.atlas.find("sueno-void_extractor_left_top");
        upright = Core.atlas.find("sueno-void_extractor_right_top");
        downleft = Core.atlas.find("sueno-void_extractor_left_bottom");
        downright = Core.atlas.find("sueno-void_extractor_right_bottom");
    }

    public class VoidExtractorBuild extends Building implements Ranged {
        public float progress = 0;
        public float voidSubstance = 0;
        public VoidStriderc target;
        public VoidExtractorBuild() {}

        @Override
        public void updateTile() {
            super.updateTile();

            if (!Vars.state.isPaused()) {
                if (Mathf.chanceDelta(0.02f)) {
                    SFx.s1.at(x, y);
                }

                if (target != null && Mathf.chanceDelta(0.02f)) {
                    SFx.s12.at(target.x(), target.y());
                }

                if (target != null && Mathf.chanceDelta(0.05f)) {
                    SFx.s3l.at(target.x(), target.y(), 0, new Object[]{this, target.x(), target.y()});
                }
            }

            if (target != null && progress > 0.25f) {
                float angle = tmp.set(target.x(), target.y()).sub(x, y).angle();
                float portKey = 340f;
                boolean first = true;

                for (Float pp : ports.keys()) {
                    float diff = Math.abs(angle - pp);
                    diff = Math.min(diff, 360 - diff);

                    if (first || diff < Math.abs(angle - portKey)) {
                        portKey = pp;
                        first = false;
                    }
                }

                float diff = angle - portKey;

                Vec2 port = ports.get(portKey);
                float xx = Mathf.cosDeg(angle-180f+diff) * 12f + target.x();
                float yy = Mathf.sinDeg(angle-180f+diff) * 12f + target.y();

                curve(x, y, port.x+x, port.y+y, xx, yy, target.x(), target.y(), 15);
            }

            Seq<Unit> uns = Groups.unit.intersect(x - maxRange(), y - maxRange(), maxRange()*2f, maxRange()*2f).select(b -> b instanceof VoidStriderc)
                    .select(b -> b.dst(x, y) <= maxRange()).sort(u -> u.dst(x, y));
            VoidStriderc un = null;

            if (!uns.isEmpty()) un = (VoidStriderc) uns.first();
            use(un);
        }

        float n = 2.9f;
        float l = 1.8f;

        Color v1 = Color.valueOf("eeeeee");
        Color v2 = Color.valueOf("cccccc");
        Vec2 tmp = new Vec2();
        @Override
        public void draw() {
            super.draw();

            float z = Draw.z();

//            Draw.draw(z + 0.1f, () -> {
//                Shader s = Draw.getShader();
//                Draw.shader(SShaders.voidSpaceShader2);
//
//                Fill.circle(x, y, 6f * voidSubstance / voidCap);
//
//                Draw.shader(s);
//            });

            float p = SInterp.old.apply(progress);

            Draw.z(z + 1.2f);
            Draw.color(v2);
            float pn = (p+voidSubstance/voidCap*0.33f+0.08f)*n;
            float pl = (p+voidSubstance/voidCap*0.33f+0.08f)*l;

            Draw.rect(right, x + pn, y);
            Draw.rect(up, x, y + pn);
            Draw.rect(left, x - pn, y);
            Draw.rect(down, x, y - pn);

            Draw.color(v1);
            Draw.z(z + 1.3f);
            Draw.rect(upright, x + pl, y + pl);
            Draw.rect(downright, x + pl, y - pl);
            Draw.rect(upleft, x - pl, y + pl);
            Draw.rect(downleft, x - pl, y - pl);

            Draw.z(SLayers.voidspace);
            drawc();

            Draw.z(z);
        }

        void drawc() {
            Fill.circle(x, y, 6f * voidSubstance / voidCap);
        }

        public void use(VoidStriderc unit) {
            if (unit == null || voidSubstance >= voidCap) {
                progress -= Time.delta / 9f / 60f;
                if (progress <= 0) progress = 0;
            } else {
                progress += Time.delta / 5f / 60f;
                if (progress > 1) progress = 1;

                if (progress > 0.25f && unit.dst(x, y) <= range()) {
                    target = unit;

                    float v = Time.delta / 60f * voidSpeed;
                    if (voidSubstance != voidCap) {
                        voidSubstance += v;
                        unit.voidSubstance(unit.voidSubstance() - v);
                        if (voidSubstance > voidCap) {
                            voidSubstance = voidCap;
                        }
                    }
                    return;
                }
            }

            target = null;
        }

        @Override
        public void drawSelect(){
            Draw.color(Color.gray);
            Lines.circle(x, y, maxRange());
            Drawf.dashCircle(x, y, range(), Color.orange);
            boolean f = true;
            for (Unit un : Groups.unit.intersect(x - range(), y - range(), x + range(), y + range()).select(b -> b instanceof VoidStriderc)
                    .select(b -> b.dst(x, y) <= range()).sort(u -> u.dst(x, y))) {
                if (un instanceof VoidStriderc) {
                    if (f) {
                        Draw.color(Color.orange);
                        Lines.stroke(1.3f);
                        Lines.square(un.x, un.y, un.hitSize, 45f);
                        Lines.square(un.x, un.y, un.hitSize+4f, 45f);
                        f = false;
                    } else {
                        Draw.color(Color.darkGray);
                        Lines.stroke(1.6f);
                        Lines.square(un.x, un.y, un.hitSize, 45f);
                    }
                }
            }
        }

        public float range() {
            return range * SInterp.old.apply(progress);
        }

        public float maxRange() {
            return range;
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(voidSubstance);
            write.f(progress);
        }


        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            voidSubstance = read.f();
            progress = read.f();
        }
    }



    public static void curve(float x1, float y1, float cx1, float cy1, float cx2, float cy2, float x2, float y2, int segments){
        float subdiv_step = 1f / segments;
        float subdiv_step2 = subdiv_step * subdiv_step;
        float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;

        float pre1 = 3 * subdiv_step;
        float pre2 = 3 * subdiv_step2;
        float pre4 = 6 * subdiv_step2;
        float pre5 = 6 * subdiv_step3;

        float tmp1x = x1 - cx1 * 2 + cx2;
        float tmp1y = y1 - cy1 * 2 + cy2;

        float tmp2x = (cx1 - cx2) * 3 - x1 + x2;
        float tmp2y = (cy1 - cy2) * 3 - y1 + y2;

        float fx = x1;
        float fy = y1;

        float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
        float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;

        float ddfx = tmp1x * pre4 + tmp2x * pre5;
        float ddfy = tmp1y * pre4 + tmp2y * pre5;

        float dddfx = tmp2x * pre5;
        float dddfy = tmp2y * pre5;

        float x, y;

        while(segments-- > 0){
            x = fx;
            y = fy;

            fx += dfx;
            fy += dfy;
            dfx += ddfx;
            dfy += ddfy;
            ddfx += dddfx;
            ddfy += dddfy;


            if (!Vars.state.isPaused()) {
                for (int i = 0; i < 5; i++) {
                    if (Mathf.chanceDelta(0.004f)) {
                        float r = Mathf.random(0f, 1f);
                        float r1 = 1f-r;
                        SFx.s2.at((x*r+fx*r1), (y*r+fy*r1), Mathf.angle(fx-x, fy-y));
                    }
                }

                if (Mathf.chanceDelta(0.012f)) {
                    float r = Mathf.random(0f, 1f);
                    float r1 = 1f-r;
                    SFx.s3.at((x*r+fx*r1), (y*r+fy*r1), Mathf.angle(fx-x, fy-y));
                };
            }
        }
    }
}