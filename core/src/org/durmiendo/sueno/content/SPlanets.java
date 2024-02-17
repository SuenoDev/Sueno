package org.durmiendo.sueno.content;


import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.g3d.Camera3D;
import arc.graphics.g3d.VertexBatch3D;
import arc.graphics.gl.Shader;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;
import mindustry.type.Sector;
import org.durmiendo.sueno.core.SVars;


public class SPlanets extends Planets {
    public static Planet hielo;

    public static void load() {
        hielo = new Planet("hielo", sun, 4.3f, 1) {
            {
                generator = new ErekirPlanetGenerator();
                meshLoader = () -> new HexMesh(this, 5);
                cloudMeshLoader = () -> new MultiMesh(
                        new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("77dde7").a(0.3f), 2, 0.42f, 1f, 0.43f),
                        new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("003153").a(0.3f), 2, 0.42f, 1.2f, 0.45f)
                );
                launchCapacityMultiplier = 0.5f;
                orbitRadius = 1000;
                sectorSeed = 2;
                allowWaves = true;
                allowWaveSimulation = true;
                allowSectorInvasion = true;
                allowLaunchSchematics = true;
                enemyCoreSpawnReplace = true;
                allowLaunchLoadout = true;
                prebuildBase = false;
                ruleSetter = r -> {
                    r.waveTeam = Team.crux;
                    r.placeRangeCheck = false;
                    r.showSpawns = false;
                };
                iconColor = Color.valueOf("7d4dff");
                atmosphereColor = Color.valueOf("3c1b8f");
                atmosphereRadIn = 0.02f;
                atmosphereRadOut = 0.3f;
                startSector = 15;
                alwaysUnlocked = true;
                accessible = true;
                visible = true;
                landCloudColor = Pal.spore.cpy().a(0.5f);
                hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);
            }

            @Override
            public void renderSectors(VertexBatch3D batch, Camera3D cam, PlanetParams params) {
                super.renderSectors(batch, cam, params);

            }
        };
    }
}
