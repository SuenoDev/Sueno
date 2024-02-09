package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import arc.scene.ui.Dialog;
import mindustry.ui.dialogs.PlanetDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.SEvents;

import static arc.Core.settings;

public class SPlanetDialog extends PlanetDialog {
    @Override
    public Dialog show() {
        settings.put("campaignselect", true);
        Events.fire(new SEvents.CampaignOpenEvent());
        return super.show();
    }

    @Override
    public void closeOnBack(Runnable callback) {
        Events.fire(new SEvents.CampaignCloseEvent());
        super.closeOnBack(callback);
    }

    public void draw() {
        super.draw();
        SVars.celestialBodyController.draw();
    }

    public SPlanetDialog() {
        super();
    }
}
