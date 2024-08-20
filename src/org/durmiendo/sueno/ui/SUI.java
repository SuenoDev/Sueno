package org.durmiendo.sueno.ui;


import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.ui.dialogs.CBDialog;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.elements.Switch;
import org.durmiendo.sueno.ui.fragments.GodModeFragment;

public class SUI {
    public static final boolean enableVoidStriderCollapseEffectDebugUI = true;

    public CBDialog cbs;
    public CBDialog satellite;
    public SPlanetDialog planet; // satellites removed, right?

    public SUI() {

    }

    /** On mod init **/
    public void build() {
        cbs = satellite = new CBDialog();
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog(); // right?

        GodModeFragment g = new GodModeFragment();
        g.build();

        Vars.ui.hudGroup.fill(t -> {
            t.left();
            t.table(f -> {
                f.background(Core.atlas.drawable("sueno-black75"));
                ImageButton a = new ImageButton(Icon.move, Styles.clearNonei);
                a.dragged((x, y) -> {
                    t.x += Core.input.deltaX();
                    t.y += Core.input.deltaY();
                });
                f.add(a).left();
                f.button(Icon.upOpen, Styles.cleari, () -> {
                    g.shower(true);
                }).left();
                Switch s = new Switch(Icon.pause);
                s.click(b -> {
                    TemperatureController.stop = !TemperatureController.stop;
                });
                s.left();
            }).left().minWidth(g.getWidth());
            t.row();
            t.table(f -> {
                f.add(g);
            });
        });

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            g.shower(true);
        });
    }

    public void warning(String text, float timer) {
        BaseDialog d = new WarningDialog(timer);
        d.cont.add(text).row();

        d.show();
    }

    private static class WarningDialog extends BaseDialog {
        public Cell<TextButton> button;
        public float timer;
        public WarningDialog(float timer) {
            super("@warning");
            timer = this.timer * timer;
            buttons.defaults().size(210, 64f);
            button = buttons.button(getTimer(), Icon.left, this::hide).size(210f, 64f);
            button.get().setDisabled(true);
            addCloseListener();
        }

        @Override
        public void draw() {
            super.draw();
            timer -= Time.delta;
            if (timer <= 0f) {
                button.get().setDisabled(false);
                button.get().setText(Core.bundle.get("back"));
            } else {
                button.get().setText(getTimer());
            }
        }

        public String getTimer() {
            return "[gray]" + Core.bundle.get("back") + " [white]" + Mathf.ceil(timer/60f) + "s.";
        }
    }
}
