package org.durmiendo.sueno.core;

import arc.assets.Loadable;
import org.durmiendo.sueno.controllers.MineralController;
import org.durmiendo.sueno.controllers.SatelliteController;
import org.durmiendo.sueno.controllers.TemperatureController;
import org.durmiendo.sueno.minerals.Minerals;
import org.durmiendo.sueno.ui.SUI;

public class SVars implements Loadable {

    //temperature params
    public static float frostDamage = 0.35F;
    public static float frostefficiency = 0.3f;

    public static float freezingPower = 4.35F;
    public static float minT = -179.8f;

    //ui
    public static SUI ui = new SUI();

    //controllers
    public static TemperatureController temperatureController;
    public static SatelliteController satelliteController;
    public static MineralController mineralController;

    //status
    public static boolean onCampain;

    //minerals

    public static Minerals minerals;

}