package org.durmiendo.sueno.world.blocks;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sueno.graphics.SShaders;

import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Mathf.*;

public class DBlock extends Block {

    public DBlock(String name) {
        super(name);
        update = true;
    }

    public static FrameBuffer buffer = new FrameBuffer();

    public class DBuilding extends Building {
        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {

            buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
            Events.on(EventType.ResizeEvent.class, e -> {
                buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
            });
            Events.run(EventType.Trigger.draw, () -> {
                Draw.draw(111, () -> {
                    Vars.renderer.effectBuffer.begin(Color.clear);
                    if (t<720) d();
                    else if (t>=720 && t<1440) {
                        dd();
                    } else {}
                    Vars.renderer.effectBuffer.end();
                    Vars.renderer.effectBuffer.blit(Shaders.screenspace);
                });
            });

            return super.init(tile, team, shouldAdd, rotation);
        }

        public void inj(float a, Runnable b) {

            buffer.begin(Color.clear);
            b.run();
            SShaders.contractionShader.set("u_per", a);
            buffer.end();
            buffer.blit(SShaders.contractionShader);
//            buffer.blit(Shaders.screenspace);

        }

        public float t=0;//712.5f;
        public float nn=t;


        @Override
        public void update() {
            super.update();
            nn+=Time.delta/60f;
            t=nn;
        }


        public void dd() {
            Draw.reset();
            stroke(2f);



            Draw.reset();
        }

        public void d() {
            Draw.reset();

            stroke(2f);
            float per = Mathf.clamp((nn-716.5f)/5.3f, 0f, 1f);


            Draw.z(111f);


            float aa = 90f-360f/12f;

            for(int i = 0;i<12;i++){
                float pr = clamp((nn-60*(i+1.2f))/5.3f, 0f, 0.75f);
                float finalAa = aa;
                int finalI = i+1;
                int finalII = 11-i;
                inj(pr, () -> {
                    Draw.color(Pal.lightFlame);
                    Draw.rect("sueno-" + (finalI) + "-num",x+Mathf.cosDeg(finalAa)*168f,y+Mathf.sinDeg(finalAa)*168f, 12f, 12f);

                    Drawf.tri(x+Mathf.cosDeg(finalII*30+90)*2,y+Mathf.sinDeg(finalII*30+90)*2, 1f, 4, finalII*30+90);
                    Drawf.tri(x+Mathf.cosDeg(finalII*30+90)*2,y+Mathf.sinDeg(finalII*30+90)*2, 1f, 1, finalII*30+180+90);
                });
                aa-=360f/12f;
            }

            inj(per, () -> {
                Draw.color(Pal.lightFlame);
                Lines.circle(x, y, 186f);
                
                stroke(0.5f);
                for(int i = 0;i<60;i++){
                    Draw.color(Pal.lightFlame);
                    float s = 0;
                    if (i % 5==0) s = 2f;

                    Drawf.tri(x+Mathf.cosDeg(i*6)*186f,y+Mathf.sinDeg(i*6)*186f, 12f+s, 3.6f + s, i*6+180f);
                    Drawf.tri(x+Mathf.cosDeg(i*6)*186f,y+Mathf.sinDeg(i*6)*186f, 24f+s, 2.6f + s, i*6+180f);
                }


                Draw.color(Pal.lightFlame);

//                float d  = t;
//                float f  = t+0.1f;
//                float fc = pow(cos(2*pi*f)/2f+0.5f, 16f)/3f;
//                float fb = -sin(pi*d)/Math.abs(sin(pi*d));
//                float fa = fb*cos(pi*d)/2 + 0.5f;
//                float fy = mod(pow(fa, 16),1)+Mathf.floor(d)-fc;

//                float fy = cos(2*pi*t)/2f-0.58f+t-(cos(2*pi*t)/2f-0.5f)/1.5f;

                float fy  = t + (cos(2*pi*t)/2-0.5f)/3.1f-.08f;
                float fyy = t/60f + (cos(pi*t/60f)/2-0.5f)/3.1f-.08f;
                float a = -(fy/12f)*360f+90;
                Drawf.tri(x+Mathf.cosDeg(a)*88f,y+Mathf.sinDeg(a)*88f, 4f, 66, a);
                Drawf.tri(x+Mathf.cosDeg(a)*88f,y+Mathf.sinDeg(a)*88f, 4f, 3f, a+180f);

                float an = -(fyy/12f)*360f+90;
                Drawf.tri(x+Mathf.cosDeg(an)*12f,y+Mathf.sinDeg(an)*12f, 6f, 56, an);
                Drawf.tri(x+Mathf.cosDeg(an)*12f,y+Mathf.sinDeg(an)*12f, 6f, 3f, an+180f);

            });
            Draw.reset();
        }

        @Override
        public void draw() {
            super.draw();
        }

    }
}
