package org.durmiendo.sueno.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
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
import mindustry.world.meta.Stat;
import org.durmiendo.sueno.entities.bullet.CopyBulletType;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemperatureSource;
import org.durmiendo.sueno.world.blocks.defense.turrets.SItemTurret;
import org.durmiendo.sueno.world.blocks.environment.Ice;
import org.durmiendo.sueno.world.blocks.storage.SCoreBlock;
import org.durmiendo.sueno.world.blocks.walls.UnDestroyable;

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
    violence, slice, stab, slash, avoidance,
    //test
    ts, undestroyable;
    public static void load() {

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

            shootType = new CopyBulletType() {{

                makeFire = true;
                damage = 0f;
                speed = 30f;
                lifetime = 16f;

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
                homingPower = 0.04f;
                homingDelay = 2.1f;
            }

                @Override
                public void update(Bullet b) {
                    super.update(b);
                    if (Mathf.random(0f, 1f) > 0.9f) {
                        b.vel.setAngle(b.vel.angle() + Mathf.random(-12, 12));
                    }
                }
            };
        }
            public void setStats() {
                super.setStats();
                this.stats.add(Stat.abilities, "[#aa2828]Насилие порождает насилие");
            }
        };

        slice = new ItemTurret("slice") {{
            range = 30f * 8f;
            inaccuracy = 2;

            requirements(Category.turret, ItemStack.with(new Object[]{Items.copper, 35}));
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
                trailWidth = 2.75f;
                trailSinScl = 2.5f;
                trailSinMag = 0.5f;

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
