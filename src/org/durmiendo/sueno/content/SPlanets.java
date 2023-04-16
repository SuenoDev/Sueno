package org.durmiendo.sueno.content;


import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;

public class SPlanets extends Planets {
    public static Planet hielo;

    public static void load() {
        hielo = new Planet("hielo", sun, 0.7f, 5){
            {
             this.generator = new SerpuloPlanetGenerator();
                this.meshLoader = () -> {
                    return new HexMesh(this, 6);
                };
                this.alwaysUnlocked = true;;
                this.defaultEnv = 17;
                this.startSector = 10;
                this.atmosphereRadIn = 0;
                this.atmosphereRadOut = 0;
                this.tidalLock = false;
                this.orbitSpacing = 7.0F;
                this.totalRadius += 2.6F;
                this.lightSrcTo = 0.5F;
                this.lightDstFrom = 0.2F;
                this.clearSectorOnLose = true;
                this.defaultCore = Blocks.coreBastion;
                this.iconColor = Color.valueOf("c5e6fa");
                this.hiddenItems.addAll(Items.serpuloItems);
                this.enemyBuildSpeedMultiplier = 0.4F;
                this.updateLighting = false;
                this.ruleSetter = (r) -> {
                    r.waveTeam = Team.malis;
                    r.placeRangeCheck = false;
                    r.showSpawns = true;
                    r.fog = true;
                    r.staticFog = true;
                    r.lighting = false;
                    r.coreDestroyClear = true;
                    r.onlyDepositCore = true;
                };
                this.unlockedOnLand.add(Blocks.coreCitadel);
            }
        };
    }

}
