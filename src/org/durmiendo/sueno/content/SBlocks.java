package org.durmiendo.sueno.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.FlakBulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Bullet;
import mindustry.gen.Icon;
import mindustry.gen.PayloadUnit;
import mindustry.gen.Teamc;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.StatusEffect;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.BaseTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.ReloadTurret;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.Prop;
import mindustry.world.blocks.environment.ShallowLiquid;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.entities.bullet.CopyBulletType;
import org.durmiendo.sueno.utils.SLog;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemperatureSource;
import org.durmiendo.sueno.world.blocks.environment.Ice;
import org.durmiendo.sueno.world.blocks.storage.SCoreBlock;
import org.durmiendo.sueno.world.blocks.walls.UnDestroyable;

import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.StatValues.ammo;

public class SBlocks {

    public static Block

    //cores
    demand,
    //heaters
    heater,

    //floor
    dev, devNone,
    //turrets
    violence,
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

            shootType = new BasicBulletType() {{

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
