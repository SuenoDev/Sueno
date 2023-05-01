package org.durmiendo.sueno;

import arc.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.ui.dialogs.*;
import mma.MMAMod;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.content.ui.SPlacementFragment;
import org.durmiendo.sueno.mainContents.SuenoUI;

public class MainSueno extends MMAMod {
    public SuenoUI sui;
    public MainSueno(){


        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;
        });


        Events.on(ClientLoadEvent.class, e -> Time.runTask(15f, () -> {
            BaseDialog dialog = new BaseDialog("Welcome to the Sueno 0.1.1");
            dialog.cont.button("Close", dialog::hide).size(100f, 50f);
            dialog.show();
        }));



    }

    @Override
    public void loadContent(){

        sui = new SuenoUI();
        sui.loadAsync();
        SPlanets.load();
        SBlocks.load();
        Log.info("Loading some Sueno content.");
    }
}
