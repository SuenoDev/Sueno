package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.Unit;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;

import java.util.concurrent.atomic.AtomicReference;

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
        check("T stop", SVars.temperatureController$.stop, b -> {
            SVars.temperatureController$.stop = !SVars.temperatureController$.stop;
        }).left();
        row();

        table(t -> {
            label(() -> Strings.format("T update: @ms", SVars.temperatureController$.stop ? 0 : SVars.temperatureController$.time)).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                return Strings.format("T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.temperatureController$.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))-SVars.def)/SVars.maxSafeTemperature),
                        Strings.fixed(SVars.temperatureController$.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)), 2));

            }).left();
            row();
            label(() -> {
                if (SVars.temperatureController$.unitsAmount < 1) return "T of you at: -";
                if (Vars.player.dead()) return "T of you at: -";
                return Strings.format("you T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, ((SVars.temperatureController$.temperatureAt(Vars.player.unit())-SVars.def)/SVars.maxSafeTemperature)),
                        Strings.fixed(SVars.temperatureController$.temperatureAt(Vars.player.unit()), 2));

            }).left();
        });
        row();


    }
}
