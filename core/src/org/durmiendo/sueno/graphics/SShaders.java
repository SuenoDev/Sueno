package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;
import org.durmiendo.sueno.core.SCore;
import org.durmiendo.sueno.core.SVars;

public class SShaders {
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();
    public static IceShader ice = new IceShader();

    public static class VoidStriderCollapseEffectShader extends Shader {
        public VoidStriderCollapseEffectShader() {
            super(
                    SVars.internalFileTree.child("shaders/void-strider-collapse-effect.vert"),
                    SVars.internalFileTree.child("shaders/void-strider-collapse-effect.frag")
            );
        }

        @Override
        public void apply() {
            super.apply();
            setUniformi("u_screen", 1);
        }
    }

    public static class IceShader extends Shader {
        Texture texture;
        public IceShader() {
            super(
                    SVars.internalFileTree.child("shaders/ice.vert").readString(),
                    SVars.internalFileTree.child("shaders/ice.frag").readString()
            );


            this.texture = SVars.core.getRegion("t1").texture;
            this.texture.setFilter(Texture.TextureFilter.linear);
            this.texture.setWrap(Texture.TextureWrap.mirroredRepeat);
        }

        @Override
        public void apply() {
            super.apply();
            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_ccampos", Core.camera.position);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);

            texture.bind(1);
        }
    }
}
