package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureAtlas;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.Colorated;

public class GodModeFragment extends Table {
    public boolean show = false;
    public Slider slider;
    public CheckBox cb;
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
        check("T stop", SVars.tempTemperatureController.stop, b -> {
            SVars.tempTemperatureController.stop = !SVars.tempTemperatureController.stop;
        }).left();
        row();

        table(t -> {
            label(() -> Strings.format("T update: @ms", SVars.tempTemperatureController.stop ? 0 : SVars.tempTemperatureController.time)).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                if (SVars.tempTemperatureController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)) == 0f) return "T at:[green] " + SVars.tempTemperatureController.normalTemp;
                return Strings.format("T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.tempTemperatureController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))-SVars.def)/SVars.maxSafeTemperature),
                        Strings.fixed(SVars.tempTemperatureController.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)), 2));

            }).left();
            row();
            label(() -> {
                if (Vars.player.dead() || SVars.tempTemperatureController.at(Vars.player.unit())==0f) return "T of you at:[green] " + SVars.tempTemperatureController.normalTemp;
                return Strings.format("you T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, ((SVars.tempTemperatureController.temperatureAt(Vars.player.unit())-SVars.def)/SVars.maxSafeTemperature)),
                        Strings.fixed(SVars.tempTemperatureController.temperatureAt(Vars.player.unit()), 2));

            }).left();
        });
        row();
        Label lb = new Label("Кисть T: 0");
        TextButton tb = new TextButton("Режим кисти T", Styles.flatTogglet);
        slider = new Slider(0, 20, 1, false);
        slider.changed(() -> {
            lb.setText("Кисть T: " + slider.getValue());
        });
        cb = new CheckBox("показ \"Цифра\"");
        cb.changed(() -> {
            if (cb.isChecked()) {
                cb.setText("показ \"Плитка\"");
            } else {
                cb.setText("показ \"Цифра\"");
            }
        });
        add(lb).left();
        row();
        add(slider).left();
        row();
        add(cb).left();

    }

    @Override
    public void draw() {
        super.draw();
        if (slider.getValue() == 0) return;
        for (int x = Mathf.ceil(Core.input.mouseWorldX()/8f-slider.getValue()); x < Mathf.ceil(Core.input.mouseWorldX()/8f+slider.getValue()); x+=1) {
            for (int y = Mathf.ceil(Core.input.mouseWorldY()/8f-slider.getValue()); y < Mathf.ceil(Core.input.mouseWorldY()/8f+slider.getValue()); y+=1) {
                Vec2 p = Core.camera.project(new Vec2(x*8, y*8));
                if (p.x/16f < 0 || p.x/16f > Vars.world.height() || p.y/16f < 0 || p.y/16f > Vars.world.height()) continue;
                if (!cb.isChecked()) {
                    Fonts.def.draw(Strings.fixed(SVars.tempTemperatureController.temperatureAt(x, y), 2), p.x, p.y,
                            Colorated.gradient(Color.cyan,Color.red, (SVars.tempTemperatureController.at(x, y)-SVars.def)/SVars.maxSafeTemperature),
                            Vars.renderer.getScale()*0.1f, false, Align.center
                    );
                } else {
                    TextureAtlas.AtlasRegion t = Core.atlas.find("sueno-white-cub2-50");
                    t.scale=Vars.renderer.getScale()*2f;
                    Drawf.additive(t, Colorated.gradient(Color.cyan, Color.red, (SVars.tempTemperatureController.temperatureAt(x,y))/300f), p.x-2, p.y-2);
                    t.scale=1f;
                }
            }
        }
    }
}
