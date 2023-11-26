package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.Events;
import arc.scene.ui.Dialog;
import arc.scene.ui.ImageButton;
import mindustry.ui.dialogs.PlanetDialog;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.events.SEvents;


public class SPlanetDialog extends PlanetDialog {


    @Override
    public Dialog show() {
        Events.fire(new SEvents.CampaignOpenEvent());
        return super.show();
    }

    @Override
    public void closeOnBack(Runnable callback) {
        Events.fire(new SEvents.CampaignCloseEvent());
        super.closeOnBack(callback);
    }

    @Override
    public void draw() {
        super.draw();
        SVars.celestialBodyController.draw();
    }

    public SPlanetDialog() {
        super();

        shown(() -> {
//            Table table = new Table();
//            table.setFillParent(true);
//            addChild(table);
//            Vars.ui.planet.fill(hernya1 -> {
//                hernya1.setBackground(Icon.android);
//                hernya1.pane(hernya2 -> {
//                    hernya2.table(hernya3 -> {
//                        hernya3.button(Icon.admin, () -> {
//
//                        }).size(25);
//                    }).growX();
//                }).align(Align.bottomRight).width(200).height(200).grow();
//            });
//
//            Vars.ui.planet.fill((x, y, w, h) -> {
//                Draw.color(Color.red);
//                Fill.rect(x, y, w, h);
//            });


            ib.clicked(() -> {
                SVars.cbs.show();
            });
            this.add(ib);
        });
    }

    ImageButton ib = new ImageButton(Core.atlas.find("sueno-satellite"));

}
