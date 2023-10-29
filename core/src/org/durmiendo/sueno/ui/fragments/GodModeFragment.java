package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
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
        visible(() -> {
            if (Core.input.keyTap(KeyCode.f7)) {
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
                        SVars.temperatureController$.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)));

            }).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                if(SVars.temperatureController$.unitsAmount < 1) return "T on U: -";
                AtomicReference<Unit> uu = null;
                SVars.temperatureController$.units.forEach((u, f) -> {
                    if (uu.get() == null) {
                        uu.set(u);
                        return;
                    }
                    if (uu.get().dst(pos) < u.dst(pos)) {
                        uu.set(u);
                    }
                });
                return Strings.format("T on U at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, SVars.temperatureController$.at(uu.get())/SVars.maxSafeTemperature),
                        SVars.temperatureController$.temperatureAt(uu.get())-SVars.def);

            }).left();
            row();
        });
        row();


    }
}
