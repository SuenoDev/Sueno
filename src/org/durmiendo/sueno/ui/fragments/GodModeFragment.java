package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.ui.Fonts;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.Colorated;

public class GodModeFragment extends Table {
    public boolean show = false;
    public boolean tmp = show;
    public Slider slider;
    private boolean working = true;
    public GodModeFragment() {
        super();
        visible(() -> {
            if (Core.input.keyTap(KeyCode.slash)) {
                show = !show;
                if (touchable == Touchable.disabled) touchable(() -> Touchable.enabled);
                else touchable(() -> Touchable.enabled);
            }
            if (show != tmp) {
                build();
                tmp = show;
            }
            return show;
        });
    }

    public void build() {
        clear();
        background(Core.atlas.drawable("sueno-black75"));
        label(() -> "[gold]The God mode").minWidth(300).center();
        row();

        if (SVars.temperatureController == null) {
            label(() -> "[red]T controller is null!").minWidth(300).center();
            working = false;
            return;
        }

        if (!SPlanets.hielo.equals(Vars.state.rules.planet)) {
            label(() -> "[red]Planet is not hielo!").minWidth(300).center();
            row();
            label(() -> "[gray]Temperature is only\navailable on hielo").minWidth(300).center();
            working = false;
            return;
        }
        check("T stop", SVars.temperatureController.stop, b -> {
            SVars.temperatureController.stop = !SVars.temperatureController.stop;
        }).left();
        row();
        CheckBox tfu = new CheckBox("T forced update");
        tfu.changed(() -> {
            tfu.setChecked(false);
            Log.info("T forced updated");
            SVars.temperatureController.temperatureCalculate();
        });
        add(tfu).left();
        row();

        table(t -> {
            label(() -> Strings.format("T update: @ms", SVars.temperatureController.stop ? 0 : SVars.temperatureController.time)).left();
            row();
            label(() -> {
                Vec2 pos = Core.input.mouseWorld();
                if (SVars.temperatureController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)) == 0f) return "T at:[green] " + (-SVars.temperatureController.tk);
                return Strings.format("T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, (SVars.temperatureController.at((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize))+SVars.temperatureController.tk) / (SVars.temperatureController.tk*2f)),
                        Strings.fixed(SVars.temperatureController.temperatureAt((int) (pos.x / Vars.tilesize), (int) (pos.y / Vars.tilesize)), 2));

            }).left();
            row();
            label(() -> {
                if (Vars.player.dead() || SVars.temperatureController.at(Vars.player.unit())==0f) return "T of you at:[green] " + SVars.temperatureController.normalTemp;
                return Strings.format("you T at:[#@] @",
                        Colorated.gradient(Color.cyan,Color.red, ((SVars.temperatureController.temperatureAt(Vars.player.unit())+SVars.temperatureController.tk) / (SVars.temperatureController.tk*2f))),
                        Strings.fixed(SVars.temperatureController.temperatureAt(Vars.player.unit()), 2));

            }).left();
        });
        row();
        Label lb = new Label("Кисть T: 0");
        slider = new Slider(0, 100, 1, false);
        slider.changed(() -> {
            lb.setText("Кисть T: " + slider.getValue());
        });
        add(lb).left();
        row();
        add(slider).left();
        row();
        Label lb1 = new Label("Радиус T: 0");
        slider1 = new Slider(0, 30, 0.5f, false);
        slider1.changed(() -> {
            lb1.setText("Радиус T: " + slider1.getValue());
        });
        add(lb1).left();
        row();
        add(slider1).left();
        row();
        Slider s = new Slider(-SVars.temperatureController.tk, SVars.temperatureController.tk, SVars.temperatureController.tk/100f, false);
        label(() -> "Установить T (j): " + Strings.fixed(s.getValue(),2)).left();
        CheckBox ch = new CheckBox("");
        add(ch).left();
        ch.setChecked(false);
        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyTap(KeyCode.j)) {
                setT(s.getValue(), slider1.getValue());
            }
        });
        ch.changed(() -> {
            ch.setChecked(false);
            setT(s.getValue(), slider1.getValue());
        });
        row();
        add(s).left();
        row();
    }

    public boolean f = false;

    public Slider slider1;
    public void setT(float v, float r) {
        if (slider.getValue() > 0) {
            for (int x = Mathf.ceil(Core.input.mouseWorldX() / 8f - r); x < Mathf.ceil(Core.input.mouseWorldX() / 8f + r); x += 1) {
                for (int y = Mathf.ceil(Core.input.mouseWorldY() / 8f -r); y < Mathf.ceil(Core.input.mouseWorldY() / 8f + r); y += 1) {
                    SVars.temperatureController.set(x, y, v);
                }
            }
        } else {
            if (SVars.temperatureController.check(Mathf.ceil(Core.input.mouseWorldX() / 8f), Mathf.ceil(Core.input.mouseWorldY() / 8f))) SVars.temperatureController.temperature[Mathf.ceil(Core.input.mouseWorldX() / 8f)][Mathf.ceil(Core.input.mouseWorldY() / 8f)] = v;
        }
    }

    @Override
    public void draw() {
        if (working) {
            re(slider, true);

            re(slider1, false);
        }
        super.draw();
    }

    private void re(Slider rr, boolean a) {
        if (!working) return;
        if (!a) {
            float v = rr.getValue();
            Vec2 p1;
            if (v == Mathf.floor(v)) {
                p1 = Core.camera.project(
                        Mathf.ceil(Core.input.mouseWorldX()/8f)*8f-4f,
                        Mathf.ceil(Core.input.mouseWorldY()/8f)*8f-4f);
            } else {
                p1 = Core.camera.project(
                        Mathf.round((Core.input.mouseWorldX())/8f)*8f,
                        Mathf.round((Core.input.mouseWorldY())/8f)*8f);
            }
            Drawf.dashSquare(new Color(1, 1, 1, 1), p1.x, p1.y, v*Vars.renderer.getDisplayScale()*16f);
            return;
        }
        if (rr.getValue() > 0) {
            for (int x = Mathf.ceil(Core.input.mouseWorldX()/8f-rr.getValue()); x < Mathf.ceil(Core.input.mouseWorldX()/8f+ rr.getValue()); x+=1) {
                for (int y = Mathf.ceil(Core.input.mouseWorldY()/8f-rr.getValue()); y < Mathf.ceil(Core.input.mouseWorldY()/8f+ rr.getValue()); y+=1) {
                    Vec2 p = Core.camera.project(new Vec2(x*8, y*8));
                    if (p.x/16f < 0 || p.x/16f > Vars.world.height() || p.y/16f < 0 || p.y/16f > Vars.world.height()) continue;
                    float t = SVars.temperatureController.temperatureAt(x, y);
                    String s;
                    s = Strings.fixed(t, 2);
                    Color c;
                    c = Colorated.gradient(Color.cyan,Color.red, (SVars.temperatureController.at(x, y)+SVars.temperatureController.tk)/(SVars.temperatureController.tk*2));

                    Fonts.def.draw(s, p.x, p.y, c, Vars.renderer.getDisplayScale()*0.1f, false, Align.center);
                }
            }
        }
    }
}