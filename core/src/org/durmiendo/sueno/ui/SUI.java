package org.durmiendo.sueno.ui;

import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satelliteDialog;

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        satelliteDialog = new SatelliteDialog();
    }
}
