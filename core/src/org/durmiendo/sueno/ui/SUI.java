package org.durmiendo.sueno.ui;


import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureAtlas;
import arc.input.KeyCode;
import arc.scene.ui.Image;
import mindustry.Vars;
import mindustry.ui.Styles;
import org.durmiendo.sueno.graphics.SEffect;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.ui.dialogs.CBDialog;
import org.durmiendo.sueno.ui.dialogs.SPausedDialog;
import org.durmiendo.sueno.ui.dialogs.SPlanetDialog;
import org.durmiendo.sueno.ui.dialogs.ShadersEditor;
import org.durmiendo.sueno.ui.fragments.GodModeFragment;
import org.durmiendo.sueno.ui.scene.BufferRegionDrawable;

public class SUI {
    public CBDialog cbs;
    public CBDialog satellite;
    public ShadersEditor shadersEditor;
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

        shadersEditor = new ShadersEditor();

        Vars.ui.hudGroup.fill(t -> {
            t.right();
            t.add(new Image(VoidStriderCollapseEffectController.uiDrawable
                    = new BufferRegionDrawable(VoidStriderCollapseEffectController.effectsBuffer))).visible(() -> Core.input.keyDown(KeyCode.semicolon));
            t.row();
            SEffect effect = new SEffect(360, e -> {
                Draw.color(Color.white);
                float scale = (1f - e.life / e.effect.lifeTime);
                TextureAtlas.AtlasRegion region = Core.atlas.find("sueno-void-strider-collapse-effect");
                Draw.rect(region, e.x, e.y, region.width * scale, region.height * scale);
            });
            t.button("create effect", Styles.cleart, () -> {
                VoidStriderCollapseEffectController.at(Vars.player.x, Vars.player.y, effect);
            }).width(200);
            t.row();
            t.button("shaders editor", Styles.cleart, () -> {
                shadersEditor.show();
            }).width(200);
            t.row();
        });
    }
}
