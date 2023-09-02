package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.event.ClickListener;
import arc.scene.event.HandCursorListener;
import arc.scene.event.Touchable;
import arc.scene.ui.Image;
import arc.scene.ui.Tooltip;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Time;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.satellites.CelestialBody;

import java.util.concurrent.atomic.AtomicInteger;

import static arc.Core.settings;
import static mindustry.Vars.*;
import static mindustry.Vars.ui;

public class SatelliteDialog extends BaseDialog {
    private Table all = new Table();
    public SatelliteDialog() {
        super("@stelitedialog");

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
        all.margin(20).marginTop(0f);
        cont.pane(all).scrollX(false);
    }

    public void build() {


        cont.pane(p -> {
            p.align(Align.bottomRight);

            int i = SVars.celestialBodyController.cbs.size;
            int m = (int) Core.graphics.getWidth() / 25 - 25;
            int j = i/m;
            for(int y = 1; y <= m; y++) {
                Table ta = new Table();
                for(int x = 0; x <= j-1;x++) {
                    ta.add(SVars.celestialBodyController.cbs.get(x).button);
                }
                p.add(ta);
            }

        });


    }
    public void destroy() {
        cont.clear();
    }
}
