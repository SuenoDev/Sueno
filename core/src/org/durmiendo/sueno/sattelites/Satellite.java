package org.durmiendo.sueno.sattelites;

import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.scene.ui.ImageButton;

import static arc.Core.camera;
import static arc.Core.graphics;

public class Satellite {
    public int id;
    public SatelliteBase base;
    private ImageButton button;
    private SPlanet planet;
    private float orbitRadius = 50f;
    private float orbitSpeed = 50f;
    private float currentAngle = 0f;
    public Vec3 position = new Vec3();

    public Satellite(int id, SPlanet planet, SatelliteBase base) {
        this.planet = planet;
        this.id = id;
        this.base = base;
    }



    public void draw() {

    }

    public void remove() {
        planet.satellites.remove(this);
    }
}
