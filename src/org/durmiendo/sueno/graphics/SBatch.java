package org.durmiendo.sueno.graphics;

import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.VertexAttribute;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import org.durmiendo.sueno.core.SVars;

import java.nio.FloatBuffer;

public class SBatch extends SortedSpriteBatch {
    public static final VertexAttribute rot = new VertexAttribute(1, "a_rotation");
    public static final int VERTEX_SIZE = 2 + 1 + 2 + 1 + 1;
    public static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
    protected Texture lastNormalTexture = null;

    int maxSpritesInBatch = 0;

    public SBatch(){
        this(4096, null);
    }

    public SBatch(int size){
        this(size, null);
    }



    public SBatch(int size, Shader defaultShader){
        super();
        shader = cs();
        apply = true;

        mesh = new Mesh(true, false, size * 4, size * 6,
                VertexAttribute.position,
                VertexAttribute.color,
                VertexAttribute.texCoords,
                VertexAttribute.mixColor,
                rot
        );

        if (size > 0) {
            buffer = mesh.getVerticesBuffer();
        }
    }


    protected Shader getShader(){
        return customShader == null ? shader : customShader;
    }
    protected FloatBuffer buffer;

    @Override
    protected void flush(){
        flushRequests();
        if (idx == 0) return;
        getShader().bind();
        setupMatrices();

        if(customShader != null && apply){
            customShader.apply();
        }

        Gl.depthMask(false);
        int count = idx / SPRITE_SIZE * 6;

        blending.apply();

        lastTexture.bind();
        if (lastNormalTexture != null)
            lastNormalTexture.bind();
        Mesh mesh = this.mesh;
        //calling buffer() marks it as dirty, so it gets reuploaded upon render
        mesh.getVerticesBuffer();

        buffer.position(0);
        buffer.limit(idx);

        mesh.render(getShader(), Gl.triangles, 0, count);

        buffer.limit(buffer.capacity());
        buffer.position(0);

        idx = 0;
    }

    protected void switchTexture(Texture texture, Texture normal){
        flush();
        lastTexture = texture;
        lastNormalTexture = normal;
    }

    @Override
    protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation){
        Texture texture = region.texture;
        if(texture != lastTexture){
            TextureRegion n = SVars.textureToNormal.get(region);
            if (n == null) switchTexture(texture, null);
            else switchTexture(texture, n.texture);
        }else if(idx == vertices.length){
            flush();
        }

        float[] vertices = this.vertices;
        int idx = this.idx;
        this.idx += SPRITE_SIZE;

        if(!Mathf.zero(rotation)){
            float worldOriginX = x + originX;
            float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            float cos = Mathf.cosDeg(rotation);
            float sin = Mathf.sinDeg(rotation);

            float x1 = cos * fx - sin * fy + worldOriginX;
            float y1 = sin * fx + cos * fy + worldOriginY;
            float x2 = cos * fx - sin * fy2 + worldOriginX;
            float y2 = sin * fx + cos * fy2 + worldOriginY;
            float x3 = cos * fx2 - sin * fy2 + worldOriginX;
            float y3 = sin * fx2 + cos * fy2 + worldOriginY;
            float x4 = x1 + (x3 - x2);
            float y4 = y3 - (y2 - y1);

            float u = region.u;
            float v = region.v2;
            float u2 = region.u2;
            float v2 = region.v;

            float color = this.colorPacked;
            float mixColor = this.mixColorPacked;

            vertices[idx] = x1;
            vertices[idx + 1] = y1;
            vertices[idx + 2] = color;
            vertices[idx + 3] = u;
            vertices[idx + 4] = v;
            vertices[idx + 5] = mixColor;
            vertices[idx + 6] = rotation;

            vertices[idx + 7] = x2;
            vertices[idx + 8] = y2;
            vertices[idx + 9] = color;
            vertices[idx + 10] = u;
            vertices[idx + 11] = v2;
            vertices[idx + 12] = mixColor;
            vertices[idx + 13] = rotation;

            vertices[idx + 14] = x3;
            vertices[idx + 15] = y3;
            vertices[idx + 16] = color;
            vertices[idx + 17] = u2;
            vertices[idx + 18] = v2;
            vertices[idx + 19] = mixColor;
            vertices[idx + 20] = rotation;

            vertices[idx + 21] = x4;
            vertices[idx + 22] = y4;
            vertices[idx + 23] = color;
            vertices[idx + 24] = u2;
            vertices[idx + 25] = v;
            vertices[idx + 26] = mixColor;
            vertices[idx + 27] = rotation;
        }else{
            float fx2 = x + width;
            float fy2 = y + height;
            float u = region.u;
            float v = region.v2;
            float u2 = region.u2;
            float v2 = region.v;


            float color = this.colorPacked;
            float mixColor = this.mixColorPacked;

            vertices[idx] = x;
            vertices[idx + 1] = y;
            vertices[idx + 2] = color;
            vertices[idx + 3] = u;
            vertices[idx + 4] = v;
            vertices[idx + 5] = mixColor;
            vertices[idx + 6] = rotation;

            vertices[idx + 7] = x;
            vertices[idx + 8] = fy2;
            vertices[idx + 9] = color;
            vertices[idx + 10] = u;
            vertices[idx + 11] = v2;
            vertices[idx + 12] = mixColor;
            vertices[idx + 13] = rotation;

            vertices[idx + 14] = fx2;
            vertices[idx + 15] = fy2;
            vertices[idx + 16] = color;
            vertices[idx + 17] = u2;
            vertices[idx + 18] = v2;
            vertices[idx + 19] = mixColor;
            vertices[idx + 20] = rotation;

            vertices[idx + 21] = fx2;
            vertices[idx + 22] = y;
            vertices[idx + 23] = color;
            vertices[idx + 24] = u2;
            vertices[idx + 25] = v;
            vertices[idx + 26] = mixColor;
            vertices[idx + 27] = rotation;
        }
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
