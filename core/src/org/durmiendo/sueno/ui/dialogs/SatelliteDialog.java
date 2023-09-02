package org.durmiendo.sueno.ui.dialogs;

import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.satellites.E;

public class SatelliteDialog extends BaseDialog {
    public SatelliteDialog() {
        super("@stelitedialog");

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
    }

    public void build() {

        Table ful = new Table();
        ScrollPane sp = new ScrollPane(null);
        Table sats = new Table();

        ful.setFillParent(true);
        cont.addChild(ful);

        ful.add(sp);
        sp.addChild(sats);

        SVars.celestialBodyController.cbs.forEach(s -> {
            sats.add(new E());
        });

    }

    public void destroy() {
        cont.clear();
    }
}
