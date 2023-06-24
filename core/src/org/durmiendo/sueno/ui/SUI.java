package org.durmiendo.sueno.ui;

import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;
import org.durmiendo.sueno.ui.dialogs.SatellitesDialog;

public class SUI extends UI {
    public SatelliteDialog satellite;

    public SPausedDialog pause;
    public SPlanetDialog planet;
    public SatellitesDialog satellines;

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        satellite = new SatelliteDialog();
        pause = new SPausedDialog();
        planet = new SPlanetDialog();
        satellines = new SatellitesDialog();
    }
}
