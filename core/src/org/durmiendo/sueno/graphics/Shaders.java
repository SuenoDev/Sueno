package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;

public class Shaders {
    public static Shader ice;

    public static void init(){
        ice = new IceShader();
    }



    public static class IceShader extends Shader {
        public IceShader() {
            super(Core.files.internal("shaders/ice.vert").readString(), Core.files.internal("shaders/ice.frag").readString());
            
        }

        @Override
        public void apply() {
            float screenWidth = Core.graphics.getWidth();
            float screenHeight = Core.graphics.getHeight();
            ice.setUniformMatrix("u_projTrans", Core.camera.mat);
            ice.setUniformf("u_texture", 0);
            ice.setUniformf("u_campos", Core.camera.position);
            ice.setUniformf("u_resolution", screenWidth, screenHeight);
            ice.setUniformf("u_time", Time.time);
        }
    }
}
