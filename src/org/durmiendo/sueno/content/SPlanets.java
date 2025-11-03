package org.durmiendo.sueno.content;


import arc.graphics.Color;
import arc.graphics.g3d.VertexBatch3D;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.*;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.g3d.wobj.Obj;
import org.durmiendo.sueno.graphics.g3d.wobj.ObjParser;
import org.durmiendo.sueno.maps.planet.HieloPlanetGenerator;
import org.durmiendo.sueno.spacestations.SpaceStations;


public class SPlanets {
    public static Planet hielo;

    public static void load() {
        hielo = new Planet("hielo", Planets.sun, 1f, 2) {
            {
                generator = new HieloPlanetGenerator();
                meshLoader = () -> new HexMesh(this, 5);

                cloudMeshLoader = () -> new MultiMesh(
                        new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("77dde7").a(0.12f), 3, 0.42f, 1f, 0.43f),
                        new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("003153").a(0.1f), 3, 0.42f, 1.2f, 0.45f)
                );
                orbitRadius = 20f;

                launchCapacityMultiplier = 0.5f;
                sectorSeed = 2;
                allowWaves = true;
                allowWaveSimulation = true;
                allowSectorInvasion = true;
                allowLaunchSchematics = false;
                enemyCoreSpawnReplace = true;
                allowLaunchLoadout = true;
                prebuildBase = false;
                ruleSetter = r -> {
                    r.waveTeam = Team.crux;
                    r.placeRangeCheck = false;
                    r.showSpawns = false;
                };
                iconColor = Color.valueOf("aaaaff");
                atmosphereColor = Color.valueOf("060f16");
                atmosphereRadIn = 0.02f;
                atmosphereRadOut = 0.3f;
                startSector = 15;
                alwaysUnlocked = true;
                accessible = true;
                visible = true;
                landCloudColor = Pal.spore.cpy().a(0.5f);
                hiddenItems.removeAll(Items.erekirItems);
                hiddenItems.removeAll(Items.serpuloItems);
            }
            
            @Override
            public void draw(PlanetParams params, Mat3D projection, Mat3D transform) {
                SVars.spaceStations.render(projection, transform);
                
                super.draw(params, projection, transform);
           }
            
            @Override
            public void drawArc(VertexBatch3D batch, Vec3 a, Vec3 b, Color from, Color to, float length, float timeScale, int pointCount) {
                super.drawArc(batch, a, b, from, to, length, timeScale, pointCount);
            }
        };
    }
}

