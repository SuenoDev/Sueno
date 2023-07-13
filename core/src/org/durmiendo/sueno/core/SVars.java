package org.durmiendo.sueno.core;

import arc.assets.Loadable;
import org.durmiendo.sueno.controllers.SatelliteController;
import org.durmiendo.sueno.controllers.TemperatureController;
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

    //status
    public static boolean FIXMAN_BEZVKUSNIY_DUSHNILA_I_OBSERAET_MOI_TEXTURI_Mode = false;
    public static boolean onCampain;
    //minerals


}