package org.durmiendo.sueno.world.blocks;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.ui.Fonts;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.utils.SStrings;

import static arc.graphics.g2d.Lines.stroke;

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
                    d();
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

        public float t=600;//712.5f;
        public float nn=t;


        @Override
        public void update() {
            super.update();
            float n = (nn-719.2f);
            nn+=Time.delta/60f;
            t+=Time.delta/60f/Math.max(n*n*n*1.7f,1f);

        }


        public void d() {
            Draw.reset();

            stroke(2f);
            float per = Mathf.clamp((nn-716.5f)/5.3f, 0f, 1f);


            Draw.z(111f);


            inj(per, () -> {
                Draw.color(Pal.lightFlame);
                Lines.circle(x, y, 186f);

                float aa = 90f-360f/12f;

                for(int i = 0;i<12;i++){
                    Draw.color(Pal.lightFlame);
                    Draw.rect("sueno-" + (i + 1) + "-num",x+Mathf.cosDeg(aa)*168f,y+Mathf.sinDeg(aa)*168f, 12f, 12f);

                    aa-=360f/12f;
                }


                Draw.color(Pal.lightFlame);
                float mb = -Mathf.sin(Mathf.pi*nn)/Math.abs(Mathf.sin(Mathf.pi*nn));
                float ma = mb*(Mathf.cos(Mathf.pi*nn)/2f)+0.5f;
                float my = Mathf.mod(Mathf.pow(ma, 64), 1)+Mathf.floor(nn);
                Fonts.def.draw("my "+SStrings.fixed(my, 2), x, y + 8, Color.red, 0.4f, true, Align.left);
                Fonts.def.draw("ma "+SStrings.fixed(ma, 2), x, y, Color.red, 0.4f, true, Align.left);
                Fonts.def.draw("mb "+SStrings.fixed(mb, 2), x, y - 8, Color.red, 0.4f, true, Align.left);
                Fonts.def.draw("nn "+SStrings.fixed(nn, 2), x, y - 16, Color.red, 0.4f, true, Align.left);


                float a = -(my/12f)*360f+90;
                Drawf.tri(x+Mathf.cosDeg(a)*88f,y+Mathf.sinDeg(a)*88f, 4f, 66, a);
                Drawf.tri(x+Mathf.cosDeg(a)*88f,y+Mathf.sinDeg(a)*88f, 4f, 3f, a+180f);

                float an = -(my/60f/12f)*360f+90;
                Drawf.tri(x+Mathf.cosDeg(an)*8f,y+Mathf.sinDeg(an)*8f, 6f, 62, an);
                Drawf.tri(x+Mathf.cosDeg(an)*8f,y+Mathf.sinDeg(an)*8f, 6f, 3f, an+180f);

            });
            Draw.reset();
        }

        @Override
        public void draw() {
            super.draw();
        }
    }
}
