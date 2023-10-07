package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.sueno.core.SVars;

public class CBDialog extends BaseDialog {
    public Seq t = new Seq<Table>();
    public CBDialog() {
        super("@cbdialog");

        addCloseButton();

        hidden(this::destroy);
        shown(this::build);
//        all.margin(20).marginTop(0f);
//        cont.pane(all).scrollX(false);
    }

    public void build() {


        cont.pane(p -> {
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
            t = ta;

        }).scrollX(false);


    }
    public void destroy() {
        cont.clear();
    }
}
