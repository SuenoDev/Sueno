package org.durmiendo.sueno.world.blocks.environment;

import mindustry.world.blocks.environment.Floor;

public class Ice extends Floor {


    public Ice(String name) {
        super(name);

//        cacheLayer = new CacheLayer.ShaderLayer(SShaders.iceShader);
        albedo = 0.9f;
        supportsOverlay = true;
//        CacheLayer.addLast(cacheLayer);
    }

}
