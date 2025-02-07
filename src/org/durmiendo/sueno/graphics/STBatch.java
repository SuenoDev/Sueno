package org.durmiendo.sueno.graphics;

import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.VertexAttribute;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.gl.Shader;

public class STBatch extends SortedSpriteBatch {
    public final VertexAttribute rot = new VertexAttribute(1, "a_rotation");
    public static final int VERTEX_SIZE = 2 + 1 + 2 + 1 + 1;
    public static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
    protected Texture lastNormalTexture = null;

    public STBatch() {
        super();
        shader = cs();
        apply = true;
        int size = 4096;

        mesh = new Mesh(true, false, size * 4, size * 6,
                VertexAttribute.position,
                VertexAttribute.color,
                VertexAttribute.texCoords,
                VertexAttribute.mixColor,
                rot
        );
    }

    public static Shader cs(){
        return new Shader(
                """
                        attribute vec4 a_position;
                        attribute vec4 a_color;
                        attribute vec2 a_texCoord0;
                        attribute vec4 a_mix_color;
                        attribute float a_rotation;
                        uniform mat4 u_projTrans;
                        varying vec4 v_color;
                        varying vec4 v_mix_color;
                        varying vec2 v_texCoords;
                        varying float v_rotation;

                        void main(){
                           v_color = a_color;
                           v_rotation = a_rotation;
                           v_color.a = v_color.a * (255.0/254.0);
                           v_mix_color = a_mix_color;
                           v_mix_color.a *= (255.0/254.0);
                           v_texCoords = a_texCoord0;
                           gl_Position = u_projTrans * a_position;
                        }""",

                """
                        varying lowp vec4 v_color;
                        varying lowp vec4 v_mix_color;
                        varying highp vec2 v_texCoords;
                        varying highp float v_rotation;
                        uniform highp sampler2D u_texture;
                        uniform highp sampler2D u_normal;
                        
                        float rads(float deg) {
                            return deg * 3.14159265359 / 180.0;
                        }
                        
                        mat3 rotY(float a) {
                            float d = rads(a);
                            float c = cos(d);
                            float s = sin(d);
                
                            return mat3(
                                c, 0.0, s,
                                0.0, 1.0, 0.0,
                                -s, 0.0, c
                            );
                        }
                

                        void main(){
                          vec4 c = texture2D(u_texture, v_texCoords);
                          vec4 n = texture2D(u_normal, v_texCoords);
                          if (n.a > 0.0) {
                              vec3 rn = rotY(v_rotation) * vec3(n.x, n.y, n.z);
                              c = vec4(rn.x, rn.y, rn.z, 1.0) * c;
                              c.a = 1.0;
                          }
                          gl_FragColor = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);
                        }"""
        );
    }
}
