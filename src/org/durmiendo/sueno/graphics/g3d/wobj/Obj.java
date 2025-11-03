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
import arc.util.Log;
import org.durmiendo.sueno.graphics.SShaders;

public class Obj {
    public static Obj zero = new Obj(){
        public void render(Vec3 pos, Vec3 rot, Mat3D projection, Mat3D view, Camera3D cam, Vec3 light) {}
    };
    
    public Seq<Material> materials = new Seq<>();
    public Shader shader = SShaders.g3d;
    public Vec3 scl = new Vec3(1.0f, 1.0f, 1.0f);
    
    private final Mat3D worldTransform = new Mat3D();
    
    public void render(Vec3 pos, Vec3 rot, Mat3D projection, Mat3D view, Camera3D cam, Vec3 light) {
        shader.bind();
        
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_view", view.val);
        shader.setUniformf("u_campos", cam.position);
        shader.setUniformf("u_lightdir", light);
        
        for (int i = 0; i < materials.size; i++) {
            Material m = materials.get(i);
            
            worldTransform.idt();
            worldTransform.translate(pos);
            worldTransform.rotate(Vec3.X, rot.x);
            worldTransform.rotate(Vec3.Y, rot.y);
            worldTransform.rotate(Vec3.Z, rot.z);
            worldTransform.scale(scl);
            
            shader.setUniformMatrix4("u_worldTrans", worldTransform.val);
            
            Texture tex = m.mtl.texture;
            if (tex != null) {
                tex.bind(0);
                shader.setUniformi("u_texture", 0);
            }
            
            applyMaterial(m);
            
            m.mesh.bind(shader);
            m.mesh.render(shader, Gl.triangles);
            m.mesh.unbind(shader);
        }
    }
    
    private void applyMaterial(Material i) {
        Color ambient = i.mtl.ambient;
        Color diffuse = i.mtl.diffuse;
        Color specular = i.mtl.specular;
        
        shader.setUniformf("u_ambientColor", ambient.r, ambient.g, ambient.b);
        shader.setUniformf("u_diffuseColor", diffuse.r, diffuse.g, diffuse.b);
        shader.setUniformf("u_specularColor", specular.r, specular.g, specular.b);
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
        
        public Color ambient = Color.white.cpy();
        public Color diffuse = Color.white.cpy();
        public Color specular = Color.white.cpy();
        public float shininess = 32f;
        public int illum = 2;
        
        public Mtl() {}
    }
}