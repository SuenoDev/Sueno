package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.scene.Element;
import arc.scene.style.BaseDrawable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.BorderImage;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.DynamicTexture;
import org.durmiendo.sueno.ui.scene.BufferRegionDrawable;
import org.durmiendo.sueno.utils.SLog;

import java.awt.*;

public class DynTexList extends BaseDialog {
    public DynTexList() {
        super("DynTexList");
        
        titleTable.clear();
        titleTable.table(t -> {
            t.table(l -> {
                l.add(new BorderImage(){{
                    setDrawable(Icon.settingsSmall);
                    border(Pal.gray);
                    thickness = 0f;
                }}).size(32f);
                l.label(() -> "DynTexList").padLeft(12f).growX().center();
            }).right().growX().padLeft(6f);
            t.table(b -> {
                t.button("@back", Styles.flatt, this::hide)
                        .size(120f, 32f).right().padRight(4f).padLeft(4f);
            }).right();
        }).growX();
        
        titleTable.row();
        titleTable.image(Tex.whiteui, Pal.gray).growX().height(3f).padTop(4f);
        
        hidden(this::destroy);
        shown(this::build);
        resized(this::build);
        
        addCloseListener();
    }
    
    private void destroy() {
    }
    
    private void build() {
        cont.clear();
        
        cont.pane(p -> {
            float l = 0;
            for (DynamicTexture d : DynamicTexture.all) {
                Button b = create(d);
                if (l > Core.graphics.getWidth()-200f) {
                    l = 0f;
                    p.row();
                }
                p.add(b).growX().pad(0);
                l += b.getPrefWidth();
            }
        }).grow().scrollX(false).margin(18f);
    }
    
    private Button create(DynamicTexture dynTex) {
        Button button = new Button();
        float hy = dynTex.height / 190f;
        
        button.table(t -> {
            t.image(
                    new TextureRegionDrawable(dynTex.getTextureRegion())
            ).size(190f, dynTex.width/hy);
        });
        button.row();
        button.table(t -> {
           t.label(() -> dynTex.width+"x"+dynTex.height);
        });
        button.update(() -> {
            if (button.hasMouse()) dynTex.forceDraw();
        });
        
        button.setStyle(Styles.cleari);
        
        button.clicked(() -> {
            SVars.ui.dynTexDebugger.dynTex = dynTex;
            SVars.ui.dynTexDebugger.show();
        });
        
        return button;
    }
}
