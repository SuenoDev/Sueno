package org.durmiendo.sueno.temperature;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import org.durmiendo.sap.SuenoSettings;
import org.durmiendo.sueno.core.SVars;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static arc.graphics.GL20.*;

public class TemperatureRenderer {
    private Texture thermalTexture;
    private Shader thermalShader;
    private int textureHandle = -1;
    
    public TemperatureRenderer() {
        thermalShader = new Shader(
            Core.files.internal("shaders/screenspace.vert"), // Стандартный вертексный шейдер Mindustry
            Core.files.internal("shaders/thermal.frag")
        );
        
        Vars.ui.hudGroup.fill(t -> {
            t.visible(() -> SVars.temperatureController.showHeatMap); // Твоя настройка
            t.draw();
        });
    }

    private void ensureTexture(int w, int h) {
        if (thermalTexture == null || thermalTexture.width != w || thermalTexture.height != h) {
            if (thermalTexture != null) thermalTexture.dispose();

            thermalTexture = new Texture(w, h);
            thermalTexture.setFilter(Texture.TextureFilter.nearest);
            textureHandle = thermalTexture.getTextureObjectHandle();
        }
    }

    
    TextureRegion tmp = new TextureRegion();
    public void draw() {
        if (!NativeTemperature.isLoaded || NativeTemperature.dataBuffer == null) return;
        
        int w = Vars.world.width();
        int h = Vars.world.height();
        
        ensureTexture(w, h);

   
        NativeTemperature.dataBuffer.position(0);
        
        Core.gl.glBindTexture(GL_TEXTURE_2D, textureHandle);
    

        
        Draw.shader(thermalShader);
        thermalShader.setUniformi("u_texture", 0);
        
        float worldW = w * Vars.tilesize;
        float worldH = h * Vars.tilesize;
        
        tmp.set(thermalTexture);
        Draw.rect(tmp, worldW / 2f, worldH / 2f, worldW, worldH);
        
        Draw.shader();
    }
}