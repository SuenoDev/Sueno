package org.durmiendo.sueno.graphics.g3d;

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
    public Seq<Material> materials = new Seq<>();
    public Shader shader = SShaders.g3d;

    public void render(Vec3 pos, Vec3 rot, Mat3D projection, Mat3D transform, Camera3D cam, Vec3 light, float scale, int target) {
        for (int i = 0; i < materials.size; i++) {
            if (target != -1 && target != i) continue;
            shader.bind();

            Texture tex = materials.get(i).mtl.texture;
            if (tex != null) tex.bind();

            shader.setUniformMatrix4("u_proj", projection.val);
            shader.setUniformMatrix4("u_trans", transform.val);
            shader.setUniformf("u_ambient", materials.get(i).mtl.ambient);
            shader.setUniformf("u_diffuse", materials.get(i).mtl.diffuse);
            shader.setUniformf("u_specular", materials.get(i).mtl.specular);
            shader.setUniformf("u_shininess", materials.get(i).mtl.shininess);
            shader.setUniformi("u_illum", materials.get(i).mtl.illum);
            shader.setUniformf("u_pos", pos);
            shader.setUniformf("u_rot", rot);
            shader.setUniformf("u_campos", cam.position);
            shader.setUniformf("u_camdir", cam.direction);
            shader.setUniformf("u_lightdir", light);
            shader.setUniformf("u_scale", scale);

            Mesh mesh = materials.get(i).mesh;
            mesh.bind(shader);

            mesh.render(shader, Gl.triangles);
        }
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


