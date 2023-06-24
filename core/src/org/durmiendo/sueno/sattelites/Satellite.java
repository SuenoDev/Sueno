package org.durmiendo.sueno.sattelites;

import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.Log;
import mindustry.type.Planet;


public class Satellite {
    private Planet planet;
    public int id;
    public SatelliteBase base;
    //private ImageButton button;

    private float orbitRadius = 50f;
    private float orbitSpeed = 50f;
    private float distance = 0;
    private float spacing = 0;

    public Vec3 position = new Vec3();
    public Vec3 center;

    public Satellite(int id, SatelliteBase base, float r, float spacing, Planet planet) {
        this.id = id;
        this.base = base;
        this.spacing = spacing;
        this.orbitRadius = r;
        this.planet = planet;

        init();
    }



    public void draw() {

        if(planet == null) return;




        position.x = orbitRadius * Mathf.cos(Mathf.degRad * spacing) * Mathf.cos(Mathf.degRad * distance) + center.x;
        position.y = orbitRadius * Mathf.cos(Mathf.degRad * spacing) * Mathf.sin(Mathf.degRad * distance) + center.y;
        position.z = orbitRadius * Mathf.sin(Mathf.degRad * spacing) + center.z;


        //Vec3 butVec = Vars.renderer.planets.cam.project(position);
        Vec3 butVec = new Vec3(10, 10, 0);
        float butX = butVec.x;
        float butY = butVec.y;

        Log.info("draw at " + position + "and " + butVec);

        //button.setPosition(butX,butY);
        //button.draw();

    }

    public void init() {
        center = planet.position;
        position = new Vec3();
        //button = new ImageButton(Core.atlas.find("sueno-satellite"), new ImageButton.ImageButtonStyle());
        //button.clicked(() -> {

            //Log.info("click");

        //});
    }
}
