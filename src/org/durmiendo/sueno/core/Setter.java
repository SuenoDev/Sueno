package org.durmiendo.sueno.core;

import arc.Core;
import arc.Events;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Setter {
    public static void load() {
        loadUI();
    }

    private static void loadUI() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            if (Core.settings.getBool("not-completed-message")) return;
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("@mod.notification");
                dialog.setStyle(Styles.defaultDialog);
                dialog.cont.add("@mod.notification.not-completed.message").row();
                dialog.cont.button("@mod.notification.ok", dialog::hide).size(100f, 50f);
                dialog.show();
                Core.settings.put("not-completed-message", true);
            });
        });
    }
}
