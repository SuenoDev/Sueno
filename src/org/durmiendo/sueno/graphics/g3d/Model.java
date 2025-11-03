package org.durmiendo.sueno.graphics.g3d;

import arc.graphics.Camera;
import arc.graphics.g3d.Camera3D;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import org.durmiendo.sueno.graphics.g3d.wobj.Obj;

public class Model {
    public Obj obj;
    public Vec3 pos;
    public Vec3 rotation;
    
    public Model() {}
    
    public Model(Obj obj, Vec3 pos, Vec3 rotation) {
        this.obj = obj;
        this.pos = pos;
        this.rotation = rotation;
    }
    
    public void render(Mat3D projection, Mat3D view, Camera3D camera, Vec3 light) {
        obj.render(pos, rotation, projection, view, camera, light);
    }
}
