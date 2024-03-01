package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.Colorated;
import org.durmiendo.sueno.temperature.TemperatureController;

public class GodModeFragment extends Table {
    public boolean show = false;
    public Slider slider;
    public GodModeFragment() {
        super();
        background(Core.atlas.drawable("sueno-black75"));
        label(() -> "       [gold]The God mode       ");
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
        check("T stop", SVars.TemperatureСontroller.stop, b -> {
            SVars.TemperatureСontroller.stop = !SVars.TemperatureСontroller.stop;
        }).left();
        row();

        table(t -> {
            label(() -> Strings.format("T update: @ms", SVars.TemperatureСontroller.stop ? 0 : SVars.TemperatureСontroller.time)).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                if (SVars.TemperatureСontroller.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)) == 0f) return "T at:[green] " + SVars.TemperatureСontroller.normalTemp;
                return Strings.format("T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.TemperatureСontroller.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))- TemperatureController.def)/ TemperatureController.maxSafeTemperature),
                        Strings.fixed(SVars.TemperatureСontroller.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)), 2));

            }).left();
            row();
            label(() -> {
                if (Vars.player.dead() || SVars.TemperatureСontroller.at(Vars.player.unit())==0f) return "T of you at:[green] " + SVars.TemperatureСontroller.normalTemp;
                return Strings.format("you T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, ((SVars.TemperatureСontroller.temperatureAt(Vars.player.unit())- TemperatureController.def)/ TemperatureController.maxSafeTemperature)),
                        Strings.fixed(SVars.TemperatureСontroller.temperatureAt(Vars.player.unit()), 2));

            }).left();
        });
        row();
        Label lb = new Label("Кисть T: 0");
        TextButton tb = new TextButton("Режим кисти T", Styles.flatTogglet);
        slider = new Slider(0, 100, 1, false);
        slider.changed(() -> {
            lb.setText("Кисть T: " + slider.getValue());
        });
        add(lb).left();
        row();
        add(slider).left();

    }

    @Override
    public void draw() {
        if (slider.getValue() > 0) {
            for (int x = Mathf.ceil(Core.input.mouseWorldX()/8f-slider.getValue()); x < Mathf.ceil(Core.input.mouseWorldX()/8f+slider.getValue()); x+=1) {
                for (int y = Mathf.ceil(Core.input.mouseWorldY()/8f-slider.getValue()); y < Mathf.ceil(Core.input.mouseWorldY()/8f+slider.getValue()); y+=1) {
                    Vec2 p = Core.camera.project(new Vec2(x*8, y*8));
                    if (p.x/16f < 0 || p.x/16f > Vars.world.height() || p.y/16f < 0 || p.y/16f > Vars.world.height()) continue;
                    float t = SVars.TemperatureСontroller.temperatureAt(x, y);
                    String s;
                    s = Strings.fixed(t, 2);
                    Color c;
                    c = Colorated.gradient(Color.cyan,Color.red, (SVars.TemperatureСontroller.at(x, y)- TemperatureController.def)/ TemperatureController.maxSafeTemperature);

                    Fonts.def.draw(s, p.x, p.y, c, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                }
            }
        }
        super.draw();
    }
}