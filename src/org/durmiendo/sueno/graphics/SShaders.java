package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.Gl;
import arc.graphics.gl.Shader;
import arc.scene.ui.layout.Scl;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;


public class SShaders {
    public static VoidSpaceShader voidSpaceShader = new VoidSpaceShader();
    public static ContractionShader contractionShader = new ContractionShader();
    public static DeadShader deadShader = new DeadShader();
    public static IceShader iceShader = new IceShader();
    public static NormalShader normalShader = new NormalShader();
    public static BlackHoleShader blackHoleShader = new BlackHoleShader();
    public static VoidStriderCollapseEffectShader voidStriderCollapseEffectShader = new VoidStriderCollapseEffectShader();


    public static Shader g3d = new Shader(
            SVars.internalFileTree.child("shaders/3d.vert"),
            SVars.internalFileTree.child("shaders/3d.frag")
    );

    public static class IceShader extends Shader{
        public IceShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/ice.frag")
            );
        }

        @Override
        public void apply(){
            setUniformf("u_offset",
                    Core.camera.position.x - Core.camera.width / 2,
                    Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);
        }
    }

    public static class BlackHoleShader extends Shader{
        public BlackHoleShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/black-hole.frag")
            );
        }

        @Override
        public void apply(){
            super.apply();
        }
    }

    public static class VoidSpaceShader extends Shader{
        public VoidSpaceShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/void-space.frag")
            );
        }

        @Override
        public void apply(){
            setUniformf("u_offset",
                    Core.camera.position.x - Core.camera.width / 2,
                    Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);
        }
    }

    public static class ContractionShader extends Shader{
        public ContractionShader(){
            super(
                    Shaders.getShaderFi("screenspace.vert"),
                    SVars.internalFileTree.child("shaders/contraction.frag")
            );
        }


        public ObjectMap<String, Float> uniforms = new ObjectMap<>();

        public void set(String uf, float v){
            uniforms.put(uf, v);
        }

        public void apply(){
            setUniformf("u_offset",
                    Core.camera.position.x - Core.camera.width / 2,
                    Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);
            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);
            for (String uf : uniforms.keys()) {
                setUniformf(uf, uniforms.get(uf));
            }

//            setUniformf("u_per", 1f);
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

    public static class NormalShader extends Shader{
        public NormalShader(){
            super(
                    SVars.internalFileTree.child("shaders/normal.vert"),
                    SVars.internalFileTree.child("shaders/normal.frag")
            );
        }

        @Override
        public void apply(){
//            setUniformi("u_normal", 3);
//            setUniformi("u_texture", 0);
            setUniformf("u_offset",
                    Core.camera.position.x - Core.camera.width / 2,
                    Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_lightPos", Core.input.mouseWorldX(), Core.input.mouseWorldY());
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_lightColor", 0.4f, 1f, 0.65f, 1f);
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
        public void bind() {
            super.bind();
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
