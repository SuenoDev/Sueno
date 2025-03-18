package org.durmiendo.sueno.graphics.g3d.wobj;

import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.g3d.Camera3D;
import arc.graphics.gl.Shader;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import org.durmiendo.sueno.graphics.SShaders;

public class Obj {
    public static Obj zero = new Obj(){
        public void render(Vec3 pos, Vec3 rot, Mat3D projection, Mat3D transform, Camera3D cam, Vec3 light, Vec3 scale) {}
    };

    public Seq<Material> materials = new Seq<>();
    public Shader shader = SShaders.g3d;
    public float scl = 1;

    public void render(Vec3 pos, Vec3 rot, Mat3D projection, Mat3D transform, Camera3D cam, Vec3 light, Vec3 scale) {
        for (int i = 0; i < materials.size; i++) {
            shader.bind();

            Material m = materials.get(i);
            Texture tex = m.mtl.texture;
            if (tex != null) tex.bind();

            apply(projection, transform, m);

            shader.setUniformf("u_pos", pos);
            shader.setUniformf("u_rot", rot);
            shader.setUniformf("u_campos", cam.position);
            shader.setUniformf("u_camdir", cam.direction);
            shader.setUniformf("u_lightdir", light);
            shader.setUniformf("u_scale", scale.x * scl, scale.y * scl, scale.z * scl);

            m.mesh.bind(shader);
            m.mesh.render(shader, Gl.triangles);
        }
    }

    private void apply(Mat3D projection, Mat3D transform, Material i) {
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);

        shader.setUniformf("u_ambient", i.mtl.ambient);
        shader.setUniformf("u_diffuse", i.mtl.diffuse);
        shader.setUniformf("u_specular", i.mtl.specular);
        shader.setUniformf("u_shininess", i.mtl.shininess);
        shader.setUniformi("u_illum", i.mtl.illum);
    }

    public static class Material {
        public String on;
        public Mtl mtl;
        public Mesh mesh;
    }

    public static class Mtl {
        public String name;
        public Texture texture;

        public Color ambient = Color.white;
        public Color diffuse = Color.white;
        public Color specular = Color.white;
        public float shininess;

        public int illum = 1;

        public Mtl() {}
    }
}


