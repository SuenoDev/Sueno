package org.durmiendo.sueno.ui;

import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satelliteDialog;

    public SPausedDialog pause;
    public SPlanetDialog planet;

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        satelliteDialog = new SatelliteDialog();
        pause = new SPausedDialog();
        planet = new SPlanetDialog();
    }
}
