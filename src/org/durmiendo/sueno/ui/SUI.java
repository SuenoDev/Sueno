package org.durmiendo.sueno.ui;


import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureAtlas;
import arc.input.KeyCode;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.ui.Image;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.ui.Styles;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.ui.dialogs.CBDialog;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.fragments.GodModeFragment;
import org.durmiendo.sueno.ui.scene.BufferRegionDrawable;

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





        Vars.ui.hudGroup.fill(t -> {
            t.left();
            t.add(new GodModeFragment());
        });

        voidStriderCollapseEffectDebugUI: {
            if (!enableVoidStriderCollapseEffectDebugUI)
                break voidStriderCollapseEffectDebugUI;
            Vars.ui.hudGroup.fill(t -> {
                t.right();
                t.add(new Image(VoidStriderCollapseEffectController.uiDrawable
                        = new BufferRegionDrawable(VoidStriderCollapseEffectController.effectsBuffer))).visible(() -> Core.input.keyDown(KeyCode.semicolon));
                t.row();
                SEffect effect = new SEffect(30, e -> {
                    Draw.color(Color.white);
                    float fin = e.life / e.effect.lifeTime;
                    float scale = fin < 0.8f ? 1f - fin : fin / 4f;

                    TextureAtlas.AtlasRegion region = Core.atlas.find("sueno-void-strider-collapse-effect");
                    Draw.rect(region, e.x, e.y, region.width * scale, region.height * scale);
                });
                Effect bulbs = new Effect(30f, e -> {
                    Color c = new Color(0.05f, 0.05f, 0.3f);

                    Angles.randLenVectors(e.id, e.fin(), 40, 80, (x, y, i, o) -> {
                        Color cc = c.cpy();
                        cc.b *= (Mathf.angle(x, y) + o) % 1;
                        Draw.color(cc);
                        Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), i * 64f);
                    });
                });
                t.button("create effect", Styles.cleart, () -> {
                    bulbs.at(Vars.player.x, Vars.player.y);
                    VoidStriderCollapseEffectController.at(Vars.player.x, Vars.player.y, effect);
                }).width(200);
            });
        }


    }
}
