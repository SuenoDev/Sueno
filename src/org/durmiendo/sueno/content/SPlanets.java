package org.durmiendo.sueno.content;


import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.*;
import mindustry.type.Planet;
import org.durmiendo.sueno.graphics.g3d.Obj;
import org.durmiendo.sueno.graphics.g3d.ObjParser;
import org.durmiendo.sueno.maps.planet.HieloPlanetGenerator;


public class SPlanets {
    public static Planet hielo, sun;

    public static void load() {
        sun = new Planet("ssuunce", null, 2) {{
            bloom = true;
            accessible = false;

            meshLoader = () -> new SunMesh(
                    this, 4,
                    5, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ff7a38"),
                    Color.valueOf("ff9638"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffe371"),
                    Color.valueOf("f4ee8e")
            );
        }};
        hielo = new Planet("hielo", sun, 1f, 2) {
            final Seq<Obj> obj = new Seq<>();
            {
                generator = new HieloPlanetGenerator();
                meshLoader = () -> new HexMesh(this, 5);

                cloudMeshLoader = () -> new MultiMesh(
                        new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("77dde7").a(0.12f), 3, 0.42f, 1f, 0.43f),
                        new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("003153").a(0.1f), 3, 0.42f, 1.2f, 0.45f)
                );
                orbitRadius = 40f;

                launchCapacityMultiplier = 0.5f;
                sectorSeed = 2;
                allowWaves = true;
                allowWaveSimulation = true;
                allowSectorInvasion = true;
                allowLaunchSchematics = false;
                enemyCoreSpawnReplace = true;
                allowLaunchLoadout = true;
                prebuildBase = false;
                ruleSetter = r -> {
                    r.waveTeam = Team.crux;
                    r.placeRangeCheck = false;
                    r.showSpawns = false;
                };
                iconColor = Color.valueOf("aaaaff");
                atmosphereColor = Color.valueOf("060f16");
                atmosphereRadIn = 0.02f;
                atmosphereRadOut = 0.3f;
                startSector = 15;
                alwaysUnlocked = true;
                accessible = true;
                visible = true;
                landCloudColor = Pal.spore.cpy().a(0.5f);
                hiddenItems.removeAll(Items.erekirItems);
                hiddenItems.removeAll(Items.serpuloItems);


                obj.add(ObjParser.load("hu/hu"));
                obj.add(ObjParser.load("mita/mita"));
            }



            public Vec3 lightDir = new Vec3(1, 1, 1).nor();
            public Color ambientColor = Color.white.cpy();
            public Vec3 camDir = new Vec3();
//            public Vec3 camPos = new Vec3();

            int ta = -1;

            Vec3[] rs = new Vec3[]{
                    new Vec3(0,0, 0),
                    new Vec3(0, Mathf.PI, 0),
            };

            Vec3[] ps = new Vec3[]{
                    new Vec3(position).add(0f, -0.5f, 2f),
                    new Vec3(position).add(0f, -0.5f, -2f),
            };

            float[] scl = new  float[]{
                    2f,
                    0.8f
            };

            float r = 0;
            @Override
            public void draw(PlanetParams params, Mat3D projection, Mat3D transform) {
                super.draw(params, projection, transform);
                r += Time.delta;
                r %= 360f;
                Vars.renderer.planets.cam.near = 0.1f;
//                t[0] = cloth;
//                t[1] = cloth;
//                t[2] = hair;
//                t[3] = face;
//                t[4] = cloth;
//                t[5] = body;
//                t[6] = cloth;
//                t[7] = cloth;
//                t[7] = cloth;

//                SShaders.g3d.bind();
//                SShaders.g3d.setUniformMatrix4("u_proj", projection.val);
//                SShaders.g3d.setUniformMatrix4("u_trans", transform.val);
                camDir.set(Vars.renderer.planets.cam.direction).rotate(Vec3.Y, getRotation());





                for (int i = 0; i < obj.size; i++) {
                    Obj o = obj.get(i);
                    ps[i].x = Mathf.cosDeg(r + 180 * i) * 2f;
                    ps[i].z = Mathf.sinDeg(r + 180 * i) * 2f;
                    rs[i].y = Mathf.degRad * (r + 270f + i*180f);
                    o.render(ps[i], rs[i], projection, transform, Vars.renderer.planets.cam, lightDir, scl[i], ta);
                }
//                SShaders.g3d.setUniformf("u_lightdir", lightDir);
//                SShaders.g3d.setUniformf("u_ambientColor", ambientColor.r, ambientColor.g, ambientColor.b);
//                SShaders.g3d.setUniformf("u_camdir", camDir);
//                SShaders.g3d.setUniformf("u_rotation", 20f);
//                SShaders.g3d.setUniformf("u_campos", Vars.renderer.planets.cam.position);
//                SShaders.g3d.setUniformf("u_pos", 2, 0, 4, 0);
//
//                SShaders.g3d.setUniformf("u_color", 0.4f, 0.4f, 0.4f, 1);


//                texture.bind();
//                am.render(SShaders.g3d, Gl.triangles);

//                if (ms != null) {
//                    for (int i = 0; i < ms.size; i++) {
//                        t[i].bind();
//                        ms.get(i).render(SShaders.g3d, Gl.triangles);
//                    }
//                }
           }
        };
    }
}

