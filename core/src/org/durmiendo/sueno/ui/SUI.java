package org.durmiendo.sueno.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;

public class SUI extends UI {
    public SatelliteDialog satellite;
    public SPlanetDialog planet;

    public SUI() {

    }

    // TODO @nekit508: кри прикол в том, что изменения не доходят до показывания планетдиалога...up
    /** On mod init **/
    public void build() {
        satellite = new SatelliteDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog();
    }
}
