package org.durmiendo.sueno.temperature;

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
                Groups.unit.each(u -> {
                    if (unit.dst(u.x, u.y) < hd.damageRange*8 && u.team != unit.team) {
                        //TODO: Fix this
                        u.damage((hd.damage / 8f + hd.overDamage * (SVars.temperatureController.at(u) - hd.minSafeTemperature) / 8f) / 3.5f * Time.delta);
                    }
                });

                Geometry.circle(unit.tileX(), unit.tileY(), Math.round(hd.damageRange), (x, y) -> {
                    Building b = Vars.world.build(x, y);
                    if (b != null && b.team != unit.team) {
                        b.damage((hd.damage / 8f + hd.overDamage * (SVars.temperatureController.at(x, y) - hd.minSafeTemperature)) * Time.delta);
                    }
                });
            }


            if (hd.overArmor > 0 && SVars.temperatureController.at(unit) > hd.minSafeTemperature) {
                unit.armor = unit.type.armor + hd.overArmor;
            } else {
                unit.armor = unit.type.armor - hd.overArmor;
            }


            if ((SVars.temperatureController.at(unit) < hd.capacity) && !SVars.temperatureController.stop) {
                SVars.temperatureController.at(unit, hd.generateTemperature * Time.delta);
            } else {
                SVars.temperatureController.at(unit, -hd.generateTemperature * Time.delta);
            }
        }
    }

    public HeatAbility(HeatData heatData) {
        super();
        hd = heatData;
    }
}
