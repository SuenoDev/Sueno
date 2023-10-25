package org.durmiendo.sueno.ui;

import arc.Core;
import arc.Events;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.actions.TouchableAction;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.ui.Bar;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;

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
        Vars.ui.hudGroup.parent.find("overlaymarker").parent.find("fps/ping").parent.fill(table -> {
            table.touchable(() -> Touchable.disabled);
            table.label(() -> Strings.format("Temperature update: @ms", SVars.temperatureController$.time)).left();
            table.row();
            table.label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                return Strings.format("Temperature at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.temperatureController$.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))+100)/200),
                        SVars.temperatureController$.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)));
            }).left();
            table.row();

            table.left();

        });
    }
}
