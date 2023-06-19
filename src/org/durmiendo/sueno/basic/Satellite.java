package org.durmiendo.sueno.basic;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.ui.ImageButton;
import mindustry.world.blocks.Attributes;
import org.durmiendo.sueno.content.ui.SatelliteDialog;

import static mindustry.Vars.universe;

public class Satellite {
    public int id;
    public SatelliteBase base;
    private ImageButton button;
    private SuenoPlanet planet;
    private float orbitRadius = 50f;
    private float orbitSpeed = 50f;
    private float currentAngle = 0f;

    public Satellite(int id,SuenoPlanet planet, SatelliteBase base) {
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
