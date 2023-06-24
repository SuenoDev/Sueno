package org.durmiendo.sueno.ui.dialogs;

import arc.Events;
import arc.scene.Element;
import arc.scene.ui.Dialog;
import arc.scene.ui.ImageButton;
import arc.struct.Seq;
import arc.struct.SnapshotSeq;
import arc.util.Log;
import mindustry.ui.dialogs.PlanetDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.CampainClose;
import org.durmiendo.sueno.events.CampainOpen;
import org.durmiendo.sueno.sattelites.Satellite;


public class SPlanetDialog extends PlanetDialog {

    public Seq<ImageButton> sib = new Seq<>();
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
        for (Satellite s : SVars.satelliteController.getSatellites()) {
            s.draw();
        }
        Log.info("planet draw");
    }
}
