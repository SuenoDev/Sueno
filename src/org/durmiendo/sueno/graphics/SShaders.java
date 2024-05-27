package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Gl;
import arc.graphics.gl.Shader;
import arc.scene.ui.layout.Scl;
import arc.util.Time;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;


public class SShaders {

    /**
     * shader in layer = 112 (SLayers.voidspace)
     */
    public static VoidSpaceShader voidSpaceShader = new VoidSpaceShader();
    public static DeadShader deadShader = new DeadShader();
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();

    public static class VoidSpaceShader extends Shader{
        public VoidSpaceShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/void-space.frag")
            );
        }

        @Override
        public void apply(){
            setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);
            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_ccampos", Core.camera.position);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);
        }
    }

    public static class DeadShader extends Shader{
        public DeadShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/dead.frag")
            );
        }

        @Override
        public void apply(){
            setUniformf("u_dp", Scl.scl(1f));
            setUniformf("u_time", Time.time / Scl.scl(1f));
            setUniformf("u_offset",
                    Core.camera.position.x - Core.camera.width / 2,
                    Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_invsize", 1f/Core.camera.width, 1f/Core.camera.height);
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
