package org.durmiendo.sueno.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.ai.types.BuilderAI;
import mindustry.annotations.Annotations;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.MoveEffectAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.ErekirUnitType;
import mindustry.type.unit.TankUnitType;
import mindustry.type.weapons.RepairBeamWeapon;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.entities.bullet.SRailBulletType;
import org.durmiendo.sueno.entities.part.SHoverPart;
import org.durmiendo.sueno.gen.VoidStriderc;
import org.durmiendo.sueno.graphics.SFx;
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


            abilities.add(new FreezingAbility(new FreezingData(){{

            }}));
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

            @Override
            public void draw(Unit unit) {
                super.draw(unit);
                Draw.draw(Layer.light, () -> {
                    Draw.color(Colorated.gradient(Color.valueOf("FF5A40").a(0.07f), Color.valueOf("FF0000").a(0.6f), (SVars.temperatureController.at(unit) + SVars.temperatureController.tk)/(SVars.temperatureController.tk *2f)));
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
                    lifetime = 10f;
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
                        randLenVectors(e.id + 1, 8, 1f + 12f * e.finpow(), (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                        color(Color.white, e.color, e.fin());
                        stroke(1.5f * e.fout());
                        randLenVectors(e.id, 4, 24f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 9f, (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
                        });
                    });

                    shootEffect = new Effect(10, e -> color(e.color));

                    lineEffect = new Effect(23f, e -> {
                        if(!(e.data instanceof Vec2 v)) return;
                        Fx.rand.setSeed(e.id);
                        Color c = Color.valueOf("f9350f");
                        c.a = 0.5f;
                        for(int i = 0; i < 10; i++){
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
                        width = 4.3f;
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

                        despawnEffect = new Effect(30f, e -> {
                            color(fromColor, e.color, e.fin());
                            stroke(2f * e.fout());
                            randLenVectors(e.id, 12, 65f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 20f, (x, y) -> {
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
                bullet = new MissileBulletType() {{
                    trailLength = 12;
                    homingPower = 0;
                    speed = 1.6f;
                    damage = 77f;
                    lifetime = 190f;
                }

                    float ranges = 32f;

                    @Override
                    public void update(Bullet b) {
                        super.update(b);
                        Damage.damage(b.team, b.x, b.y, ranges, 25 * Time.delta);
                    }

                    @Override
                    public void loadIcon() {
                        super.loadIcon();
                        bultex = Core.atlas.find("sueno-sun-bullet");
                    }

                    float lfosb = 68f;
                    TextureRegion bultex;

                    public void draw(Bullet b) {
                        float fin = (Time.time % lfosb) / lfosb;

                        float fin1 = (Time.time % (lfosb*1.2f)) / (lfosb*1.2f);
//                        color(Pal.lightOrange);
//                        stroke(2.9f * (0.5f - mod(fin1 - 0.5f)));
//                        randLenVectors(b.id + (long)(Time.time / (lfosb*1.2f)), 7, 1f, (x, y) -> {
//                            lineAngle(b.x + x, b.y + y, Mathf.angle(x, y) + fin1 * 80, fin1 * 16f);
//                        });
//
//                        color(Pal.lighterOrange);
//                        stroke(1.5f * (0.5f - mod(fin - 0.5f)));
//                        randLenVectors(b.id + 1 + (long)(Time.time / (lfosb)), 10, 1f, (x, y) -> {
//                            lineAngle(b.x + x, b.y + y, Mathf.angle(x, y) + fin * 66, fin * 16f);
//                        });
//
//                        color(Color.white);
//                        stroke(0.8f * (0.5f - mod(fin - 0.5f)));
//                        randLenVectors(b.id + 1 + (long)(Time.time / (lfosb)), 14, 1f, (x, y) -> {
//                            lineAngle(b.x + x, b.y + y, Mathf.angle(x, y) + fin * 66, fin * 24f);
//                        });

                        super.draw(b);
                        bultex.scale = (Mathf.cos(2f * Mathf.pi * (fin-0.5f)) + 15) / 16f - 0.0625f;
                        Drawf.spinSprite(bultex, b.x, b.y, ((Mathf.cos(2f * Mathf.pi * (fin-0.5f)) + 25) / 26f) * 360f - 166.153846f);
                    }

                    float mod(float x){
                        if (x < 0) x*=-1;
                        return x;
                    }
                };
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
            speed = 1.75f;
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
                layer =  Layer.groundUnit+0.1f;
            }});

            collapseEffect = SFx.voidStriderCollapseEffect;
            collapseRadius = 80;
        }};
    }
}