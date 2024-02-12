package org.durmiendo.sueno.world.blocks.environment;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import org.durmiendo.sueno.graphics.Shaders;

public class Ice extends Floor {
    private Shader iceShader;
    private float timer;

    public Ice() {
        super("icefloor");
        iceShader = Shaders.ice;
    }

    public Ice(String name) {
        super(name);
        iceShader = Shaders.ice;
    }


    public void drawOverlay(Tile t) {
        super.drawOverlay(t);

        Draw.shader(iceShader);
        iceShader.apply();
    }
}
