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

        protected String preprocess(String source, boolean fragment) {
            if (source.contains("#ifdef GL_ES")) {
                throw new ArcRuntimeException("Shader contains GL_ES specific code; this should be handled by the preprocessor. Code: \n```\n" + source + "\n```");
            } else if (source.contains("#version")) {
                throw new ArcRuntimeException("Shader contains explicit version requirement; this should be handled by the preprocessor. Code: \n```\n" + source + "\n```");
            } else {
                if (fragment) {
                    source = "#ifdef GL_ES\nprecision " + (source.contains("#define HIGHP") && !source.contains("//#define HIGHP") ? "highp" : "mediump") + " float;\nprecision mediump int;\n#else\n#define lowp  \n#define mediump \n#define highp \n#endif\n" + source;
                } else {
                    source = "#ifndef GL_ES\n#define lowp  \n#define mediump \n#define highp \n#endif\n" + source;
                }
                source = "#version 400\n" + source;
                if (Core.gl30 != null) {
                    return (fragment ? "out lowp vec4 fragColor;\n" : "") + source.replace("varying", fragment ? "in" : "out").replace("attribute", fragment ? "???" : "in").replace("texture2D(", "texture(").replace("textureCube(", "texture(").replace("gl_FragColor", "fragColor");
                } else {
                    return source;
                }
            }
        }
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
