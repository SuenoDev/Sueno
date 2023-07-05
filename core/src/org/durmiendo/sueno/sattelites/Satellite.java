package org.durmiendo.sueno.sattelites;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.scene.ui.Button;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.ui.dialogs.SatelliteDialog;


public class Satellite {
    private Planet planet;
    public int id;
    public SatelliteBase base;
    public ImageButton button;

    public float orbitRadius = 55f;
    public float distance = 0; // XY
    public float spacing = 0; // ZX
    public Vec2 speed = new Vec2(20,0);

    public Vec3 position = new Vec3();
    public Vec3 center;

    public Satellite(int id, SatelliteBase base, float r, float spacing, float distance, Planet planet) {
        this.id = id;
        this.base = base;
        this.spacing = spacing;
        this.orbitRadius = r;
        this.planet = planet;
        this.distance = distance;

        center = planet.position;
        position = new Vec3();
        Log.info("satellite init");
        init();
    }



    public void draw() {

        if(planet == null) return;

        //SVars.satelliteController.removeSatellite(this);
        init();

        renderOrbit(36);
        button.draw();
    }

    public void init() {
        //SVars.satelliteController.addSatellite(this);
        button = new ImageButton(new ImageButton.ImageButtonStyle());
        button.image(Core.atlas.find("sueno-satellite"));
        button.clicked(() -> {
            Log.info("click");
            SVars.ui.satellite.draw();
        });
        button.sizeBy(50);
        Vec3 e = Vars.renderer.planets.cam.project(newPos(new Vec3(), spacing, distance));
        button.setPosition(e.x, e.y);

    }

    public Vec3 newPos(Vec3 r, float spacing_, float distance_) {
        r.x = orbitRadius * Mathf.cos(Mathf.degRad * spacing_) * Mathf.cos(Mathf.degRad * distance_) + center.x;
        r.y = orbitRadius * Mathf.cos(Mathf.degRad * spacing_) * Mathf.sin(Mathf.degRad * distance_) + center.y;
        r.z = orbitRadius * Mathf.sin(Mathf.degRad * spacing_) + center.z;

        return r;
    }

    public void renderOrbit(int i) {
        float s = 25;
        float k = 2;
        float r = k;
        float spac = spacing;
        float dist = distance;
        Vec3 proj = Vars.renderer.planets.cam.project(newPos(new Vec3(), spac, dist));
        Vec3 newproj = new Vec3(proj);

        for(int j = 0;i>j;j++){
            proj = Vars.renderer.planets.cam.project(newPos(proj, spac + r, dist ));
            newproj = Vars.renderer.planets.cam.project(newPos(newproj, spac + speed.x/k - r , dist + speed.y/k));
            Drawf.line(new Color(0xffffff33), newproj.x + s, newproj.y + s ,  proj.x + s, proj.y + s );
            spac += speed.x / k;
            dist += speed.y / k;

        }
    }

    public void update() {
        spacing += speed.x / 1000;
        distance += speed.y / 1000;
        newPos(position, spacing, distance);
    }

}