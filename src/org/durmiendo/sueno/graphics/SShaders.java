package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Gl;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;


public class SShaders {

    /**
     * shader in layer = 112 (SLayers.voidspace)
     */
    public static VoidSpaceShader voidSpaceShader = new VoidSpaceShader();
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();

    public static class VoidSpaceShader extends Shader{
        Texture texture;
        Texture noiseTex;


        public VoidSpaceShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/void-space.frag")
            );

            loadNoise();

            Core.assets.load("sprites/spaace.png", Texture.class).loaded = t -> {
                texture = t;
                texture.setFilter(Texture.TextureFilter.linear);
                texture.setWrap(Texture.TextureWrap.mirroredRepeat);
            };
        }

        public String textureName(){
            return "noise";
        }

        public void loadNoise(){
            Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
                t.setFilter(Texture.TextureFilter.linear);
                t.setWrap(Texture.TextureWrap.repeat);
            };
        }

        @Override
        public void apply(){
            setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);

            if(hasUniform("u_noise")){
                if(noiseTex == null){
                    noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
                }

                noiseTex.bind(1);
                Vars.renderer.effectBuffer.getTexture().bind(0);

                setUniformi("u_noise", 1);
            }

            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_ccampos", Core.camera.position);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);
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
