package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import mindustry.ui.dialogs.PausedDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.game.SEventType;

public class SPausedDialog extends PausedDialog {
    public void runExitSave(){
        super.runExitSave();
        if (SVars.onCampain) {
            Events.fire(new SEventType.CampainClose());
        }
    }
}
