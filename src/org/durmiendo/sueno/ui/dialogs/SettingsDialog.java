package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class SettingsDialog extends BaseDialog {

    public SettingsDialog() {
        super("@settings");

        Vars.ui.settings.button("@settings.graphics", Core.atlas.drawable("sueno-sueno-white"), () -> {

        });
    }
}
