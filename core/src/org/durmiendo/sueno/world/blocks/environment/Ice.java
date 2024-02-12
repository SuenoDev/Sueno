package org.durmiendo.sueno.world.blocks.environment;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import com.google.common.cache.Cache;
import mindustry.game.EventType;
import mindustry.graphics.CacheLayer;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import org.durmiendo.sueno.graphics.SShaders;

import java.util.Arrays;

public class Ice extends Floor {
    private Shader iceShader;
    private CacheLayer layer;

    public Ice(String name) {
        super(name);
        iceShader = SShaders.ice;
        layer = new CacheLayer.ShaderLayer(iceShader);
        CacheLayer.add(0, layer);
        cacheLayer = layer;
    }
}
