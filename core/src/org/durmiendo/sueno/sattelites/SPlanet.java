package org.durmiendo.sueno.sattelites;


import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;

// TODO that's so~ bad (controllers better)

public class SPlanet extends Planet {

    public Seq<Satellite> satellites = new Seq<>();

    public SPlanet(String name, Planet parent, float radius){
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

    public SPlanet(String name, Planet parent, float radius, int sectorSize){
        this(name, parent, radius);

    }

    public Seq<Satellite> getSatellites() {
        return satellites;
    }

    public void addSatellite(Satellite sat) {
        satellites.add(sat);
    }

    public void removeSatellite(Satellite sat) {
        SVars.satelliteController.removeSatellite(sat);
    }
}
