package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Gl;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;


public class SShaders {

    /**
     * shader in layer = Layer.groundUnit + 0.12f;
     */
    public static VoidSpaceShader voidSpaceShader = new VoidSpaceShader();
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();

    public static class VoidSpaceShader extends Shaders.SurfaceShader {
        Texture texture;
        public VoidSpaceShader() {
            super(
                    SVars.internalFileTree.child("shaders/void-space.vert").readString(),
                    SVars.internalFileTree.child("shaders/void-space.frag").readString()
            );

            Core.assets.load("sprites/space.png", Texture.class).loaded = t -> {
                texture = t;
                texture.setFilter(Texture.TextureFilter.linear);
                texture.setWrap(Texture.TextureWrap.mirroredRepeat);
            };
        }
        @Override
        public void apply() {
            super.apply();
            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_ccampos", Core.camera.position);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);

            if (texture != null) {
                texture.bind(1);
                setUniformi("u_stars", 1);
            }
        }
    }

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
            Gl.activeTexture(Gl.texture0);
            VoidStriderCollapseEffectController.effectsBuffer.getTexture().bind();
            Gl.activeTexture(Gl.texture1);
            VoidStriderCollapseEffectController.screenBuffer.getTexture().bind();

            SShaders.voidStriderCollapseEffectShader.setUniformi("u_effects", 0);
            SShaders.voidStriderCollapseEffectShader.setUniformi("u_screen", 1);

            Gl.activeTexture(Gl.texture0);
        }
    }
}
