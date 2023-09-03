package org.durmiendo.sueno.ui;

import mindustry.Vars;
import mindustry.core.UI;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.CBDialog;

public class SUI extends UI {
    public CBDialog satellite;
    public SPlanetDialog planet;

    public SUI() {

    }

    // TODO @nekit508: кри прикол в том, что изменения не доходят до показывания планетдиалога...up
    /** On mod init **/
    public void build() {
        satellite = new CBDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog();
    }
}
