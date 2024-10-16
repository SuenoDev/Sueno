package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.Prop;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import mindustry.world.meta.Stat;
import org.durmiendo.sueno.entities.bullet.AreaLaserBullet;
import org.durmiendo.sueno.world.blocks.DBlock;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemperatureSource;
import org.durmiendo.sueno.world.blocks.defense.turrets.MachineGunTurret;
import org.durmiendo.sueno.world.blocks.defense.turrets.SItemTurret;
import org.durmiendo.sueno.world.blocks.defense.turrets.SirenTurret;
import org.durmiendo.sueno.world.blocks.distribution.Monorail;
import org.durmiendo.sueno.world.blocks.environment.Ice;
import org.durmiendo.sueno.world.blocks.storage.SCoreBlock;
import org.durmiendo.sueno.world.blocks.walls.UnDestroyable;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.randLenVectors;
import static mindustry.type.ItemStack.with;

public class SBlocks {

    public static Block

    //cores
    demand,
    //heaters
    heater,

    //floor
    dev, devNone,
    //turrets
    violence, slice, stab, slash, avoidance, adaptation, vengeance, siren,
    //test
    ts, undestroyable;

    public static void load() {

        new Monorail("monorail") {{
            requirements(Category.effect, with(Items.scrap, 10));
            size = 1;
            health = 100;

        }};

        new DBlock("core") {{
            requirements(Category.effect, with(Items.scrap, 99999));
            size = 5;
            health = 16000;
            armor = 8f;
        }};

        demand = new SCoreBlock("demand") {{
            requirements(Category.effect, with(Items.scrap, 10));
            size = 5;
            health = 16000;
            armor = 8f;
            unitType = SUnits.believer;
        }};

        heater = new Heater("heater") {{
            requirements(Category.effect, with(Items.scrap, 10));
            consumePower(1.5f);
            size = 2;
            health = 200;
        }};

        //test blocks
        ts = new TemperatureSource("ts1") {{
            category = Category.effect;
            buildVisibility = BuildVisibility.sandboxOnly;
            hasPower = false;
            hasItems = false;
            hasLiquids = false;
            size = 1;
            health = 20;
        }};

        undestroyable = new UnDestroyable("undestroyable");


        siren = new SirenTurret("siren") {{
            requirements(Category.turret, with(Items.scrap, 10));
            consumePower(12f);
            size = 6;
            health = 1000;
            reload = 50f;
            inaccuracy = 12;
            range = 54f * 8f;

            unitSort = UnitSorts.strongest;
            shootSound = Sounds.none;
            loopSoundVolume = 1f;
            loopSound = Sounds.laserbeam;

            shootWarmupSpeed = 0.08f;
            shootCone = 360f;

            aimChangeSpeed = 0.9f;
            rotateSpeed = 0.9f;

            shootY = 0.5f;
            outlineColor = Pal.darkOutline;
            envEnabled |= Env.space;

            shootType = new AreaLaserBullet(){{
                buildingDamageMultiplier = 0.3f;
                hitColor = Color.valueOf("fda981");

            }};
        }};

        vengeance = new ItemTurret("vengeance") {{
            size = 4;
            health = 1000;
            requirements(Category.turret, with(Items.scrap, 10));
            inaccuracy = 15;
            range = 15f * 8f;
            shoot = new ShootBarrel() {{
                shots = 19;
                shotDelay = 0.5f;
                barrels = new float[]{
                        -2f, 0f, 0f, 0f, 0f, 0f, 2f, 0f, 0f
                };
            }};

            reload = 105f;

            ammo(new Object[]{Items.copper, new BasicBulletType(5.6f, 13f) {{
                    sprite = "mine-bullet";
                    width = height = 8f;
                    layer = Layer.scorch;
                    shootEffect = smokeEffect = Fx.none;

                    backColor =  Pal.lightFlame;
                    frontColor = Color.white;
                    mixColorTo = Color.white;

                    hitSound = Sounds.plasmaboom;

                    hitSize = 22f;

                    collidesAir = false;

                    lifetime = 20f;

                    hitEffect = Fx.shootPyraFlame;
                    keepVelocity = false;

                    shrinkX = shrinkY = 0f;

                    inaccuracy = 2f;
                    weaveMag = 4f;
                    weaveScale = 3f;
                    homingPower = 0.001f;
                    homingDelay = 30f;
                    homingRange = 24f;
                    trailColor = Pal.darkPyraFlame;
                    trailWidth = 3f;
                    trailLength = 5;
                    trailEffect = Fx.fire;

                    splashDamage = 33f;
                    splashDamageRadius = 32f;

                    fragBullets = 1;

                    fragBullet = new BulletType(4f, 20f){{
                        ammoMultiplier = 6f;
                        hitSize = 7f;
                        lifetime = 18f;
                        pierce = true;
                        collidesAir = false;
                        statusDuration = 60f * 10;
                        shootEffect = Fx.shootPyraFlame;
                        hitEffect = Fx.hitFlameSmall;
                        despawnEffect = Fx.none;
                        status = StatusEffects.burning;
                        hittable = false;
                        makeFire = true;
                    }};
            }}});
        }};

        violence = new PowerTurret("violence") {{
            requirements(Category.turret, with(Items.scrap, 10));
            consumePower(4f);
            size = 6;
            health = 1000;
            reload = 50f;
            inaccuracy = 12;
            range = 54f * 8f;



            shoot = new ShootPattern() {{
                shots = 1;
                shotDelay = 3f;
            }};

            shootType = new BasicBulletType() {{
                makeFire = true;
                damage = 60f;
                speed = 18f;
                lifetime = 30f;

                pierce = true;
                pierceBuilding = true;

                trailColor = Color.valueOf("c02f2f");
                trailLength = 12;

                trailChance = 0.1f;
                trailInterval = 0.1f;
                trailWidth = 0.7f;
                trailEffect = new Effect(30.0F, (e) -> {
                    Draw.color(Color.valueOf("7a1a1a"), Color.valueOf("5a0a0a"), e.fin());
                    Angles.randLenVectors((long)e.id, 1, 2.0F + e.fin() * 9.0F, (x, y) -> {
                        Fill.circle(e.x + x, e.y + y, 0.12F + e.fslope() * 1.5F);
                    });
                    Draw.color();
                    Drawf.light(e.x, e.y, 20.0F * e.fslope(), Pal.lightFlame, 1F);
                });

                homingRange = 192;
                homingPower = 0.08f;
            }};
        }
            public void setStats() {
                super.setStats();
                this.stats.add(Stat.abilities, "[#aa2828]Насилие порождает насилие");
            }
        };

        slice = new ItemTurret("slice") {{
            range = 30f * 8f;
            inaccuracy = 2;

            requirements(Category.turret, ItemStack.with(Items.copper, 35));
            ammo(new Object[]{Items.copper, new BasicBulletType(9.4f, 34f) {{
                lifetime = 30f;

                homingDelay = 4f;
                homingPower = 0.1f;
                homingRange = 28f;

                trailColor = Color.valueOf("ffcd59");
                trailLength = 12;
                trailWidth = 1.35f;
                trailSinScl = 2.5f;
                trailSinMag = 0.5f;
                trailEffect = Fx.none;

                fragRandomSpread = 0f;
                fragSpread = 0f;
                fragAngle = 0f;
                fragBullets = 1;
                fragBullet = new LaserBulletType() {{
                    colors = new Color[]{Color.valueOf("ffcd59"), Color.valueOf("fff18c"), Color.white};
                    damage = 26f;
                    buildingDamageMultiplier = 0.25F;
                    width = 16.5f;
                    hitEffect = Fx.hitLancer;
                    sideAngle = 15f;
                    sideWidth = 0.8f;
                    sideLength = 20f;
                    lifetime = 22f;
                    drawSize = 200f;
                    length = 60f;
                    pierceCap = 2;
                }};


                }}
            });

            size = 2;

            shoot = new ShootPattern() {{
                shots = 1;
                shotDelay = 12f;
                reload = 48f;
            }
                public void shoot(int totalShots, BulletHandler handler) {
                    handler.shoot(0.0F, 0.0F, 0.0F - 10f, this.firstShotDelay + this.shotDelay);
                    handler.shoot(0.0F, 0.0F, 0.0F + 10f, this.firstShotDelay + this.shotDelay);
                }
            };

        }};


        avoidance = new SItemTurret("avoidance") {{
            requirements(Category.turret, ItemStack.with(Items.copper, 35));
            ammo(Items.copper, new ArtilleryBulletType(2.1f, 34f) {{
                lifetime = 287f;
                scaleLife = false;
                width = height = 11f;

                trailColor = Color.valueOf("8ca3c4");
                trailLength = 10;
                trailWidth = 2.6f;
                trailSinScl = 2.5f;
                trailSinMag = 0.37f;

                frontColor = Color.valueOf("a8ccdb");
                backColor = Color.valueOf("6a83bd");


                trailEffect = new Effect(50.0F, (e) -> {
                    Draw.color(backColor);
                    Draw.z(Layer.bullet - 1);
                    Fill.circle(e.x, e.y, e.rotation * e.fout());
                });

                collidesTiles = true;
                collidesGround = true;
                collides = true;

                ammoMultiplier = 1f;

                homingPower = 0.005f;
                homingRange = 120f;

                weaveMag = 0.3f;
                weaveScale = 1f;
                knockback = 0.8f;

                hitEffect = despawnEffect = new ExplosionEffect(){{
                    lifetime = 50f;
                    waveStroke = 0;
                    waveLife = 0;
                    waveColor = Color.clear;
                    sparkColor = smokeColor = backColor;
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
                @Override
                public boolean testCollision(Bullet bullet, Building tile) {
                    return !(tile.block instanceof Wall) && super.testCollision(bullet, tile);
                }
            });

            size = 4;
            targetAir = false;
            recoil = 2f;
            range = 540f;
            inaccuracy = 3f;
            shootCone = 10f;
            health = 1200;
            shootSound = Sounds.bang;
            shoot = new ShootBarrel() {{
                barrels = new float[]{
                        -10f, 0f, 0f, 10f, 0f, 0f
                };
                shots = 14;
                shotDelay = 6.8f;
                reload = 350f;
            }};
        }};

        slash = new ItemTurret("slash") {{
            requirements(Category.turret, ItemStack.with(Items.copper, 35));
            size = 4;
            recoil = 2f;
            range = 540f;
            inaccuracy = 3f;
            shootCone = 10f;
            health = 1200;
            shoot = new ShootBarrel() {{

            }};
        }};


        adaptation = new MachineGunTurret("adaptation") {{
            requirements(Category.turret, ItemStack.with(Items.copper, 35));
            ammo(Items.copper, new BasicBulletType(13f, 44f) {{
                reload = 90f;
                lifetime = 15f;
                drag = 0.01f;
                scaleLife = false;
                width = height = 4f;


                trailColor = Pal.missileYellow;
                trailLength = 22;
                trailWidth = 0.8f;
                trailSinScl = 1.1f;
                trailSinMag = 0.37f;

                frontColor = Pal.bulletYellow;
                backColor = Pal.bulletYellowBack;

                despawnEffect = hitEffect = new Effect(60, e -> {
                    color(Pal.missileYellow, e.color, e.fin());
                    stroke(1.9f * e.fout());
                    randLenVectors(e.id, 4, 128f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 9f, (x, y) -> {
                        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 5f);
                    });

                    stroke(1.3f * (e.fout() - 0.2f));
                    circle(e.x, e.y, 0.4f);
                });

                trailEffect = new Effect(50.0F, (e) -> {
                    Draw.color(backColor);
                    Draw.z(Layer.bullet - 1);
                    Fill.circle(e.x, e.y, e.rotation * e.fout());
                });

                ammoMultiplier = 1f;

                homingPower = 0.002f;
                homingRange = 32f;

                weaveMag = 0.3f;
                weaveScale = 1f;
                knockback = 0.8f;

                pierce = true;
                pierceBuilding = true;
                pierceCap = 2;

                fragAngle = 4f;
                fragBullets = 2;
                fragSpread = 3f;
                fragRandomSpread = 4f;
                fragBullet = new BasicBulletType(11f, 12f) {{
                    lifetime = 8f;


                    trailColor = Pal.missileYellow;
                    trailLength = 12;
                    trailWidth = 0.6f;
                    trailSinScl = 1f;
                    trailSinMag = 0.31f;

                    frontColor = Pal.bulletYellow;
                    backColor = Pal.bulletYellowBack;

                    despawnEffect = hitEffect = new Effect(50, e -> {
                        color(Pal.missileYellow, e.color, e.fin());
                        stroke(1.9f * e.fout());
                        randLenVectors(e.id, 2, 100f * e.fin(), e.rotation+Mathf.randomSeed(e.id,-3f, 3f), 9f, (x, y) -> {
                            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 5f);
                        });

                        stroke(1.2f * (e.fout() - 0.2f));
                        circle(e.x, e.y, 0.3f);
                    });

                    trailEffect = new Effect(50.0F, (e) -> {
                        Draw.color(backColor);
                        Draw.z(Layer.bullet - 1);
                        Fill.circle(e.x, e.y, e.rotation * e.fout());
                    });

                    ammoMultiplier = 1f;

                    homingPower = 0.005f;
                    homingRange = 12f;

                    weaveMag = 0.3f;
                    weaveScale = 1f;
                    knockback = 0.8f;
                }};
            }});

            size = 4;
            targetAir = false;
            recoil = 2f;
            range = 540f;
            inaccuracy = 3f;
            shootCone = 10f;
            health = 1200;
            shootSound = Sounds.bang;
        }};





//        new WallDrill("WaDR-22") {{
//            requirements(Category.effect, with(Items.sporePod, 69));
//            size = 3;
//            rotators = 3;
//            rotatorsSideSpace = 0.8f;
//            health = 96;
//            tier = 5;
//            drillTime = 60 * 2.5f * 2f;
//
//            consumeLiquid(Liquids.water, 0.25f / 60f).boost();
//        }};


        new Floor("corite") {{
            size = 1;
            variants = 3;
        }};


        new StaticWall("corite-wall") {{
            size = 1;
            variants = 2;
        }};

        new Floor("Femmanite") {{
            size = 1;
            variants = 3;
        }};

        dev = new Floor("dev") {{
            size = 1;
            variants = 5;
        }};

        new Floor("none") {{
            size = 1;
            variants = 1;
        }};

        devNone = new Floor("test") {{
            size = 1;
            variants = 1;
        }};

        new StaticWall("Femmanite-wall") {{
            size = 1;
            variants = 2;
        }};

        new Prop("Femmanite-boulder") {{
            size = 1;
            variants = 2;
        }};

        new Floor("Femmanite-ice") {{
            size = 1;
            variants = 3;
        }};

        new StaticWall("Femmanite-ice-wall") {{
            size = 1;
            variants = 2;
        }};

        new Floor("i") {{
            size = 1;
            variants = 2;
        }};
        new Floor("s") {{
            size = 1;
            variants = 2;
        }};
        new Ice("t") {{
            size = 1;
            variants = 3;
        }};
        new StaticWall("w") {{
            size = 1;
            variants = 2;
        }};
    }
}
