package org.durmiendo.sueno.ui;

import arc.util.Log;
import mindustry.Vars;
import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satellite;
    public SPlanetDialog planet;

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        satellite = new SatelliteDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog();

        Log.info("Sueno ui init");
    }
}
