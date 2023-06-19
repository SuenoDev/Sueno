package org.durmiendo.sueno.ui.dialogs;

import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

public class SatelliteDialog extends BaseDialog {
    public SatelliteDialog(String title) {
        super(title);

        addCloseButton();

        setup();

        shown(this::setup);
    }
    public void setup() {
        cont.clear();


        Table table = new Table();

        ScrollPane pane = new ScrollPane(table);


        cont.add(pane);
    }
}
