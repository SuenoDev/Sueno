package org.durmiendo.sueno.sattelites;

import arc.Core;
import arc.graphics.Camera;
import arc.graphics.GL20;
import arc.graphics.Gl;
import arc.graphics.g3d.Camera3D;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.scene.ui.Button;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import mindustry.Vars;


import static arc.Core.assets;
import static arc.Core.gl;


public class Satellite {
    public int id;
    public SatelliteBase base;
    private ImageButton button;

    private float orbitRadius = 50f;
    private float orbitSpeed = 50f;
    private float distance = 0;
    private float spacing = 0;

    public Vec3 position = new Vec3();
    public Vec3 center;

    public Satellite(int id, SatelliteBase base, float r, float spacing, Vec3 center) {
        this.id = id;
        this.base = base;
        this.spacing = spacing;
        this.orbitRadius = r;
        this.center = center;

        init();
    }



    public void draw() {

        position.x = orbitRadius * Mathf.cos(Mathf.degRad * spacing) * Mathf.cos(Mathf.degRad * distance) + center.x;
        position.y = orbitRadius * Mathf.cos(Mathf.degRad * spacing) * Mathf.sin(Mathf.degRad * distance) + center.y;
        position.z = orbitRadius * Mathf.sin(Mathf.degRad * spacing) + center.z;


        Vec3 butVec = Vars.renderer.planets.cam.project(position);
        float butX = butVec.x;
        float butY = butVec.y;

        button.setPosition(butX,butY);
        button.draw();

    }

    public void init() {
        button = new ImageButton(Core.atlas.find("sueno-satellite"), new ImageButton.ImageButtonStyle());
        button.clicked(() -> {

            Log.info("click");

        });
    }
}
