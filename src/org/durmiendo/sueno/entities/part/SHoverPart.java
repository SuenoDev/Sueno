package org.durmiendo.sueno.entities.part;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.entities.part.DrawPart;

public class SHoverPart extends DrawPart {
    public float radius = 4f;
    public float x, y, rotation, phase = 50f, stroke = 3f, minStroke = 0.12f, speed = 0.3f;
    public int circles = 2, sides = 4;
    public Color color = Color.white;
    public boolean mirror = false;
    public float layer = -1f, layerOffset = 0f;

    @Override
    public void draw(PartParams params){
        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(under && turretShading) Draw.z(z - 0.0001f);

        Draw.z(Draw.z() + layerOffset);

        int len = mirror && params.sideOverride == -1 ? 2 : 1;

        Draw.color(color);

        for(int c = 0; c < circles; c++){
            float fin = ((Time.time / phase + (float)c / circles) % 1f);
            Lines.stroke((1f-fin) * stroke + minStroke);

            for(int s = 0; s < len; s++){
                //use specific side if necessary
                int i = params.sideOverride == -1 ? s : params.sideOverride;

                float sign = (i == 0 ? 1 : -1) * params.sideMultiplier;
                Tmp.v1.set((x) * sign, y).rotate(params.rotation - 90);

                float
                        rx = params.x + Tmp.v1.x,
                        ry = params.y + Tmp.v1.y;

                Lines.poly(rx, ry, sides, radius * fin, (params.rotation + fin*360f*speed)*sign);
            }
        }

        Draw.reset();

        Draw.z(z);
    }

    @Override
    public void load(String name){

    }
}
