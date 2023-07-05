package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import mindustry.ui.dialogs.PausedDialog;
import org.durmiendo.sueno.events.CampainClose;


public class SPausedDialog extends PausedDialog {
    public void runExitSave(){
        Events.fire(new CampainClose());
        super.runExitSave();
    }
}
