package org.durmiendo.sueno.world.blocks.environment;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import mindustry.game.EventType;
import mindustry.graphics.CacheLayer;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import org.durmiendo.sueno.graphics.Shaders;

public class Ice extends Floor {
    private Shader iceShader;
    private CacheLayer layer;

    public Ice(String name) {
        super(name);
        iceShader = Shaders.ice;
        layer = new CacheLayer.ShaderLayer(iceShader);
        CacheLayer.add(layer);

        Events.on(EventType.Trigger.drawOver.getDeclaringClass(), d -> {
            iceShader.bind();
            layer.begin();
            iceShader.apply();
            Draw.shader(iceShader);
            layer.end();
        });
    }
}
