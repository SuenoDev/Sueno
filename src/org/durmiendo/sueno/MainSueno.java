package org.durmiendo.sueno;

import arc.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.ui.dialogs.*;
import mma.MMAMod;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SPlanets;
//import org.durmiendo.sueno.mainContents.SuenoUI;

public class MainSueno extends MMAMod {


    public MainSueno(){


        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;
        });


        Events.on(ClientLoadEvent.class, e -> Time.runTask(15f, () -> {
            BaseDialog dialog = new BaseDialog("Welcome to the Sueno" + SVars.version);
            dialog.cont.button("Close", dialog::hide).size(180f, 80f);
            dialog.show();
        }));



    }

    @Override
    public void loadContent(){

        //new SuenoUI().loadAsync();
        SPlanets.load();
        SBlocks.load();
    }
}
