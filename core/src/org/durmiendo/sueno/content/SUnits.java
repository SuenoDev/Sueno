package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Mover;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;


public class SUnits {

    public static @Annotations.EntityDef({Tankc.class, Unitc.class}) UnitType
            spark,singe,sear,sun;
    public static void load() {
        spark = new TankUnitType("spark"){{
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 3.5f;
            health = 1800f;
            armor = 3f;
            treadRects = new Rect[]{new Rect(11f - 32f, 4f - 32f, 14f, 54f - 1f)};
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
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 8f;
            health = 6800f;
            treadRects = new Rect[]{new Rect(10f - 44f, 9f - 44f, 19f, 62f - 1f)};
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("sueno-singe-weapon"){{
                y = -1.25f;
                layerOffset = 0.0001f;
                reload = 5f;
                shootY = 4.5f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 6f;
                mirror = false;
                x = 0f;

                heatColor = Color.valueOf("f9350f");
                cooldownTime = 30f;
                bullet = new BasicBulletType(){{
                    speed=5f;
                    damage=77f;
                    fragBullets=4;
                    fragAngle=8f;
                    lifetime = 50f;
                    fragBullet = new BasicBulletType(){{
                       damage=22f;
                       speed=1.4f;
                       drawSize=3f;
                    }};
                }};
            }});

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 1.5f;
                capacity = 130f;
                overArmor = 4f;
                minSafeTemperature = 100f;
                overRegeneration = 120f;
            }}));
        }};

        sear = new TankUnitType("sear"){{
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 16f;
            health = 14000f;
            treadRects = new Rect[]{new Rect(16-126f/2f, 6f-130f/2f, 25f, 87f-1f)};
            researchCostMultiplier = 0f;
            weapons.add(new Weapon("sueno-sear-gatling"){{

                shootY = 4.5f;
                recoil = 3f;
                parts.add(new RegionPart(){{
                    suffix = "-barrel-l";
                    progress = PartProgress.recoil;
                    recoilIndex = 0;
                    moveY = -1.5f;
                    under = true;
                    mirror = false;
                    outline = true;
                }}).add(new RegionPart(){{
                    suffix = "-barrel-center";
                    progress = PartProgress.recoil;
                    recoilIndex = 1;
                    moveY = -1.5f;
                    under = true;
                    mirror = false;
                    outline = true;
                }}).add(new RegionPart(){{
                    suffix = "-barrel-r";
                    progress = PartProgress.recoil;
                    recoilIndex = 2;
                    moveY = -1.5f;
                    under = true;
                    mirror = false;
                    outline = true;
                }});
                x = 0f;
                y = 0;
                top = true;
                layerOffset = 0.1f;
                shootY = 4f;
                reload = 3f;
                rotate = true;
                shootCone = 12f;
                rotateSpeed = 3.2f;
                recoil = 0f;
                mirror = false;

                shoot = new ShootBarrel(){{
                    barrels = new float[]{
                            -2f, 0f, 0f, 0f, 0f, 0f, 2f, 0f, 0f
                    };
                    bullet = new BasicBulletType(){{
                        speed = 4f;
                        damage = 77f;
                        lifetime = 70f;
                    }
                        @Override
                        public void removed(Bullet b) {
                            super.removed(b);
                            SVars.tempTemperatureController.at(Mathf.round(b.x), Mathf.round(b.y), 11110.1f);
                        }
                    };
                }};
                inaccuracy = 7f;
            }}).add(new Weapon("sear-missile"){{
                reload = 60f;
                bullet = new BasicBulletType(){{
                    speed = 4f;
                    damage = 77f;
                    lifetime = 30f;
                }
                public @Nullable Bullet create(@Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY){
                    damage+=(((Unit) owner).maxHealth - ((Unit) owner).health)/1000f;
                    return super.create(owner, shooter, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY);
                }
                };
            }});
            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 2.5f;
                capacity = 150f;
            }}));
        }};

        sun = new TankUnitType("sun"){{
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            health = 22000f;
            armor = 18f;
            treadRects = new Rect[]{new Rect(19f-140f/2f, 9f-160f/2f, 9f, 132f-1f)};
            researchCostMultiplier = 0f;
            weapons.add(new Weapon("sueno-sun-gun"){{
                mirror = false;
                rotate = true;
                reload = 50f;
                y = -5;
                x = 0;
                bullet = new BasicBulletType(){{
                    speed = 4.2f;
                    damage = 77f;
                    lifetime = 70f;
                }};
            }
                @Override
                protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
                if (SVars.tempTemperatureController.at(unit) > 130f) mount.bullet.damage*=1.2f;

                super.shoot(unit, mount, shootX, shootY, rotation);
                Fx.dynamicExplosion.at(unit.x, unit.y, World.conv(4*8f));
                Geometry.circle(unit.tileX(), unit.tileY(), 12, (x, y) -> SVars.tempTemperatureController.at(x, y, 10f));
            }
            });

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 4f;
                capacity = 180f;
            }
            }){
                public void update(Unit unit) {
                    super.update(unit);
                    Geometry.circle(unit.tileX(), unit.tileY(), 12, (x, y) -> {
                        Building c = Vars.world.build(x,y);
                        if (c!=null && c.team!=unit.team) c.health-=70*Time.delta;
                    });
                    HeatAbility ha = null;
                    for (Ability ability : unit.abilities) {
                        if (ability instanceof HeatAbility) {
                            ha = (HeatAbility) ability;
                            break;
                        }
                    }
                    if (ha == null) return;
                    float ph = (1-unit.health/unit.maxHealth)*10f;
                    ha.hd.regeneration*=ph*4f;
                    ha.hd.capacity*=ph*0.5f;
                }
            });
        }};
    }
}