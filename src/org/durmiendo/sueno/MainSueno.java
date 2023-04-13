package org.durmiendo.sueno;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import org.durmiendo.sueno.content.SBlock;
import org.durmiendo.sueno.content.SPlanets;

public class MainSueno extends Mod {

    public MainSueno(){

        Events.on(ClientLoadEvent.class, e -> {
            Time.runTask(15f, () -> {
                BaseDialog dialog = new BaseDialog("Welcome to the Sueno");
                dialog.buttons.add("Close");
                dialog.show();
            });
        });
    }

    @Override
    public void loadContent(){
        SPlanets.load();
        SBlock.load();
        Log.info("Loading some Sueno content.");
    }
}
