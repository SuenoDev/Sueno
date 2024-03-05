package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.RailBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.entities.bullet.SRailBulletType;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.math.Colorated;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;
import org.durmiendo.sueno.world.units.types.VoidStriderUnitType;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;


public class SUnits {

    public static @Annotations.EntityDef({Unitc.class, VoidStriderc.class, Legsc.class}) UnitType voidStrider;

    public static @Annotations.EntityDef({Tankc.class, Unitc.class}) UnitType
            spark, singe, sear, sun;
    public static void load() {
        spark = new TankUnitType("spark"){{
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 3.5f;
            health = 1800f;
            armor = 3f;
            treadRects = new Rect[]{new Rect(11f - 64f/2f, 4f - 64f/2f, 14f, 55f)};
            researchCostMultiplier = 0f;

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 0.005f;
                capacity = 0.6f;
                minSafeTemperature = 0.35f;
                damageRange = 7f;
                dpsOverTemperature = 0.2f;
                damage = 20f;
                regeneration = 20f;
            }}));
        }

            @Override
            public void draw(Unit unit) {
                super.draw(unit);
                Draw.draw(Layer.light, () -> {
                    Draw.color(Colorated.gradient(Color.valueOf("FF5A40").a(0.07f), Color.valueOf("FF0000").a(0.9f), (SVars.temperatureController.at(unit) + SVars.temperatureController.tk)/(SVars.temperatureController.tk *2f)));
                    Draw.scl(3.5f);
                    Draw.rect(SVars.core.getRegion("glow"), unit.x, unit.y);
                });
            }
        };

        singe = new TankUnitType("singe"){{
            outlineColor = Color.valueOf("141414");
            hitSize = 12f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            armor = 8f;
            health = 6800f;
            treadRects = new Rect[]{new Rect(10f - 88f/2f, 11f - 88f/2f, 18f, 65f)};
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("sueno-singe-weapon"){{
                y = -1.25f;
                layerOffset = 0.0001f;
                reload = 6.4f;
                shootY = 4.5f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 4f;
                mirror = false;
                x = 0f;

                heatColor = Color.valueOf("f9350f");
                cooldownTime = 30f;
                bullet = new SRailBulletType(){{
                    length = 210f;
                    instability = 80f;
                    damage = 12f;
                    hitColor = Color.valueOf("feb380");
                    pierceDamageFactor = 0.8f;
                    spread = 4f;
                    smokeEffect = Fx.none;
                    endEffect = hitEffect = new Effect(30, e -> {
                        color(e.color);
                        stroke(1.2f * e.fout());
                        randLenVectors(e.id + 1, 15, 1f + 12f * e.finpow(), (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                        color(Color.white, e.color, e.fin());
                        stroke(1.5f * e.fout());
                        randLenVectors(e.id, 5, 24f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 9f, (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                    });

                    shootEffect = new Effect(10, e -> color(e.color));

                    lineEffect = new Effect(23f, e -> {
                        if(!(e.data instanceof Vec2 v)) return;
                        Fx.rand.setSeed(e.id);
                        Color c = Color.valueOf("f9350f");
                        c.a = 0.5f;
                        for(int i = 0; i < 18; i++){
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            stroke(e.fout() * 1.6f + 0.6f);
                            color(c);
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 12f * Fx.rand.random(0.5f, 1f) + 0.3f);
                            stroke(e.fout() * 1.2f + 0.2f);
                            color(e.color);
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 12f * Fx.rand.random(0.5f, 1f) + 0.3f);
                        }
                        stroke(e.fout() * 1.2f + 0.6f);
                        e.scaled(14f, b -> {
                            stroke(b.fout() * 1.8f);
                            color(c);
                            Lines.line(e.x, e.y, v.x, v.y);
                            stroke(e.fout() * 0.9f);
                            color(e.color);
                            Lines.line(e.x, e.y, v.x, v.y);
                        });
                    });
                }

                    @Override
                    public void handlePierce(Bullet b, float initialHealth, float x, float y) {
                        SVars.temperatureController.at(Mathf.round(x)/8,Mathf.round(y)/8, 0.5f);
                        super.handlePierce(b, initialHealth, x, y);
                    }

                    @Override
                    public void despawned(Bullet b) {
                        super.despawned(b);
                        Vec2 nor = Tmp.v1.trns(b.rotation(), 1f).nor();
                        SVars.temperatureController.at(Mathf.round(b.x + nor.x * b.fdata)/8, Mathf.round(b.y + nor.y * b.fdata)/8, 0.5f);
                    }
                };
            }});

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 0.0075f;
                capacity = 0.65f;
                overArmor = 4f;
                minSafeTemperature = 0.5f;
                overRegeneration = 120f;
                regeneration = 0f;
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
            treadRects = new Rect[]{new Rect(17-126f/2f, 36f-130f/2f, 23f, 91f)};
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
                    bullet = new RailBulletType(){{
                        speed = 4f;
                        damage = 77f;
                        lifetime = 70f;
                    }
                        @Override
                        public void handlePierce(Bullet b, float initialHealth, float x, float y) {
                            SVars.temperatureController.at(Mathf.round(x)/8,Mathf.round(y)/8, 0.0005f);
                            super.handlePierce(b, initialHealth, x, y);
                        }

                        @Override
                        public void despawned(Bullet b) {
                            super.despawned(b);
                            Vec2 nor = Tmp.v1.trns(b.rotation(), 1f).nor();
                            SVars.temperatureController.at(Mathf.round(b.x + nor.x * b.fdata)/8, Mathf.round(b.y + nor.y * b.fdata)/8, 0.0005f);
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
                generateTemperature = 0.0125f;
                capacity = 0.75f;
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
            treadRects = new Rect[]{new Rect(20f-140f/2f, 15f-160f/2f, 17f, 132f)};
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
                if (SVars.temperatureController.at(unit) > 130f) mount.bullet.damage*=1.2f;

                super.shoot(unit, mount, shootX, shootY, rotation);
                Fx.dynamicExplosion.at(unit.x, unit.y, World.conv(4*8f));
                Geometry.circle(unit.tileX(), unit.tileY(), 12, (x, y) -> SVars.temperatureController.at(x, y, 0.05f));
            }
            });

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 0.02f;
                capacity = 0.9f;
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

        voidStrider = new VoidStriderUnitType("void-strider"){{
            speed = 0.5f;
            health = 150;
            outlineColor = Color.valueOf("141414");
            hitSize = 64f;
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            health = 22000f;
            armor = 18f;

            collapseEffect = SFx.voidStriderCollapseEffect;
            collapseRadius = 80;
        }};
    }
}