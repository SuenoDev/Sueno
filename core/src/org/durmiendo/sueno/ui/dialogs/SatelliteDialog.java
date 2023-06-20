package org.durmiendo.sueno.ui.dialogs;

import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

public class SatelliteDialog extends BaseDialog {
    public SatelliteDialog() {
        super("@stelitedialog");

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
    }

    public void build() {

    }

    public void destroy() {
        cont.clear();
    }
}
