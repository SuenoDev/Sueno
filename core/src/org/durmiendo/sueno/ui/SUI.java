package org.durmiendo.sueno.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.ui.Bar;
import org.durmiendo.sueno.core.SVars;

public class SUI extends UI {
    /*public CBDialog satellite;
    public SPlanetDialog planet;*/ // satellites removed, right?

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        /*satellite = new CBDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog();*/ // right?

        Vars.ui.hudGroup.fill(table -> {
            table.label(() -> Strings.format("Temperature update: @ms", SVars.temperatureController$.time)).grow().left();
            table.row();
            table.label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                return Strings.format("Temperature at: @",
                        SVars.temperatureController$.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)));
            }).grow().left();
            table.row();

            table.left();
            table.setWidth(300);
        });
    }
}
