package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.ai.types.BuilderAI;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.MoveEffectAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.ErekirUnitType;
import mindustry.type.unit.MissileUnitType;
import mindustry.type.unit.TankUnitType;
import mindustry.type.weapons.RepairBeamWeapon;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.entities.VEffect;
import org.durmiendo.sueno.entities.abilities.DeathZoneAbility;
import org.durmiendo.sueno.entities.bullet.SRailBulletType;
import org.durmiendo.sueno.entities.bullet.SunBulletType;
import org.durmiendo.sueno.entities.part.SHoverPart;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.SFx;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.math.Colorated;
import org.durmiendo.sueno.temperature.FreezingAbility;
import org.durmiendo.sueno.temperature.FreezingData;
import org.durmiendo.sueno.temperature.HeatAbility;
import org.durmiendo.sueno.temperature.HeatData;
import org.durmiendo.sueno.type.weapon.RevolverWeapon;
import org.durmiendo.sueno.world.units.types.VoidStriderUnitType;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.tilesize;


public class SUnits {

    public static @Annotations.EntityDef({Unitc.class, VoidStriderc.class, Legsc.class}) UnitType
            voidStrider;
    public static @Annotations.EntityDef({Unitc.class, ElevationMovec.class}) UnitType
            vessel, vial, space, engross;
    public static @Annotations.EntityDef({Tankc.class, Unitc.class}) UnitType
            spark, singe, sear, sun;

    public static @Annotations.EntityDef({Unitc.class, Payloadc.class}) UnitType
            believer;

    private static final Vec2 a = new Vec2();
    private static final Vec2 s = new Vec2();
    private static final Vec2 b = new Vec2();
    private static final Vec2 f = new Vec2();

    public static void load() {
        believer = new ErekirUnitType("believer") {{
            coreUnitDock = true;
            controller = u -> new BuilderAI(true, 500f);
            isEnemy = false;
            envDisabled = 0;

            targetPriority = -2;
            lowAltitude = false;
            mineWalls = true;
            mineFloor = true;
            mineHardnessScaling = false;
            flying = true;
            mineSpeed = 9f;
            mineTier = 3;
            buildSpeed = 1.5f;
            drag = 0.08f;
            speed = 7.5f;
            rotateSpeed = 8f;
            accel = 0.08f;
            itemCapacity = 110;
            health = 700f;
            armor = 3f;
            hitSize = 12f;
            buildBeamOffset = 8f;
            payloadCapacity = 2f * 2f * tilesize * tilesize;
            pickupUnits = true;
            vulnerableWithPayloads = true;

            fogRadius = 0f;
            targetable = false;
            hittable = false;

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 20f;
                x = 0f;
                y = 4.5f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.33f;
                aimDst = 0f;
                shootCone = 40f;
                mirror = false;

                repairSpeed = 3.6f / 2f;
                fractionRepairSpeed = 0.03f;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 65f;
                }};
            }});

            outlineColor = Color.valueOf("141414");

            engineOffset = 10.0f;
            trailColor = engineColor = Color.valueOf("ffbc7b");
            engineSize = 3f;

            trailLength = 12;
        }};

        vessel = new UnitType("vessel"){{
            hovering = true;
            health = 980f;
            armor = 4f;
            speed = 2.8f;
            rotateSpeed = 6.7f;

            engineColor = Color.valueOf("9aedd7");
            useEngineElevation = false;

            abilities.add(new MoveEffectAbility(0f, -9f, engineColor, Fx.missileTrailShort, 4f){{
            }});

            engines.add(new UnitEngine(){{
                x = 0f;
                y = -4.7f;
            }});

            parts.add(new SHoverPart(){{
                x = 4f;
                y = -4f;
                mirror = true;
                radius = 6f;
                phase = 63f;
                stroke = 2f;
                speed = -0.28f;
                layerOffset = -0.001f;
                color = Color.valueOf("79eef2");

            }}, new SHoverPart(){{
                x = 2.4f;
                y = 4.5f;
                mirror = true;
                radius = 4f;
                phase = 63f;
                stroke = 2f;
                speed = 0.28f;
                layerOffset = -0.001f;
                color = Color.valueOf("79eef2");

            }});



            weapons.add(new Weapon("sueno-vessel-weapon"){{
                recoil = 1f;
                mirror = false;
                x = 0f;
                y = 0f;
                reload = 19f;
                bullet = new SRailBulletType(){{
                    lifetime = 10f;

                    length = 175f;
                    instability = 0f;
                    damage = 17f;
                    hitColor = Color.valueOf("feb380");
                    pierceDamageFactor = 0.8f;
                    spread = 0f;
                    smokeEffect = Fx.none;
                    endEffect = hitEffect = new Effect(30, e -> {
                        color(Color.valueOf("aed4f2"), Color.white, e.fin());
                        stroke(1.5f * e.fout());
                        randLenVectors(e.id, 5, 24f * e.fin(), (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                        stroke(0.6f * e.fout());
                        color(Color.white);
                        randLenVectors(e.id, 5, 24f * e.fin(), (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 1.6f);
                        });

                    });
                    lineEffect = new Effect(23f, e -> {
                        if (!(e.data instanceof Vec2 v)) return;
                        b.set(e.x, e.y);
                        float len = v.dst(b);
                        Fx.rand.setSeed(e.id);
                        Color c = Color.valueOf("aed4f2");
                        stroke(1.2f * e.fout());
                        color(c);
                        f.set(v);

                        s.set(f.add(b));
                        s.x /= 2f; s.x += Fx.rand.random(-0.8f*len/8f, 0.8f*len/8f);
                        s.y /= 2f; s.y += Fx.rand.random(-0.8f*len/8f, 0.8f*len/8f);
                        a.set(s);
                        a.x += Fx.rand.random(-1.8f*len/8f, 1.8f*len/8f);
                        a.y += Fx.rand.random(-1.8f*len/8f, 1.8f*len/8f);

                        stroke(e.fout() * 1.2f + 0.6f);
                        e.scaled(14f, b -> {
                            stroke(1.6f * b.fout());
                            color(c);
                            Lines.curve(e.x, e.y, s.x, s.y, a.x, a.y, v.x, v.y, 10);

                            stroke(0.4f * b.fout());
                            color(Color.white);
                            Lines.curve(e.x, e.y, s.x, s.y, a.x, a.y, v.x, v.y, 10);
                        });

                    });
                }

                public final float deadIncrement = -0.0002f;
                    @Override
                    public void handlePierce(Bullet b, float initialHealth, float x, float y) {
                        SVars.temperatureController.at(Mathf.round(x)/8,Mathf.round(y)/8, deadIncrement);
                        super.handlePierce(b, initialHealth, x, y);
                    }

                    @Override
                    public void despawned(Bullet b) {
                        super.despawned(b);
                        SVars.temperatureController.at(Mathf.round(b.x)/8, Mathf.round(b.y)/8, deadIncrement);
                    }
                };
            }});

            abilities.add(new FreezingAbility(new FreezingData(){{
                tmrGenCapacity = -0.15f;
                tmrGenSpeed = 0.7f;

                tmrGenPowerFP = -0.00015f;
                tmrGenRadiusFP = 32f;
                tmrGenCapacityFP = -0.2f;
            }}));
        }};

        vial = new UnitType("vial"){{
            hovering = true;
            health = 4400f;
            armor = 6;


            abilities.add(new FreezingAbility(new FreezingData(){{

            }}));
        }};

        space = new UnitType("space"){{
            hovering = true;
            health = 9000f;
            armor = 7f;


            abilities.add(new FreezingAbility(new FreezingData(){{

            }}));
        }};


        engross = new UnitType("engross"){{
            hovering = true;
            health = 16000f;
            armor = 27f;
            hitSize = 42.5f;
            rotateSpeed = 1f;
            speed = 0.8f;

            engines.add(new UnitEngine(){{
                x = 0f;
                y = -17.3f;
                radius = 6.4f;
            }});

            parts.add(new SHoverPart(){{
                x = 8.8f;
                y = -16.7f;
                mirror = true;
                radius = 7f;
                phase = 63f;
                stroke = 2f;
                speed = -0.15f;
                layerOffset = -0.001f;
                color = Color.valueOf("79eef2");
            }}, new SHoverPart(){{
                x = 16.5f;
                y = -10.6f;
                mirror = true;
                radius = 6f;
                phase = 90f;
                stroke = 2.3f;
                speed = 0.11f;
                layerOffset = -0.001f;
                color = Color.valueOf("79eef2");
            }});


            weapons.add(new RepairBeamWeapon("sueno-engross-gun"){{
                mirror = true;
                x = 10.625f;
                y = -7.083f;
                range = 48f;
                shootY = 4.5f;
                beamWidth = 0.5f;
                repairSpeed = 1f;

                bullet = new BulletType(){{
                    maxRange = 65f;
                }};
            }});

            float spawnTime = 60f * 3.8f;
            abilities.add(new DeathZoneAbility(new MissileUnitType("bomb"){{
                range = 8f * 2.5f;
                playerControllable = false;
                logicControllable = false;
                speed = 0.24f;
                rotateSpeed = 2.5f;
                health = 80f;
                armor = 3f;
                lifetime = 24f * 60f;
                outlineColor = Color.valueOf("353535");
                engineOffset = 2f;
                engineSize = 2f;

                homingDelay = 20f;

                weapons.add(new Weapon(){{
                    shootCone = 360f;
                    mirror = false;
                    reload = 1f;
                    shootOnDeath = true;
                    bullet = new ExplosionBulletType(140f, 28f){{
                        collidesAir = true;
                        suppressionRange = 140f;
                        shootEffect = new ExplosionEffect(){{
                            lifetime = 50f;
                            waveStroke = 5f;
                            waveLife = 8f;
                            waveColor = Color.white;
                            sparkColor = smokeColor = Pal.redSpark;
                            waveRad = 40f;
                            smokeSize = 4f;
                            smokes = 7;
                            smokeSizeBase = 0f;
                            sparks = 10;
                            sparkRad = 40f;
                            sparkLen = 6f;
                            sparkStroke = 2f;
                        }};
                    }};
                }});
            }}, spawnTime, 40f, 80f, 4));
        }};









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

        public Color
                s = Color.valueOf("FF5A40").a(0.07f),
                e = Color.valueOf("FF0000").a(0.23f);

            @Override
            public void draw(Unit unit) {
                super.draw(unit);
                Draw.draw(Layer.light, () -> {
                    Draw.color(Colorated.gradient(
                            s, e, Math.min((SVars.temperatureController.at(unit) - 0.35f), 0f)
                    ));
                    Draw.scl(3.5f);
                    Draw.rect(SVars.core.getRegion("glow"), unit.x, unit.y);
                    Drawf.light(unit.x, unit.y,56f, e, 0.4f);
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
                reload = 9.4f;
                shootY = 3.65f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 4f;
                mirror = false;
                x = 0f;

                heatColor = Color.valueOf("f9350f");
                cooldownTime = 30f;
                bullet = new SRailBulletType(){{

                    lifetime = 10f;
                    length = 210f;
                    instability = 80f;
                    damage = 15f;
                    hitColor = Color.valueOf("feb380");
                    pierceDamageFactor = 0.8f;
                    spread = 4f;
                    smokeEffect = Fx.none;
                    endEffect = hitEffect = new Effect(30, e -> {
                        color(e.color);
                        stroke(1.2f * e.fout());
                        randLenVectors(e.id + 1, 8, 1f + 12f * e.finpow(), (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                        color(Color.white, e.color, e.fin());
                        stroke(1.5f * e.fout());
                        randLenVectors(e.id, 4, 24f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 9f, (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                    });

                    shootEffect = new Effect(10, e -> {
                        color(e.color);
                    });

                    Color c = Color.valueOf("f9350f");
                    lineEffect = new Effect(23f, e -> {
                        if(!(e.data instanceof Vec2 v)) return;
                        Fx.rand.setSeed(e.id);

                        c.a = 0.5f;
                        for(int i = 0; i < 10; i++) {
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            stroke(e.fout() * 1.6f + 0.6f);
                            color(c);
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 12f * Fx.rand.random(0.5f, 1f) + 0.3f);
                            stroke(e.fout() * 1.2f + 0.2f);
                            color(e.color);
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 12f * Fx.rand.random(0.5f, 1f) + 0.3f);
                        }

                        for (int i = 0; i < 5; i++) {
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            stroke(e.fout() * 1.6f + 0.6f);
                            color(c);
                            Drawf.tri(e.x + Fx.v.x, e.y + Fx.v.y,e.foutpowdown() * 6.4f * Fx.rand.random(0.5f, 1f) + 0.3f,e.foutpowdown() * 3f * Fx.rand.random(0.5f, 1f) + 0.3f, e.rotation + e.finpow() + 90);
                            stroke(e.fout() * 1.2f + 0.2f);
                            color(e.color);
                            Drawf.tri(e.x + Fx.v.x, e.y + Fx.v.y,e.foutpowdown() * 6.4f * Fx.rand.random(0.5f, 1f) + 0.3f,e.foutpowdown() * 3f * Fx.rand.random(0.5f, 1f) + 0.2f, e.rotation + e.finpow() + 90);
                        }

                        for (int i = 0; i < 5; i++) {
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            stroke(e.fout() * 1.6f + 0.6f);
                            color(c);
                            Drawf.tri(e.x + Fx.v.x, e.y + Fx.v.y,e.foutpowdown() * 6.4f * Fx.rand.random(0.5f, 1f) + 0.3f,e.foutpowdown() * 3f * Fx.rand.random(0.5f, 1f) + 0.3f, e.rotation + e.finpow() - 90);
                            stroke(e.fout() * 1.2f + 0.2f);
                            color(e.color);
                            Drawf.tri(e.x + Fx.v.x, e.y + Fx.v.y,e.foutpowdown() * 6.4f * Fx.rand.random(0.5f, 1f) + 0.3f,e.foutpowdown() * 3f * Fx.rand.random(0.5f, 1f) + 0.2f, e.rotation + e.finpow() - 90);
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
                        SVars.temperatureController.at(Mathf.round(x)/8,Mathf.round(y)/8, 0.01f);
                        super.handlePierce(b, initialHealth, x, y);

                    }

                    @Override
                    public void despawned(Bullet b) {
                        super.despawned(b);
                        Vec2 nor = Tmp.v1.trns(b.rotation(), 1f).nor();
                        SVars.temperatureController.at(Mathf.round(b.x + nor.x * b.fdata)/8, Mathf.round(b.y + nor.y * b.fdata)/8, 0.01f);
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
                recoils = 3;
                x = 0f;
                y = 0;
                top = true;
                layerOffset = 0.1f;
                shootY = 4f;
                reload = 13.5f;
                rotate = true;
                shootCone = 12f;
                rotateSpeed = 3.2f;
                mirror = false;
                inaccuracy = 3f;


                shoot = new ShootBarrel(){{
                    shootY = 8f;
                    recoil = 1.2f;

                    barrels = new float[]{
                            -2f, 0f, 0f, 0f, 0f, 0f, 2f, 0f, 0f
                    };
                    bullet = new BasicBulletType(){
                        float length = 24f;
                        Color toColor = Color.valueOf("feb380");
                        Color fromColor = Color.valueOf("ffc494");
                        {
                        speed = 17.4f;
                        damage = 77f;
                        lifetime = 26f;
                        width = 7.3f;
                        hitColor = Color.valueOf("ffdab7");
                        shootEffect = smokeEffect = Fx.none;
                        hitEffect = new Effect(30f, e -> {
                            color(fromColor, e.color, e.fin());
                            stroke(2f * e.fout());
                            randLenVectors(e.id, 12, 65f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 20f, (x, y) -> {
                                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                            });
                            stroke(1.5f * e.fout());
                            randLenVectors(e.id, 6, 24f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-1.2f, 1.2f)-180f, 4f, (x, y) -> {
                                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                            });
                        });


                        trailEffect = new Effect(30f, e -> {
                            color(e.color);
                            Drawf.tri(e.x + Mathf.cosDeg(e.rotation) * 32f * e.fin(), e.y + Mathf.sinDeg(e.rotation) * 32f * e.fin(), 3 * e.foutpowdown(), 3 * e.foutpowdown(), e.rotation);
                        });
                        trailChance = 0.3f;
                        trailRotation = true;

                        despawnEffect = new Effect(30f, e -> {
                            color(fromColor, e.color, e.fin());
                            stroke(2f * e.fout());
                            randLenVectors(e.id, 12, 125f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 20f, (x, y) -> {
                                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                            });
                        });
                        
                        trailWidth = 0.8f;
                        trailLength = 9;
                        pierce = true;

                    }
                        @Override
                        public void draw(Bullet b) {
                            super.draw(b);
                            float realLength = length, rot = b.rotation();

                            Draw.color(fromColor, toColor, b.fin());
                            Drawf.tri(b.x, b.y, width * b.fout(), (realLength + 50), b.rotation());
                            Drawf.tri(b.x, b.y, width * b.fout(), 3f, b.rotation() + 180f);
                            Draw.reset();
                            Drawf.light(b.x, b.y, b.x + Angles.trnsx(rot, realLength), b.y + Angles.trnsy(rot, realLength), width * 2.5f * b.fout(), toColor, lightOpacity);
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
            }});//.add(new Weapon("sear-missile"){{
//                reload = 60f;
//                bullet = new BasicBulletType(){{
//                    speed = 4f;
//                    damage = 77f;
//                    lifetime = 30f;
//                }
//                public @Nullable Bullet create(@Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY){
//                    damage+=(((Unit) owner).maxHealth - ((Unit) owner).health)/1000f;
//                    return super.create(owner, shooter, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY);
//                }
//                };
//            }});
            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 0.0125f;
                capacity = 0.75f;
            }}));
        }};

        sun = new TankUnitType("sun"){{
            outlineColor = Color.valueOf("141414");
            treadPullOffset = 3;
            speed = 0.75f;
            rotateSpeed = 1.2f;
            health = 22000f;
            armor = 18f;
            treadRects = new Rect[]{new Rect(19f-140f/2f, 21f-160f/2f, 20f, 132f)};
            researchCostMultiplier = 0f;
            hitSize = 24f;
            singleTarget = true;
            targetGround = true;
            targetAir = true;
            weapons.add(new RevolverWeapon("sueno-sun-gun"){{
                parts.add(new RegionPart(){{
                    suffix = "-blade";
                    under = true;
                    mirror = true;
                    x = 4.25f;
                    y = 17f;
                    moveRot = 180f;
                    moveY = 5f;
                    moveX = 0.5f;
                    progress = PartProgress.warmup;
                }}, new RegionPart(){{
                        suffix = "-coil";
                        mirror = true;
                        under = true;
                        moveY = 2f;
                        moveX = 1f;
                        progress = PartProgress.warmup;
                        moves.add(new PartMove(){{
                            y = -1f;
                            x = -1f;
                            progress = PartProgress.recoil;
                        }});
                }});
                mirror = false;
                top = true;
                layerOffset = 0.01f;
                y = -5f;
                x = 0f;
                reload = 25f;
                inaccuracy = 2f;
                rotate = true;

                rotateSpeed = 1.2f;
                shootSound = Sounds.bang;
                shootY = 24f;
                shoot = new ShootAlternate(){{
                    barrels = 1;
                    shots = 1;
                    spread = 8.5f;
                }};
                maxCartridges = 7;
                reloadCartridges = 120f;
                bullet = new SunBulletType() {{
                    trailLength = 12;
                    homingPower = 0;
                    speed = 1.1f;
                    damage = 34f;
                    lifetime = 245f;

                    hitEffect = despawnEffect = SFx.deadSun;

                    trailChance = 0.4f;
                    trailInterval = 44f;
                    trailRotation = true;
                    trailEffect = new Effect(48f, e -> {
                        color(e.color);
                        stroke(0.6f * e.foutpowdown());
                        randLenVectors(e.id+1, 2, 1.9f, (x, y) -> {
                            Lines.lineAngle(e.x + x + Mathf.cosDeg(e.rotation) * e.fin() * 8f, e.y + y + Mathf.sinDeg(e.rotation) * e.fin() * 8f, e.rotation-180f, 4.6f * e.foutpowdown());
                        });
                    });
                }


                public Color bullet = Color.valueOf("FFF2B3");
                    @Override
                    public Bullet create(Entityc owner, Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, Mover mover, float aimX, float aimY) {
                        Bullet b = super.create(owner, shooter, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY);
                        SFx.sun.create(x, y, 0, bullet, b);

                        return b;
                    }
                };
            }

                @Override
                public void addStats(UnitType u, Table t) {
                    super.addStats(u, t);
                    t.row();
                    t.add("[#FF3A2E]Снаряд поглащает силу вражеских снарядов");
                    t.row();
                    t.add("[#8A5BFF]Снаряд имеет гравитацию");
                }

                @Override
                protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
                if (SVars.temperatureController.at(unit) > 0.65f) mount.bullet.damage*=1.2f;

                super.shoot(unit, mount, shootX, shootY, rotation);
                //Fx.dynamicExplosion.at(unit.x, unit.y, World.conv(4*8f));
                //Geometry.circle(unit.tileX(), unit.tileY(), 12, (x, y) -> SVars.temperatureController.at(x, y, 0.05f));
            }
            });

            abilities.add(new HeatAbility(new HeatData(true){{
                generateTemperature = 0.02f;
                capacity = 0.9f;
            }
            }){
                public void update(Unit unit) {
                    super.update(unit);
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

            outlineColor = Color.valueOf("141414");
            speed = 0.75f;
            hitSize = 26f;
            stepShake = 0.2f;
            rotateSpeed = 1.2f;
            health = 12000f;
            armor = 38f;
            legCount = 4;
            legBaseOffset = 5f;
            legGroupSize = 2;
            legMoveSpace = 1.5f;
            legForwardScl = 1.2f;
            legLength = 30f;
            legExtension = -5.5f;
            legMinLength = 0.5f;
            legMaxLength = 1.2f;
            legLengthScl = 1f;
            rippleScale = 0.24f;


            parts.add(new RegionPart("-wing"){{
                mirror = true;
                layerOffset = 0.01f;
                layer = Layer.groundUnit + 0.1f;
                moves.add(new PartMove(){{
                    y = -1f;
                    x = -1f;
                    moveX = 3.6f;
                    moveY = 0f;
                    rotation = 0f;
                    moveRot = -20f;
                    progress = PartProgress.charge;
                }});
            }});

            weapons.add(new Weapon(){{
                bullet = new BasicBulletType(){{
                    damage = 420f;
                    speed = 10.5f;
                    shootEffect = Fx.none;

                    homingDelay = 2f;
                    homingPower = 0.01f;
                    homingRange = 32f;

                    trailColor = Color.valueOf("ffffff");
                    trailLength = 9;
                    trailWidth = 1.75f;
                    trailSinScl = 2.5f;
                    trailSinMag = 0.5f;
                    trailEffect = Fx.none;

                    sprite = "sueno-laarge-orb";

                    hitEffect = despawnEffect = new ExplosionEffect(){{
                        layer = SLayers.voidspace;
                        lifetime = 50f;
                        waveStroke = 0;
                        waveLife = 0;
                        waveColor = Color.clear;
                        sparkColor = smokeColor = Color.lightGray;
                        waveRad = 0f;
                        smokeSize = 4f;
                        smokes = 7;
                        smokeSizeBase = 0f;
                        sparks = 10;
                        sparkRad = 40f;
                        sparkLen = 6f;
                        sparkStroke = 2f;
                    }};
                }

                public final Effect t = new VEffect(400.0F, (e) -> {
                    Object trail$temp = e.data;
                    if (trail$temp instanceof Trail trail) {
                        e.lifetime = (float)trail.length * 1.4F;
                        if (!Vars.state.isPaused()) {
                            trail.shorten();
                        }

                        Draw.z(SLayers.voidspace);
                        trail.drawCap(e.color, e.rotation);
                        Draw.z(SLayers.voidspace);
                        trail.draw(e.color, e.rotation);
                    }
                });
                    public void drawTrail(Bullet b) {
                        if (this.trailLength > 0 && b.trail != null) {
                            float z = SLayers.voidspace;
                            Draw.z(z);
                            b.trail.draw(this.trailColor, this.trailWidth);
                            Draw.z(z);
                        }

                    }

                    public void removed(Bullet b) {
                        if (this.trailLength > 0 && b.trail != null && b.trail.size() > 0) {
                            Draw.z(SLayers.voidspace);
                            t.at(b.x, b.y, this.trailWidth, this.trailColor, b.trail.copy());
                        }
                    }
            };

                mirror = true;
                recoil = 0.5f;
                reload = 5f;
                shootX = -10f;
                shootY = 0f;
                inaccuracy = 6.5f;
                shoot = new ShootPattern() {{
                    shots = 3;
                    shotDelay = 3.2f;
                    reload = 42f;
                }};
            }});


            collapseEffect = SFx.voidStriderCollapseEffect;
            collapseRadius = 80;
        }};
    }
}