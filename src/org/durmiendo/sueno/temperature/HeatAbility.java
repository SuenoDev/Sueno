package org.durmiendo.sueno.temperature;

import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import org.durmiendo.sueno.core.SVars;

public class HeatAbility extends mindustry.entities.abilities.Ability {
    public final HeatData hd;
    @Override
    public void displayBars(Unit unit, Table bars){

    }
    @Override
    public void draw(Unit unit) {
        super.draw(unit);
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);

        if (hd.isHeat) {
            unit.heal(hd.regeneration / 8f / 3.5f * Time.delta);
            if (SVars.temperatureController.at(unit) > hd.minSafeTemperature) {
                unit.heal(hd.overRegeneration / 8f / 3.5f /*(SVars.tempTemperatureController.at(unit) - hd.minSafeTemperature)*/ * Time.delta);
            }

            if (SVars.temperatureController.at(unit) > hd.minSafeTemperature) {
                float dam = hd.damage / 8f + hd.overDamage *
                        (SVars.temperatureController.at(unit) - hd.minSafeTemperature) / 3.5f * Time.delta;
                Groups.unit.each(u -> {
                    if (u.team != unit.team && u.dst(unit) / 8f < hd.damageRange) {
                        u.health -= dam;
                        if (u.health <= 0) u.destroy();
                    }
                });
                Geometry.circle(unit.tileX(), unit.tileY(), Mathf.round(hd.damageRange/1f), (x, y) -> {
                    Building b = Vars.world.build(x, y);
                    if (b != null && b.team != unit.team) {
                        b.health -= dam;
                        if (b.health <= 0) b.damage(1);
                    }
                });
            }


            if (hd.overArmor > 0 && SVars.temperatureController.at(unit) > hd.minSafeTemperature) {
                unit.armor = unit.type.armor + hd.overArmor;
            } // else {
//                unit.armor = unit.type.armor - hd.overArmor;
//            }


            if ((SVars.temperatureController.at(unit) < hd.capacity) && !TemperatureController.stop) {
                SVars.temperatureController.at(unit, hd.generateTemperature * Time.delta);
            } else {
                //SVars.temperatureController.at(unit, -hd.generateTemperature * Time.delta * 0.1f);
            }
        }
    }

    public HeatAbility(HeatData heatData) {
        super();
        hd = heatData;
    }
}
