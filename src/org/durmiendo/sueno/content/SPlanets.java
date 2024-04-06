package org.durmiendo.sueno.content;


import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;


public class SPlanets {
    public static Planet hielo;

    public static void load() {
        hielo = new Planet("hielo", Planets.sun, 1f, 2) {
            {
                generator = new ErekirPlanetGenerator();
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
                visible = true;
                landCloudColor = Pal.spore.cpy().a(0.5f);
                hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);
            }
        };
    }
}
