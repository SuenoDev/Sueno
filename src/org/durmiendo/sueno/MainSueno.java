package org.durmiendo.sueno;


import mindustry.mod.*;
import org.durmiendo.sueno.content.SBlock;
import org.durmiendo.sueno.content.SPlanets;


public class MainSueno extends Mod {
    public MainSueno(){


        /*Events.on(ClientLoadEvent.class, e -> Time.runTask(15f, () -> {
            BaseDialog dialog = new BaseDialog("Welcome to the Sueno 0.1.1");
            dialog.cont.button("Close", dialog::hide).size(100f, 50f);
            dialog.show();
        }));*/


    }

    @Override
    public void loadContent(){

        SPlanets.load();
        SBlock.load();

    }
}
