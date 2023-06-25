package org.durmiendo.sueno.ui;

import arc.util.Log;
import mindustry.Vars;
import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satellite;

    public SPausedDialog pause;
    public SPlanetDialog planet;

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        satellite = new SatelliteDialog();

        paused = new SPausedDialog();
        Vars.ui.paused = pause;
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;


        Log.info("Sueno ui init");
    }
}
