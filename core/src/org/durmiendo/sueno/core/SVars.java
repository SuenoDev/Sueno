package org.durmiendo.sueno.core;

import arc.assets.Loadable;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.temperature.Controller;
import org.durmiendo.sueno.ui.SUI;
import org.durmiendo.sueno.ui.dialogs.CBDialog;

public class SVars implements Loadable {

    //temperature params
    public static float freezingDamage = 0.35f;
    public static float freezingPower = -1.4f;

    public static float startTemperature = 0;

    public static float minEffectivityTemperature = 100;
    public static float minSafeTemperature = -100;
    public static float minTemperatureDamage = 20;

    public static float maxFreezingSpeed = 2f;
    public static float maxSafeTemperature = 120;
    public static float maxHeatDamage = 300;
    public static float maxBoost = 20;

    //ui
    public static SUI ui = new SUI();
    public static CBDialog cbs = new CBDialog();

    //controllers
    //public static TemperatureController temperatureController;
    public static CelestialBodyController celestialBodyController;
    public static WeatherController weathercontroller;
    public static Controller temperatureController$;

    //status
    public static boolean FIXMAN_BEZVKUSNIY_DUSHNILA_I_OBSERAET_MOI_TEXTURI_Mode = false; // durmiendo moment
    public static boolean onCampaign;

    //minerals

    //celestials body

    public static float def = 30;
}