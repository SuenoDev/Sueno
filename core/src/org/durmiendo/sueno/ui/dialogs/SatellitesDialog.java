package org.durmiendo.sueno.ui.dialogs;

import arc.scene.ui.Dialog;
import arc.scene.ui.ImageButton;
import arc.struct.Seq;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;



public class SatellitesDialog extends BaseDialog {

    //Seq<ImageButton> ib = new Seq<>();

    public SatellitesDialog() {
        super("", Styles.fullDialog);
    }

    @Override
    public void draw() {
    }

    @Override
    public Dialog show() {

        return super.show();
    }


    @Override
    public void closeOnBack(Runnable callback) {
        super.closeOnBack(callback);
    }
}
