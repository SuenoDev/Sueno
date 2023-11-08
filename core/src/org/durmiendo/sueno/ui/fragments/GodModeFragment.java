package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.Unit;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;

public class GodModeFragment extends Table {
    public boolean show = false;
    public GodModeFragment() {
        super();
        background(Core.atlas.drawable("sueno-black75"));
        label(() -> "     [gold]The God mode     ");
        row();
        visible(() -> {
            if (Core.input.keyTap(KeyCode.slash)) {
                show = !show;
                if (touchable == Touchable.disabled) touchable(() -> Touchable.enabled);
                else touchable(() -> Touchable.enabled);
            }
            return show;
        });

        row();
        check("T stop", SVars.tempController.stop, b -> {
            SVars.tempController.stop = !SVars.tempController.stop;
        }).left();
        row();

        table(t -> {
            label(() -> Strings.format("T update: @ms", SVars.tempController.stop ? 0 : SVars.tempController.time)).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                if (SVars.tempController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)) == 0f) return "T at:[green] " + SVars.tempController.normalTemp;
                return Strings.format("T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.tempController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))-SVars.def)/SVars.maxSafeTemperature),
                        Strings.fixed(SVars.tempController.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)), 2));

            }).left();
            row();
            label(() -> {
                if (Vars.player.dead() || SVars.tempController.at(Vars.player.unit())==0f) return "T of you at:[green] " + SVars.tempController.normalTemp;
                return Strings.format("you T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, ((SVars.tempController.temperatureAt(Vars.player.unit())-SVars.def)/SVars.maxSafeTemperature)),
                        Strings.fixed(SVars.tempController.temperatureAt(Vars.player.unit()), 2));

            }).left();
        });
        row();



    }
}
