package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import arc.scene.ui.Dialog;
import mindustry.ui.dialogs.PlanetDialog;
import org.durmiendo.sueno.game.SEventType;

public class SPlanetDialog extends PlanetDialog {
    @Override
    public Dialog show() {
        Events.fire(new SEventType.CampainOpen());
        return super.show();

    }

    @Override
    public void closeOnBack(Runnable callback) {
        Events.fire(new SEventType.CampainClose());
        super.closeOnBack(callback);
    }
}
