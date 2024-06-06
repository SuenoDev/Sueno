package org.durmiendo.sueno.maps.planet;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec3;
import mindustry.Vars;
import mindustry.content.Loadouts;
import mindustry.game.Schematics;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.TileGen;
import org.durmiendo.sueno.content.SBlocks;

public class HieloPlanetGenerator extends PlanetGenerator {{
        baseSeed = 2;
        defaultLoadout = Loadouts.basicBastion;
    }

    @Override
    public float getSizeScl() {
        return 3000f;
    }

    @Override
    public float getHeight(Vec3 vec3) {
        return 0;
    }

    @Override
    public Color getColor(Vec3 vec3) {
        float r = Mathf.randomSeed(Mathf.round(vec3.y * 1000),0.084375f);
        return new Color(0.6f - r, 0.671875f - r, 0.75f + Mathf.randomSeed(Mathf.round(vec3.y * 1000), 0.01f));
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = SBlocks.devNone;
    }

    @Override
    protected void generate(){
        pass((x, y) -> {
            float max = 0;
            for(Point2 p : Geometry.d8edge){
                max = Math.max(0, Vars.world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                block = floor.asFloor().wall;
            }
        });

        Schematics.placeLaunchLoadout(width / 2, height / 2);
    }
}
