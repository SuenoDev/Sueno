package org.durmiendo.sueno.graphics.g3d;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.g2d.Draw;
import arc.graphics.g3d.Camera3D;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.geom.Vec3;
import arc.util.Disposable;
import org.durmiendo.sueno.core.SVars;

public class S3Renderer implements Disposable {
    public Camera3D cam;
    public FrameBuffer buffer;

    Mesh mesh;

    Mat projection = new Mat();
    Mat transformation = new Mat();
    Vec3 translation = new Vec3();

    public Shader shader = new Shader(
            SVars.internalFileTree.child("shaders/3d.vert"),
            SVars.internalFileTree.child("shaders/3d.frag")
    );

    public S3Renderer(){
        init();
    }

    public void init() {
        cam = new Camera3D();
        cam.fov = 100;
        cam.near = 0.1f;
        cam.far = 10000;

        buffer = new FrameBuffer(2, 2, true);
    }

    public void render() {
        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.update(); // обязательно обновляем камеру


        // готовим Gl
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);
        Gl.enable(Gl.cullFace);
        Gl.cullFace(Gl.back);

        buffer.begin(Color.clear); // чистим буфер и начинаем запись

        baseRender();

        buffer.end(); // прекращаем писать в буфер

        // Gl в исходную
        Gl.disable(Gl.cullFace);
        Gl.disable(Gl.depthTest);
        Gl.depthMask(false);

        // рисуем рельтаты рендера
        Draw.blit(buffer, shader);
    }

    void baseRender() {
        if (mesh == null || shader == null) {
            return;
        }

        projection.setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        transformation.idt();
        translation.set(100, 100, 0);

        shader.bind();
        shader.setUniformMatrix4("u_proj", projection);
        shader.setUniformMatrix4("u_transf", transformation);
        shader.setUniformf("u_transl", translation.x, translation.y, translation.z);

        mesh.render(shader, Gl.triangles);
    }

    public void dispose() {
        if (mesh != null) {
            mesh.dispose();
        }
        if (shader != null) {
            shader.dispose();
        }
    }
}
