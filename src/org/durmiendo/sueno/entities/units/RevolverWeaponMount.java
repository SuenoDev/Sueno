package org.durmiendo.sueno.entities.units;

import mindustry.entities.units.WeaponMount;
import mindustry.type.Weapon;
import org.durmiendo.sueno.type.weapon.RevolverWeapon;

public class RevolverWeaponMount extends WeaponMount {
    public int cartridges = 1;
    public float reloadCartridges = 1;
    public RevolverWeapon revolverWeapon;

    public RevolverWeaponMount(Weapon weapon) {
        super(weapon);
        revolverWeapon = (RevolverWeapon) weapon;
    }
}
