package org.durmiendo.sueno.entities.bullet;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.entities.Effect;
import mindustry.entities.bullet.PointLaserBulletType;
import mindustry.gen.Bullet;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class AreaLaserBullet extends PointLaserBulletType {

    public Effect effect = new Effect(60f, e -> {
        stroke(3f);
        color(Color.white);
        randLenVectors(e.id+1, 11, 5f, 8f, (x, y) -> {
            lineAngle(x, y, Mathf.angle(x, y), 12f, 12f);
        });
    });

    @Override
    public void draw(Bullet b){
        //Draw.color(color);
        //Drawf.laser(laser, laserEnd, b.x, b.y, b.aimX, b.aimY, 4f);

        Draw.reset();
    }
}
