package org.durmiendo.sueno.core;

import arc.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.content.SItems;
import org.durmiendo.sueno.content.SPlanets;

// TODO remove old temperature and write new (on controllers)

public class SCore extends Mod {
    public SCore(){
        // TODO that's bad
        Events.on(ClientLoadEvent.class, e -> {
            Vars.renderer.planets.projector.setScaling(1f / 1500f);
            Vars.renderer.planets.cam.fov = 60f;
            Vars.renderer.planets.cam.far = 1500f;
        });
    }

    @Override
    public void loadContent(){
        SItems.load();
        SBlocks.load();
        SPlanets.load();
    }
}
