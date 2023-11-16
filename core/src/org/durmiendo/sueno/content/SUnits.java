package org.durmiendo.sueno.content;

import arc.math.geom.Rect;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.unit.TankUnitType;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;


public class SUnits {

    public static UnitType spark,singe,sear,sun;
    public static void load() {
        spark = new TankUnitType("spark"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 3.5f;
            health = 1800f;
            armor = 3f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 1f;
                capacity = 120f;
                minSafeTemperature = 70f;
                damageRange = 7f;
                dpsOverTemperature = 0.2f;
                damage = 20f;
                regeneration = 20f;
            }}));
        }};

        singe = new TankUnitType("singe"){{
            constructor = UnitEntity::create;
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 3.5f;
            health = 6800f;
            armor = 8f;
            treadRects = new Rect[]{new Rect( -4, -4, 8, 8)};
            researchCostMultiplier = 0f;

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 1.5f;
                capacity = 130f;
                minSafeTemperature = 70f;
                damageRange = 7f;
                dpsOverTemperature = 0.2f;
                damage = 20f;
                regeneration = 20f;
            }}));
        }};
    }
}
