package org.durmiendo.sueno.ui;


import arc.Core;
import arc.Events;
import arc.freetype.FreeTypeFontGenerator;
import arc.graphics.Texture;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.PixmapPacker;
import arc.math.Mathf;
import arc.scene.style.BaseDrawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.ui.dialogs.*;
import org.durmiendo.sueno.ui.fragments.GodModeFragment;

public class SUI {
    public static final boolean enableVoidStriderCollapseEffectDebugUI = true;
    
    public Font jetBrainsMonoFont;
    public TextField.TextFieldStyle baseEditorStyle;
    
    public SPlanetDialog planet; // satellites removed, right?
    
    public DynTexDebugger dynTexDebugger;
    public DynTexList dynTexList;
    public DynTexVisible dynTexVisible;

    public SUI() {

    }
    
    /** On mod init **/
    public void build() {
        initFonts();
        
        baseEditorStyle = new TextField.TextFieldStyle(Styles.nodeArea){{
            background = new BaseDrawable(Tex.clear){
                @Override
                public float getLeftWidth() {
                    return 35f;
                }
            };
            font = jetBrainsMonoFont;
        }};
        
        planet = new SPlanetDialog();
        Vars.ui.planet = planet;
        Vars.ui.paused = new SPausedDialog(); // right?

        GodModeFragment g = new GodModeFragment();
        g.create();
        
        dynTexDebugger = new DynTexDebugger();
        dynTexList = new DynTexList();
        dynTexVisible = new DynTexVisible();
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
            this.timer = timer;
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
    
    static Font load(String path) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(SVars.internalFileTree.child(path));
        PixmapPacker p = new PixmapPacker(8096, 8096,2, false);
        Font f = gen.generateFont(new FreeTypeFontGenerator.FreeTypeFontParameter(){{
            packer = p;
            size = 14;
        }});
        p.updateTextureAtlas(Core.atlas, Texture.TextureFilter.linear, Texture.TextureFilter.linear, false);
        p.dispose();
        return f;
    }
    
    public void initFonts() {
        jetBrainsMonoFont = load("fonts/JetBrainsMono-Regular.ttf");
    }
}
