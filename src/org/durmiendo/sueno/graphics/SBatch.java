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

import java.lang.reflect.Field;

public class SBatch extends SortedSpriteBatch {
    public Mesh meshn;

    //new xy + color + uv + mix_color + rotation + uv2
    public static final int VERTEX_SIZEN = 2 + 1 + 2 + 1 + 1 + 2;
    public static final int SPRITE_SIZEN = 4 * VERTEX_SIZEN;

    //old xy + color + uv + mix_color
    public static final int VERTEX_SIZEO = 2 + 1 + 2 + 1;
    public static final int SPRITE_SIZEO = 4 * VERTEX_SIZEO;

    public static VertexAttribute rot = new VertexAttribute(1, "a_rotation");
    public static VertexAttribute uvn = new VertexAttribute(2, Shader.texcoordAttribute + "1");

    public SBatch() {
    }


    public void create() {
        set("tmpVertices", new float[SPRITE_SIZEN]);
        set("vertices", new float[SPRITE_SIZEN*4096]);
//        setn("SPRITE_SIZE", SPRITE_SIZEN);
//        setn("VERTEX_SIZE", VERTEX_SIZEN);
        shader = SShaders.normalShader;
//        shader = createShader();

//        try {
//            Field ff = Core.batch.getClass().getField("lastTexture");
//            ff.setAccessible(true);
//            lastTexture = (Texture)ff.get(Core.batch);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        meshn = new Mesh(true, false, 4096 * 4, 4096 * 6,
                VertexAttribute.position,
                VertexAttribute.color,
                VertexAttribute.texCoords,
                VertexAttribute.mixColor,
                rot,
                uvn
        );
        int len = 4096 * 6;
        short[] indices = new short[len];
        short j = 0;
        for(int i = 0; i < len; i += 6, j += 4){
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }
        meshn.setIndices(indices);
    }


    protected void setn(String name, Object value) {
        try {
            Field tmpVertices = getClass().getField(name);
            tmpVertices.setAccessible(true);
            tmpVertices.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void set(String name, Object value) {
        try {
            Field tmpVertices = getClass().getField(name);
            tmpVertices.setAccessible(true);
            tmpVertices.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void flush() {
//        super.flush();
        flushRequests();
        if(idx == 0) return;

        if (lastTexture instanceof NTexture n) {
//            Shader b = customShader;
//            customShader = SShaders.normalShader;
            getShader().bind();
            setupMatrices();

//            if(customShader != null && apply){
                getShader().apply();
//            }

            Gl.depthMask(false);

            int spritesInBatch = idx / SPRITE_SIZEN;
            int count = spritesInBatch * 6;

            blending.apply();

            n.normal.bind(1);
            n.base.bind(0);

            Mesh mesh = this.meshn;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);

//            customShader = b;
        } else {
            getShader().bind();
            setupMatrices();

            if(customShader != null && apply){
                getShader().apply();
            }

            Gl.depthMask(false);

            int spritesInBatch = idx / SPRITE_SIZEN;
            int count = spritesInBatch * 6;

            blending.apply();

            lastTexture.bind(0);

//            deconstruct();

            Mesh mesh = this.meshn;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);
        }
        idx = 0;
    }



//    public void deconstruct(){
//        int c = idx/SPRITE_SIZEN;
//        for (int i = 0; i < c; i++) {
//            int i1= i*SPRITE_SIZEN;
//            int i2 = i*SPRITE_SIZEO;
//
//            vertices[i2] = vertices[i1];
//            vertices[i2+1] = vertices[i1+1];
//            vertices[i2+2] = vertices[i1+2];
//            vertices[i2+3] = vertices[i1+3];
//            vertices[i2+4] = vertices[i1+4];
//            vertices[i2+5] = vertices[i1+5];
//
//            vertices[i2+6] = vertices[i1+9];
//            vertices[i2+7] = vertices[i1+10];
//            vertices[i2+8] = vertices[i1+11];
//            vertices[i2+9] = vertices[i1+12];
//            vertices[i2+10] = vertices[i1+13];
//            vertices[i2+11] = vertices[i1+14];
//
//            vertices[i2+12] = vertices[i1+18];
//            vertices[i2+13] = vertices[i1+19];
//            vertices[i2+14] = vertices[i1+20];
//            vertices[i2+15] = vertices[i1+21];
//            vertices[i2+16] = vertices[i1+22];
//            vertices[i2+17] = vertices[i1+23];
//
//            vertices[i2+18] = vertices[i1+27];
//            vertices[i2+19] = vertices[i1+28];
//            vertices[i2+20] = vertices[i1+29];
//            vertices[i2+21] = vertices[i1+30];
//            vertices[i2+22] = vertices[i1+31];
//            vertices[i2+23] = vertices[i1+32];
//        }
//    }

    public float[] reconstruct(float[] verticesl){
        int iter = verticesl.length/SPRITE_SIZEO;
        float[] tmpv = new float[SPRITE_SIZEN*iter];
        for (int i = 0; i < iter; i++) {
            int i2 = i*SPRITE_SIZEN;
            int i1 = i*SPRITE_SIZEO;


            tmpv[i2     ] = verticesl[i1];
            tmpv[i2 + 1 ] = verticesl[i1 + 1];
            tmpv[i2 + 2 ] = verticesl[i1 + 2];
            tmpv[i2 + 3 ] = verticesl[i1 + 3];
            tmpv[i2 + 4 ] = verticesl[i1 + 4];
            tmpv[i2 + 5 ] = verticesl[i1 + 5];
            tmpv[i2 + 6 ] = 0;
            tmpv[i2 + 7 ] = 0;
            tmpv[i2 + 8 ] = 0;

            tmpv[i2 + 9 ] = verticesl[i1 + 6];
            tmpv[i2 + 10] = verticesl[i1 + 7];
            tmpv[i2 + 11] = verticesl[i1 + 8];
            tmpv[i2 + 12] = verticesl[i1 + 9];
            tmpv[i2 + 13] = verticesl[i1 + 10];
            tmpv[i2 + 14] = verticesl[i1 + 11];
            tmpv[i2 + 15] = 0;
            tmpv[i2 + 16] = 0;
            tmpv[i2 + 17] = 0;

            tmpv[i2 + 18] = verticesl[i1 + 12];
            tmpv[i2 + 19] = verticesl[i1 + 13];
            tmpv[i2 + 20] = verticesl[i1 + 14];
            tmpv[i2 + 21] = verticesl[i1 + 15];
            tmpv[i2 + 22] = verticesl[i1 + 16];
            tmpv[i2 + 23] = verticesl[i1 + 17];
            tmpv[i2 + 24] = 0;
            tmpv[i2 + 25] = 0;
            tmpv[i2 + 26] = 0;

            tmpv[i2 + 27] = verticesl[i1 + 18];
            tmpv[i2 + 28] = verticesl[i1 + 19];
            tmpv[i2 + 29] = verticesl[i1 + 20];
            tmpv[i2 + 30] = verticesl[i1 + 21];
            tmpv[i2 + 31] = verticesl[i1 + 22];
            tmpv[i2 + 32] = verticesl[i1 + 23];
            tmpv[i2 + 33] = 0;
            tmpv[i2 + 34] = 0;
            tmpv[i2 + 35] = 0;
        }
        return tmpv;
    }

    @Override
    protected void draw(Texture texture, float[] spriteVertices, int offset, int count){
        if(sort && !flushing){
            super.draw(texture, spriteVertices, offset, count);
        }else{
            offset = offset/SPRITE_SIZEO*SPRITE_SIZEN;
            float[] v = reconstruct(spriteVertices);
            int verticesLength = vertices.length;
            spriteVertices = v;

            int remainingVertices = verticesLength;
            if (texture != lastTexture) {
                switchTexture(texture);
            } else {
                remainingVertices -= idx;
                if (remainingVertices == 0) {
                    flush();
                    remainingVertices = verticesLength;
                }
            }
            count = count/SPRITE_SIZEO*SPRITE_SIZEN;
            int copyCount = Math.min(remainingVertices, count);
//            SLog.dInfo("copyCount: " + copyCount + " " + v.length);
            System.arraycopy(spriteVertices, offset, vertices, idx, copyCount);
            idx += copyCount;
            count -= copyCount;
            while (count > 0) {
                offset += copyCount;
                flush();
                copyCount = Math.min(verticesLength, count);
                System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
                idx += copyCount;
                count -= copyCount;
            }
        }
    }


    @Override
    protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation){
        if(sort && !flushing){
            super.draw(region, x, y, originX, originY, width, height, rotation);
        }else{
            if (region.texture != lastTexture) {
                switchTexture(region.texture);
            } else if (idx == vertices.length) {
                flush();
            }

            float[] vertices = this.vertices;
            int idx = this.idx;
            this.idx += SPRITE_SIZEN;

            if (!Mathf.zero(rotation)) {
                //bottom left and top right corner points relative to origin
                float worldOriginX = x + originX;
                float worldOriginY = y + originY;
                float fx = -originX;
                float fy = -originY;
                float fx2 = width - originX;
                float fy2 = height - originY;

                // rotate
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

                float[] uvns = SVars.regionToUV.get(region); //uv u2v2
                float un = uvns != null ? uvns[0] : 0;
                float v2n = uvns != null ? uvns[1]: 0;
                float u2n = uvns != null ? uvns[2] : 0;
                float vn = uvns != null ? uvns[3] : 0;

                float color = this.colorPacked;
                float mixColor = this.mixColorPacked;

                vertices[idx] = x1;
                vertices[idx + 1] = y1;
                vertices[idx + 2] = color;
                vertices[idx + 3] = u;
                vertices[idx + 4] = v;
                vertices[idx + 5] = mixColor;
                vertices[idx + 6] = rotation;
                vertices[idx + 7] = un;
                vertices[idx + 8] = vn;

                vertices[idx + 9] = x2;
                vertices[idx + 10] = y2;
                vertices[idx + 11] = color;
                vertices[idx + 12] = u;
                vertices[idx + 13] = v2;
                vertices[idx + 14] = mixColor;
                vertices[idx + 15] = rotation;
                vertices[idx + 16] = un;
                vertices[idx + 17] = v2n;

                vertices[idx + 18] = x3;
                vertices[idx + 19] = y3;
                vertices[idx + 20] = color;
                vertices[idx + 21] = u2;
                vertices[idx + 22] = v2;
                vertices[idx + 23] = mixColor;
                vertices[idx + 24] = rotation;
                vertices[idx + 25] = u2n;
                vertices[idx + 26] = v2n;

                vertices[idx + 27] = x4;
                vertices[idx + 28] = y4;
                vertices[idx + 29] = color;
                vertices[idx + 30] = u2;
                vertices[idx + 31] = v;
                vertices[idx + 32] = mixColor;
                vertices[idx + 33] = rotation;
                vertices[idx + 34] = u2n;
                vertices[idx + 35] = vn;
//                SLog.tInfo(idx + " vs: " + Arrays.toString(), 100);
            } else {
                float fx2 = x + width;
                float fy2 = y + height;
                float u = region.u;
                float v = region.v2;
                float u2 = region.u2;
                float v2 = region.v;

                float[] uvns = SVars.regionToUV.get(region);
                float un = uvns != null ? uvns[0] : 0;
                float v2n = uvns != null ? uvns[1]: 0;
                float u2n = uvns != null ? uvns[2] : 0;
                float vn = uvns != null ? uvns[3] : 0;

                float color = this.colorPacked;
                float mixColor = this.mixColorPacked;

                vertices[idx] = x;
                vertices[idx + 1] = y;
                vertices[idx + 2] = color;
                vertices[idx + 3] = u;
                vertices[idx + 4] = v;
                vertices[idx + 5] = mixColor;
                vertices[idx + 6] = rotation;
                vertices[idx + 7] = un;
                vertices[idx + 8] = vn;

                vertices[idx + 9] = x;
                vertices[idx + 10] = fy2;
                vertices[idx + 11] = color;
                vertices[idx + 12] = u;
                vertices[idx + 13] = v2;
                vertices[idx + 14] = mixColor;
                vertices[idx + 15] = rotation;
                vertices[idx + 16] = un;
                vertices[idx + 17] = v2n;

                vertices[idx + 18] = fx2;
                vertices[idx + 19] = fy2;
                vertices[idx + 20] = color;
                vertices[idx + 21] = u2;
                vertices[idx + 22] = v2;
                vertices[idx + 23] = mixColor;
                vertices[idx + 24] = rotation;
                vertices[idx + 25] = u2n;
                vertices[idx + 26] = v2n;

                vertices[idx + 27] = fx2;
                vertices[idx + 28] = y;
                vertices[idx + 29] = color;
                vertices[idx + 30] = u2;
                vertices[idx + 31] = v;
                vertices[idx + 32] = mixColor;
                vertices[idx + 33] = rotation;
                vertices[idx + 34] = u2n;
                vertices[idx + 35] = vn;
            }
        }
    }
}
