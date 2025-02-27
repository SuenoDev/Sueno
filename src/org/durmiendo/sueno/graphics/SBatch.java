package org.durmiendo.sueno.graphics;

import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.gl.Shader;

public class SBatch extends SortedSpriteBatch {

    @Override
    protected void flush() {
//        super.flush();
        flushRequests();
        if(idx == 0) return;

        if (lastTexture instanceof NTexture n) {
            Shader b = customShader;
            customShader = SShaders.normalShader;
            getShader().bind();
            setupMatrices();

//            if(customShader != null && apply){
                customShader.apply();
//            }

            Gl.depthMask(false);
            int spritesInBatch = idx / SPRITE_SIZE;

            int count = spritesInBatch * 6;

            blending.apply();

            n.base.bind();
//            n.normal.bind(1);

            Mesh mesh = this.mesh;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);
            Gl.activeTexture(Gl.texture0);
            customShader = b;
        } else {
            getShader().bind();
            setupMatrices();

            if(customShader != null && apply){
                customShader.apply();
            }

            Gl.depthMask(false);
            int spritesInBatch = idx / SPRITE_SIZE;

            int count = spritesInBatch * 6;

            blending.apply();

            lastTexture.bind();

            Mesh mesh = this.mesh;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);
        }
        idx = 0;
    }
}
