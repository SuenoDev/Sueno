package org.durmiendo.sueno.core;

import arc.files.Fi;
import arc.graphics.Texture;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.mod.Mods;
import org.durmiendo.sap.SuenoSettings;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.files.InternalFileTree;
import org.durmiendo.sueno.processors.SuenoInputProcessor;
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

    public static ObjectMap<Texture, Texture> textureToNormal = new ObjectMap<>();
    public static ObjectSet<Texture> noNormal = new ObjectSet<>();
    public static ObjectMap<Texture, String> regions = new ObjectMap<>();

    //controllers
    public static CelestialBodyController celestialBodyController;
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
}