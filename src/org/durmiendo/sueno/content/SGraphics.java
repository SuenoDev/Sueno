package org.durmiendo.sueno.content;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.graphics.Layer;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.DynamicTexture;

public class SGraphics {
    public static DynamicTexture sun;
    
    public static void load() {
        sun = DynamicTexture.create(
                100, 100, "shaders/sun.frag",
                shader -> {
                    shader.setUniformf("u_resolution", sun.realWidth, sun.realHeight);
                    shader.setUniformf("u_time", Time.time/100f);
                }
        );
    }
}
