package org.durmiendo.sueno.content;


import arc.graphics.Color;
import arc.math.geom.Mat3D;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.maps.planet.TantrosPlanetGenerator;
import mindustry.type.Planet;


public class SPlanets {
    public static Planet hielo;

    public static void load() {
        hielo = new Planet("hielo", Planets.sun, 1f, 2) {
            {
                generator = new TantrosPlanetGenerator();
                meshLoader = () -> new HexMesh(this, 5);
                cloudMeshLoader = () -> new MultiMesh(
                        new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("77dde7").a(0.3f), 2, 0.42f, 1f, 0.43f),
                        new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("003153").a(0.3f), 2, 0.42f, 1.2f, 0.45f)
                );
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
                atmosphereColor = Color.valueOf("3c1b8f");
                atmosphereRadIn = 0.02f;
                atmosphereRadOut = 0.3f;
                startSector = 15;
                alwaysUnlocked = true;
                accessible = true;
                visible = false;
                landCloudColor = Pal.spore.cpy().a(0.5f);
                hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);
            }

            public void binit() {

            }
            @Override
            public void draw(PlanetParams params, Mat3D projection, Mat3D transform) {
                super.draw(params, projection, transform);

            }
        };
    }
}
