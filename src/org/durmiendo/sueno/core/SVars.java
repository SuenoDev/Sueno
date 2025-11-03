package org.durmiendo.sueno.core;

import arc.files.Fi;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import mindustry.Vars;
import mindustry.mod.Mods;
import org.durmiendo.sap.SuenoSettings;
import org.durmiendo.sueno.files.InternalFileTree;
import org.durmiendo.sueno.processors.SuenoInputProcessor;
import org.durmiendo.sueno.spacestations.SpaceStations;
import org.durmiendo.sueno.statuses.StatusEffectsController;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.ui.SUI;

public class SVars {
    /** Mod core object. **/
    public static Sueno core;
    /** Mod data container. **/
    public static Mods.LoadedMod sueno;
    /** All mod UI there. **/
    public static SUI ui = new SUI();
    /** JAR files accessor. **/
    public static InternalFileTree internalFileTree = new InternalFileTree(Sueno.class);

    //controllers
    public static SpaceStations spaceStations;
    public static TemperatureController temperatureController;
    public static StatusEffectsController statusEffectsController;


    public static SuenoInputProcessor input;

    public static boolean onCampaign;
    public static Fi mainDir = Vars.dataDirectory.child("sueno");
    public static Fi dumpDir = mainDir.child("dump");

    @SuenoSettings(def = 1)
    public static boolean extendedLogs = true;

    @SuenoSettings(def = 1)
    public static boolean dataVisible = true;

    @SuenoSettings(def = 1)
    public static boolean versionWarning = true;
    
    @SuenoSettings(def = 1, steep = .1f, min = 1, max = 6, accuracy = 1)
    public static float quality = 1.0f;
    
    @SuenoSettings(def = 1, steep = 1f, min = 1, max = 3, accuracy = 1)
    public static float qval;
    
    public static float getQuality() {
        return quality * quality;
    }
}