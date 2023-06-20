package org.durmiendo.sueno.core;

import arc.assets.Loadable;
import org.durmiendo.sueno.controllers.TemperatureController;
import org.durmiendo.sueno.ui.SUI;

public class SVars implements Loadable {
    public static float frostDamage = 0.35F;
    public static float freezingPower = 4.35F;
    public static float minT = -179.8f;

    public static SUI ui = new SUI();

    public static TemperatureController temperatureController;
}