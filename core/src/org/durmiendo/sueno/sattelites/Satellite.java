package org.durmiendo.sueno.sattelites;

import arc.scene.ui.ImageButton;

public class Satellite {
    public int id;
    public SatelliteBase base;
    private ImageButton button;
    private SPlanet planet;
    private float orbitRadius = 50f;
    private float orbitSpeed = 50f;
    private float currentAngle = 0f;

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
