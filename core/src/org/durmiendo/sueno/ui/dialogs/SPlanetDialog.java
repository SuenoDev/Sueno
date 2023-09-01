package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.PlanetDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;


public class SPlanetDialog extends PlanetDialog {


    @Override
    public Dialog show() {
        Events.fire(new CampainOpen());
        return super.show();
    }

    @Override
    public void closeOnBack(Runnable callback) {
        Events.fire(new CampainClose());
        super.closeOnBack(callback);
    }

    @Override
    public void draw() {
        super.draw();
        SVars.celestialBodyController.draw();
    }

    public SPlanetDialog() {
        super();
    }
}
