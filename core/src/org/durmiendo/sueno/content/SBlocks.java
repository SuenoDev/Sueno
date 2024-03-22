package org.durmiendo.sueno.content;

import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.Prop;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.meta.BuildVisibility;
import org.durmiendo.sueno.world.blocks.Heater;
import org.durmiendo.sueno.world.blocks.TemperatureSource;
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
    //test
    ts, undestroyable;
    public static void load() {
        demand = new SCoreBlock("demand") {{
            requirements(Category.effect, with(Items.scrap, 10));
            size = 5;
            health = 16000;
            armor = 8f;
            unitType = UnitTypes.gamma;
        }

        };
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
