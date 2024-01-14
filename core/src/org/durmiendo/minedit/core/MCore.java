package org.durmiendo.minedit.core;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations;

@ModAnnotations.RootDirectoryPath(rootDirectoryPath = "core")
@ModAnnotations.AnnotationSettings(
        rootPackage = "org.durmiendo.minedit",
        modInfoPath = "res/mod.json",
        classPrefix = "M"
)
@ModAnnotations.MainClass
public class MCore extends Mod {
    public MVars m = new MVars();
    public MCore(){
        MVars.core = this;
    }

    @Override
    public void loadContent(){
        MVars.sueno = Vars.mods.getMod(getClass());
    }

    @Override
    public void init() {
        MVars.ui.build();
        Vars.ui.menufrag.addButton("Изменить", Icon.rightOpen, () -> {
            MVars.ui.choiceDialog.show();
        });
        Events.on(EventType.ClientLoadEvent.class, e -> {
            MVars.init();
        });
    }

    public TextureRegion getRegion(String nameWithoutPrefix) {
        return Core.atlas.find(MVars.sueno.name + '-' + nameWithoutPrefix);
    }
}