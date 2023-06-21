package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import mindustry.ui.dialogs.PausedDialog;
import org.durmiendo.sueno.game.SEventType;
import org.durmiendo.sueno.core.SVars;

public class SPausedDialog extends PausedDialog {
    public void runExitSave(){
        super.runExitSave();
        if (SVars.onCampain) {
            Events.fire(new SEventType.CampainClose());
        }
    }
}
