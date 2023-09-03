package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.scene.style.Style;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.FullTextDialog;
import org.durmiendo.sueno.core.SVars;

public class CBDialog extends BaseDialog {
    private Table all = new Table();
    public ScrollPane sp;
    public CBDialog() {
        super("@cbdialog");

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
//        all.margin(20).marginTop(0f);
//        cont.pane(all).scrollX(false);
    }

    public void build() {


        sp = cont.pane(p -> {
            //p.align(Align.bottomRight);


            int i = SVars.celestialBodyController.cbs.size;
            int m = (int) (Core.graphics.getWidth() / 25);
            int j = i/m*3;
            Seq<Table> ta = new Seq<>();
            for(int y = 0; y <= m; y++) {
                ta.add(new Table());
                Table v = ta.get(y);
                for(int x = 0; x <= j-1;x++) {
                    if(x+y*(j-1) >= i) continue;
                    v.add(SVars.celestialBodyController.cbs.get(x+y*(j-1)).button);
                    Log.info(y+x);
                }

                p.add(v);
                p.row();

            }

        }).scrollX(false).get();


    }
    public void destroy() {
        cont.clear();
    }

   public void re() {
        destroy();
        build();
   }
}
