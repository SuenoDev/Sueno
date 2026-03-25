package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.FloatSeq;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import mindustry.ui.Fonts;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.environment.Floor;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.utils.SLog;

import java.util.concurrent.atomic.AtomicInteger;

public class VioTurret extends PowerTurret {
    public int lifeEatRange = 7;
    Seq<BulletType> bulletTypes = new Seq<>();
    FloatSeq changes = new FloatSeq();
    float a = 0;

    public VioTurret(String name) {
        super(name);
    }

    public void addb(BulletType bulletType, float c) {
        bulletTypes.add(bulletType);
        changes.add(c);
        a += c;
    }
    
    
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        
        int num = 0;
        
        for (int i = x-lifeEatRange; i <= x+lifeEatRange; i++) {
            for (int j = y-lifeEatRange; j <= y+lifeEatRange; j++) {
                if (isphos(i, j, x, y)) {
                    drawp(i, j, x, y);
                    num++;
                }
            }
        }
        
        Fonts.def.draw(Integer.toString(num), x*8f, y*8f+size*4f+12f, Color.white, 0.42f, false, Align.center);
    }
    
    boolean isphos(int x, int y, int bx, int by) {
        if (x > bx+lifeEatRange || y > by+lifeEatRange || y < by-lifeEatRange || x < bx - lifeEatRange) return false;
        if (x > bx - size/2 && x < bx + size/2+1 && y > by - size/2 && y < by + size/2+1) return false;
        
        
        Tile t = Vars.world.tiles.get(x, y);
        if (t != null) {
            Floor f = t.overlay();
            if (f != null) {
                return f.equals(SBlocks.phosphor);
            }
        }
        
        return false;
    }
    
    void drawp(int x, int y, int bx, int by) {
        float xr = x * 8f;
        float yr = y * 8f;
        
        // Константа для аппроксимации дуги окружности кривой Безье (для радиуса 2)
        // Рассчитывается как: (4 * (sqrt(2) - 1) / 3) * radius
        float c = 1.10456f;
        
        Draw.color(Color.orange);
        Lines.stroke(0.35f);
        
        // Верх
        if (!isphos(x, y + 1, bx, by))
            Lines.line(xr - 4f, yr + 4f, xr + 4f, yr + 4f);
        
        // Низ
        if (!isphos(x, y - 1, bx, by))
            Lines.line(xr - 4f, yr - 4f, xr + 4f, yr - 4f);
        
        // Право
        if (!isphos(x + 1, y, bx, by))
            Lines.line(xr + 4f, yr - 4f, xr + 4f, yr + 4f);
        
        // Лево
        if (!isphos(x - 1, y, bx, by))
            Lines.line(xr - 4f, yr - 4f, xr - 4f, yr + 4f);
        
//        // Верхний левый угол
//        Lines.curve(xr - 4f, yr + 2f,
//                xr - 4f, yr + 2f + c,
//                xr - 2f - c, yr + 4f,
//                xr - 2f, yr + 4f,
//                8);
//
//        // Нижний левый угол
//        Lines.curve(xr - 4f, yr - 2f,
//                xr - 4f, yr - 2f - c,
//                xr - 2f - c, yr - 4f,
//                xr - 2f, yr - 4f,
//                8);
//
//        // Нижний правый угол
//        Lines.curve(xr + 2f, yr - 4f,
//                xr + 2f + c, yr - 4f,
//                xr + 4f, yr - 2f - c,
//                xr + 4f, yr - 2f,
//                8);
//
//        // Верхний правый угол
//        Lines.curve(xr + 2f, yr + 4f,
//                xr + 2f + c, yr + 4f,
//                xr + 4f, yr + 2f + c,
//                xr + 4f, yr + 2f,
//                8);
//
//
//        // Верх
//        if (!isphos(x, y + 1, bx, by))
//            Lines.line(xr - 2f, yr + 4f, xr + 2f, yr + 4f);
//
//        // Низ
//        if (!isphos(x, y - 1, bx, by))
//            Lines.line(xr - 2f, yr - 4f, xr + 2f, yr - 4f);
//
//        // Право
//        if (!isphos(x + 1, y, bx, by))
//            Lines.line(xr + 4f, yr - 2f, xr + 4f, yr + 2f);
//
//        // Лево
//        if (!isphos(x - 1, y, bx, by))
//            Lines.line(xr - 4f, yr - 2f, xr - 4f, yr + 2f);
        
        Draw.reset();
    }
    
    public class VioTurretBuild extends PowerTurret.PowerTurretBuild {
        @Override
        public boolean isValid() {
            return super.isValid();
        }
        
        public BulletType peekAmmo() {
            if (bulletTypes.size == 0) return null;
            float randomNumber = Mathf.rand.nextFloat();
            float cumulativeProbability = 0;
            for (int i = 0; i < changes.size; i++) {
                cumulativeProbability += changes.get(i)/a;
                if (randomNumber <= cumulativeProbability) {
                    return bulletTypes.get(i);
                }
            }

            return bulletTypes.get(bulletTypes.size -1);
        }
        
        protected void handleBullet(Bullet bullet, float offsetX, float offsetY, float angleOffset){
            int num = 0;
            
            for (int i = tileX()-lifeEatRange; i <= tileX()+lifeEatRange; i++) {
                for (int j = tileY()-lifeEatRange; j <= tileY()+lifeEatRange; j++) {
                    if (isphos(i, j, tileX(), tileY())) {
                        num+=1;
                        if (Mathf.chance(0.2f)) {
                            SFx.phosevil.at(i*8f, j*8f, Mathf.angle(tileX() - i, tileY() - j) - 180f);
                            SFx.phosevil2.at(i*8f, j*8f, Mathf.angle(bullet.aimX - i*8f, bullet.aimY - j*8f));
                        }
                    }
                }
            }
            

            bullet.damage += Mathf.sqr(num) * 0.95f;
        }
    }
}
