package org.durmiendo.sueno.spacestations;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.VertexAttribute;
import arc.graphics.g3d.Camera3D;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.struct.ShortSeq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.game.EventType;
import mindustry.type.Planet;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.graphics.g3d.Model;
import org.durmiendo.sueno.graphics.g3d.wobj.Obj;
import org.durmiendo.sueno.graphics.g3d.wobj.ObjParser;
import org.durmiendo.sueno.math.Vec4;
import org.durmiendo.sueno.utils.SLog;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SpaceStations {
    public static Obj cube;
    public Seq<Model> models = new Seq<>();
    
    
    public SpaceStations() {
        cube = ObjParser.loadObj("router/router", new Vec3(0.1f,0.1f,0.1f));
        SLog.info(cube.materials.first().mtl.name);
        SLog.info(cube.materials.first().mtl.texture.toString());
        
        for (int i = 0; i < 10; i++) {
            float r1 = Mathf.random(-5, +5);
            float r2 = Mathf.random(-5, +5);
            float r3 = Mathf.random(-5, +5);
            models.add(
                    new Model(cube, new Vec3(r1, r2, r3), Vec3.Zero.cpy())
            );
        }
        
    }
    

    Vec3 tmp = new Vec3();
    public void render(Mat3D projection, Mat3D view) {
        Camera3D cam = Vars.renderer.planets.cam;
        models.each(m -> {
            tmp.set(Planets.sun.position).sub(m.pos).nor();
            m.render(projection, view, cam, tmp);
        });
    }
}