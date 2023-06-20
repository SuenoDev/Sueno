package org.durmiendo.sueno.ui;

import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satelliteDialog;

    public SUI() {
        satelliteDialog = new SatelliteDialog();
    }

    /** After content load **/
    public void build() {

    }
}
