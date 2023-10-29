package org.durmiendo.sueno.ui;


import mindustry.Vars;
import mindustry.core.UI;
import org.durmiendo.sueno.ui.fragments.GodModeFragment;

public class SUI extends UI {
    /*public CBDialog satellite;
    public SPlanetDialog planet;*/ // satellites removed, right?

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        /*satellite = new CBDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog();*/ // right?
        Vars.ui.hudGroup.fill(t -> {
            t.left();
            t.add(new GodModeFragment());
        });
    }
}
