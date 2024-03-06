package org.durmiendo.sueno.net;

import arc.util.Log;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.content.Blocks;
import mindustry.entities.Units;
import mindustry.world.Tile;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.world.units.types.VoidStriderUnitType;

public class RemoteMethods {
    @Annotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.both)
    public static void voidStriderCollapse(VoidStriderc voidStriderc) {
        float x = voidStriderc.x();
        float y = voidStriderc.y();
        float radius = 0;

        if (voidStriderc.type() instanceof VoidStriderUnitType voidStriderUnitType) {
            radius = voidStriderUnitType.collapseRadius;
            VoidStriderCollapseEffectController.at(x, y, voidStriderUnitType.collapseEffect);
        } else
            Log.err("Type of @ must be VoidStriderUnitType (not @) for correct collapsing effects.\nCollapses with this unitType will be incorrect.",
                    voidStriderc, voidStriderc.type());

        Vars.indexer.allBuildings(x, y, radius, tile -> {
            tile.tile().setBlock(Blocks.air);
        });
        final float finalRange = radius;
        Units.nearby(x - radius, y - radius, radius * 2f, radius * 2f, u -> {
            if (u != voidStriderc && u.within(x, y, finalRange + u.hitSize/2f))
                u.destroy();
        });
    }
}

