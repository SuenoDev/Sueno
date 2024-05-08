package org.durmiendo.sueno.core;

import mindustry.mod.Mods;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.files.InternalFileTree;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.ui.SUI;

public class SVars {
    /** Mod core object. **/
    public static SCore core;
    /** Mod data container. **/
    public static Mods.LoadedMod sueno;
    /** All mod UI there. **/
    public static SUI ui = new SUI();
    /** JAR files accessor. **/
    public static InternalFileTree internalFileTree = new InternalFileTree(SCore.class);

    //controllers
    public static CelestialBodyController celestialBodyController;
    public static WeatherController weathercontroller;
    public static TemperatureController temperatureController;

    /** Useless **/
    public static boolean onCampaign;
    public static boolean debug = true;
}