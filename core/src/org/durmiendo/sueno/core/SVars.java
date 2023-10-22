package org.durmiendo.sueno.core;

import arc.assets.Loadable;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.TemperatureController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.math.Map;
import org.durmiendo.sueno.ui.SUI;
import org.durmiendo.sueno.ui.dialogs.CBDialog;

public class SVars implements Loadable {

    //temperature params
    public static float freezingDamage = 0.35f;
    public static float freezingEfficiency = 0.3f;
    public static float startT = 100;

    public static float freezingPower = -1.4f;
    public static float maxFreezingSpeed = 2f;
    public static float startCeiling = 150;
    public static float maxSafeT = 120;
    public static float minEffectivityT = 100;
    public static float minSafeT = -100;
    public static float minHeatDamage = 20;
    public static float maxHeatDamage = 300;
    public static float maxBoost = 20;

    //ui
    public static SUI ui = new SUI();
    public static CBDialog cbs = new CBDialog();

    //controllers
    public static TemperatureController temperatureController;
    public static CelestialBodyController celestialBodyController;
    public static WeatherController weathercontroller;

    //status
    public static boolean FIXMAN_BEZVKUSNIY_DUSHNILA_I_OBSERAET_MOI_TEXTURI_Mode = false;
    public static boolean onCampain;

    //minerals

    //celestials body

    public static float def = 30;
}