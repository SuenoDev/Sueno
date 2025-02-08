package org.durmiendo.sueno.core;

import arc.files.Fi;
import arc.graphics.Texture;
import arc.struct.ObjectMap;
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

//    public static FrameBuffer nb;
//    public static FrameBuffer rnb;
//    public static FrameBuffer fb;

    //controllers
    public static CelestialBodyController celestialBodyController;
    public static TemperatureController temperatureController;
    public static StatusEffectsController statusEffectsController;


    public static SuenoInputProcessor input;

    public static boolean onCampaign;
    public static Fi mainDirecory = Vars.dataDirectory.child("sueno/");

    @SuenoSettings()
    public static boolean extendedLogs = false;

    @SuenoSettings(def = 1)
    public static boolean dataVisible = true;

    @SuenoSettings(def = 1)
    public static boolean versionWarning = true;
}