package org.durmiendo.sueno.basic;


import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.struct.Seq;
import mindustry.graphics.g3d.PlanetGrid;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;
import mindustry.type.Sector;

public class SuenoPlanet extends Planet {

    public Seq<Satellite> satellites = new Seq<>();

    public SuenoPlanet(String name, Planet parent, float radius){
        super(name, parent, radius);

        this.radius = radius;
        this.parent = parent;
        this.orbitOffset = Mathf.randomSeed(id + 1, 360);

        //total radius is initially just the radius
        totalRadius = radius;

        //get orbit radius by extending past the parent's total radius
        orbitRadius = parent == null ? 0f : (parent.totalRadius + parent.orbitSpacing + totalRadius);

        //orbit time is based on radius [kepler's third law]
        orbitTime = Mathf.pow(orbitRadius, 1.5f) * 1000;

        //add this planet to list of children and update parent's radius
        if(parent != null){
            parent.children.add(this);
            parent.updateTotalRadius();
        }

        //calculate solar system
        for(solarSystem = (Planet) this; solarSystem.parent != null; solarSystem = solarSystem.parent);
    }

    public SuenoPlanet(String name, Planet parent, float radius, int sectorSize){
        this(name, parent, radius);

    }

    public Seq<Satellite> getSatellites() {
        return satellites;
    }

    public void addSatellite(Satellite sat) {
        satellites.add(sat);
    }

    public void removeSatellite(Satellite sat) {
        sat.remove();
    }

    public void draw(PlanetParams params, Mat3D projection, Mat3D transform) {
        super.draw(params, projection, transform);
        for (Satellite satellite : satellites) {
            satellite.draw();
        }
    }
}
