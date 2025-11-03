package org.durmiendo.sueno.world.blocks.defense;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import com.google.errorprone.annotations.Var;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import org.durmiendo.sueno.utils.SLog;

import java.util.ArrayList;
import java.util.List;

import static mindustry.Vars.indexer;

public class TriShield extends Block {
    public TriShield(String name) {
        super(name);
        update = true;
        solid = true;
        group = BlockGroup.projectors;
        envEnabled |= Env.space;
    }
    
    
    public class TriShieldBuild extends Building {
        public Seq<Tri> points;
        
        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            Building b = super.init(tile, team, shouldAdd, rotation);
            
            points = gen(200);
            
            return b;
        }
        
        @Override
        public void update() {
            super.update();
            
        }
        
        @Override
        public void draw() {
            super.draw();
            
//            Log.info(points);
//            Draw.z(Layer.effect);
            for (Tri tri : points) {
                Draw.rect("sueno-tri1", x + tri.x, y + tri.y, 8f, 8f, 0f);
            }
        }
    }
    
    public static Seq<Tri> gen(int count) {
        Seq<Tri> shieldPoints = new Seq<>();
        
        float angle = 190f / 2f;
        float len = 100;
        for (int i = 0; i < count; i++) {
            float d = Mathf.random(48, len);
            float dc = d / 100f* 1.54f;
            float a = Mathf.random(-angle*dc, angle*dc);
            
            float x = Mathf.cosDeg(a) * d;
            float y = Mathf.sinDeg(a) * d;
            Tri tri = new Tri(x, y, angle);
            shieldPoints.add(tri);
            
        }
        
        return shieldPoints;
    }
    
    public static class Tri {
        public float x, y;
        public float angle;
        public Tri(float x, float y, float angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }
}