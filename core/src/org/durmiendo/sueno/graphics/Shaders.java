package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;

public class Shaders {
    public static Shader ice;

    public static void init(){
        ice = new IceShader();
    }



    public static class IceShader extends Shader {
        Texture texture;
        public IceShader() {
            super(Fi.get("C:\\Users\\Durmiendo\\Desktop\\prj\\Sueno\\core\\res\\shaders\\ice.vert").readString(), Fi.get("C:\\Users\\Durmiendo\\Desktop\\prj\\Sueno\\core\\res\\shaders\\ice.frag").readString());

            Core.assets.load("sprites/space.png", Texture.class).loaded = (t) -> {
                this.texture = t;
                this.texture.setFilter(Texture.TextureFilter.linear);
                this.texture.setWrap(Texture.TextureWrap.mirroredRepeat);
            };
        }

        @Override
        public void apply() {
            texture.bind(1);
//            float screenWidth = Core.graphics.getWidth();
//            float screenHeight = Core.graphics.getHeight();
//            ice.setUniformMatrix("u_projTrans", Core.camera.mat);
//            ice.setUniformf("u_texture", 0);
//            ice.setUniformf("u_campos", Core.camera.position);
//            ice.setUniformf("u_resolution", screenWidth, screenHeight);
//            ice.setUniformf("u_time", Time.time);
        }
    }
}
