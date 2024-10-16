package org.durmiendo.sueno.world.blocks.environment;

import mindustry.graphics.CacheLayer;
import mindustry.world.blocks.environment.Floor;
import org.durmiendo.sueno.graphics.SShaders;

public class Ice extends Floor {


    public Ice(String name) {
        super(name);

        cacheLayer = new CacheLayer.ShaderLayer(SShaders.iceShader);
        albedo = 0.9f;
        supportsOverlay = true;

        CacheLayer.addLast(cacheLayer);
    }

}
