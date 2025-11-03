package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.util.Scaling;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.BorderImage;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.graphics.DynamicTexture;
import org.durmiendo.sueno.utils.SLog;

public class DynTexVisible extends BaseDialog {
    
    public DynTexVisible() {
        super("DynTexVisible");
        
        titleTable.clear();
        titleTable.table(t -> {
            t.table(l -> {
                l.add(new BorderImage(){{
                    setDrawable(Icon.settingsSmall);
                    border(Pal.gray);
                    thickness = 0f;
                }}).size(32f);
                l.label(() -> "DynTexVisible").padLeft(12f).growX().center();
            }).right().growX().padLeft(6f);
            t.table(b -> t.button("@back", Styles.flatt, this::hide)
                    .size(120f, 32f).right().padRight(4f).padLeft(4f)).right();
        }).growX();
        
        titleTable.row();
        titleTable.image(Tex.whiteui, Pal.gray).growX().height(3f).pad(0f);
        
        hidden(this::destroy);
        shown(this::build);
        resized(this::build);
        
        addCloseListener();
    }
    
    private DynamicTexture dynTex;
    
    public void show(DynamicTexture dynTex) {
        this.dynTex = dynTex;
        show();
    }
    
    private void build() {
        cont.clear();
        cont.table(all -> {
            all.image(
                    dynTex.getTextureRegion()
            ).update(i -> dynTex.forceDraw()).scaling(Scaling.fit).grow();
        }).grow();
    }
    
    private void destroy() {
    
    }
}
