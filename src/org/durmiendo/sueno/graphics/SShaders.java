package org.durmiendo.sueno.graphics;

import arc.graphics.Gl;
import arc.graphics.gl.Shader;
import org.durmiendo.sueno.core.SVars;


public class SShaders {
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();

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
