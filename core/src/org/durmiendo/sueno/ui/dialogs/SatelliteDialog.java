package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.Input;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.satellites.CelestialBody;
import org.durmiendo.sueno.satellites.Satellite;

public class SatelliteDialog extends BaseDialog {
    Satellite s;

    public SatelliteDialog(DialogStyle style) {
        super("@stellitedialog", style);

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
    }

    public void build()  {

        cont.pane(p -> {

            Table info = new Table();
            info.add(new Label("Высота: " + s.orbitRadius*15000 + " м\nСкорость: " + s.speed + " ю/кадр\nПрочность: " + s.health));
            p.add(info);
            p.row();
            Table control = new Table();
            ImageButton up = new ImageButton(Icon.up);
            up.clicked(() -> {
               s.up();
            });
            ImageButton down = new ImageButton(Icon.down);
            down.clicked(() -> {
                s.down();
            });
            TextButton d = new TextButton("destroy");
            d.clicked(() -> {
                s.destroy();
            });
            control.add(up);
            control.add(down);
            control.row();
            control.add(d);
            p.add(control);
        });

    }

    public void destroy() {
        cont.clear();
    }
    public void put(Satellite ss) {
        s = ss;
    }

    public void upd(Satellite s) {
        destroy();
        put(s);
        build();
    }
}
