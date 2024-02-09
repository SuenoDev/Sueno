package org.durmiendo.sueno.temperature;

public class HeatData {
    /**
     * generate n temperature of second
     */
    public float

    generateTemperature,


    /**
     * maximum amount of temperature per unit
     */
    capacity,

    /**
     * minimum temperature to cause damage
     */
    minSafeTemperature,

    /**
     * n damage per second within a certain radius
     */
    damageRange,

    /**
     * dps over temperature, when overheated,
     * a unit begins to deal n damage per second within a certain radius
     */
    dpsOverTemperature,

    /**
     * damage of units into radius
     */
    damage,

    /**
     * damage from excess temperature,
     * calculated according to the formula if temperature is over minSafeTemperature:
     *
     * (temperature - minSafeTemperature) * overDamage + damage
     */
    overDamage,

    /**
     * n hp regen of sec
     */
    regeneration,

    /**
     * adds regeneration to a unit if its temperature is above n
     */
    overRegeneration,

    /**
     * adds armor to a unit if its temperature is above n
     */
    overArmor;

    /**
     * determines whether the unit will heat up
     */
    public boolean isHeat = false;

    public HeatData(boolean isHeat) {
        this.isHeat = isHeat;
    }

    public HeatData() {}
}
