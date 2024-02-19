package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.Events;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import org.durmiendo.sueno.core.SVars;
import mindustry.content.Planets;
import mindustry.game.EventType;
import mindustry.type.Planet;


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

            Log.info("shader");


            this.texture = SVars.core.getRegion("t1").texture;
            this.texture.setFilter(Texture.TextureFilter.linear);
            this.texture.setWrap(Texture.TextureWrap.mirroredRepeat);
        } 
        @Override
        public void apply() {
            super.apply();
            setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_zoom",  Vars.renderer.getDisplayScale());
            setUniformf("u_time", Time.time);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            texture.bind(1);
        }
    }
}
