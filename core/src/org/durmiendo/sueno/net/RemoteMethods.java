package org.durmiendo.sueno.net;

import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.entities.Units;
import mindustry.world.Tile;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;

public class RemoteMethods {
    @Annotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.both)
    public static void voidStriderCollapse(VoidStriderc voidStriderc) {
        float x = voidStriderc.x(), y = voidStriderc.y(), range = voidStriderc.range();
        VoidStriderCollapseEffectController.at(x, y, 600f, range);

        Vars.indexer.allBuildings(x, y, range, building -> {
            Tile.buildDestroyed(building);
        });
        Units.nearby(x - range, y - range, range * 2f, range * 2f, u -> {
            if (u != voidStriderc && u.within(x, y, range + u.hitSize/2f)){
                u.destroy();
            }
        });
    }
}

