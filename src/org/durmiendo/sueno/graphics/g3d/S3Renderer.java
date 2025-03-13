package org.durmiendo.sueno.graphics.g3d;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.SpriteBatch;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.g3d.Camera3D;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.ObjectMap;
import arc.util.Disposable;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SBlocks;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;

public class S3Renderer implements Disposable {
    public Camera3D cam;
    public FrameBuffer buffer;

    private static final float[] vertices = new float[SpriteBatch.SPRITE_SIZE];

    Obj ohno = ObjParser.load("bat/models_accum", 2f/5f);

    public ObjectMap<Block, Obj> objs = new ObjectMap<>(){{
        put(SBlocks.mita, ohno);
    }};

    public ObjectMap<Building, Builds> builds = new ObjectMap<>();

    Mat projection = new Mat();
    Mat transformation = new Mat();
    Vec3 translation = new Vec3();

    public Shader shader = new Shader(
            SVars.internalFileTree.child("shaders/3d.vert"),
            SVars.internalFileTree.child("shaders/3d.frag")
    );

    Field colorPacked;
    Field mixColorPacked;

    public S3Renderer(){
        init();

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            builds.clear();
            Vars.world.tiles.eachTile(t -> {
                Block block = t.block();
                Obj obj = objs.get(block);
                Building building = t.build;

                if (obj != null && building != null) {
                    Builds b = new Builds(obj, block, building);
                    builds.put(building, b);
                }
            });
        });

        Events.on(EventType.BlockBuildEndEvent.class, e -> {
            Block block = e.tile.block();
            Obj obj = objs.get(block);
            Building building = e.tile.build;

            if (obj != null && building != null) {
                Builds b = new Builds(obj, block, building);
                builds.put(building, b);
            }
        });



        Events.on(EventType.ClientLoadEvent.class, e -> {
            try {
                colorPacked = Core.batch.getClass().getDeclaredField("colorPacked");
                colorPacked.setAccessible(true);
                mixColorPacked = Core.batch.getClass().getDeclaredField("mixColorPacked");
                mixColorPacked.setAccessible(true);
                Log.info("color");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        TextureRegion r = new TextureRegion();

        Events.run(EventType.Trigger.preDraw, this::render);
        Events.run(EventType.Trigger.draw, () -> {
            Draw.z(36);
//            Draw.rect();
//            Draw.blit(buffer.getTexture(), Shaders.screenspace);
            r.set(buffer.getTexture());
            Draw.rect(r, Core.camera.position.x, Core.camera.position.y, Core.graphics.getWidth() / Vars.renderer.getDisplayScale(), Core.graphics.getHeight() / Vars.renderer.getDisplayScale());
        });
    }

    public void init() {
        cam = new Camera3D();
        cam.fov = 100;
        cam.near = 0.01f;
        cam.far = 10000;

        buffer = new FrameBuffer(2, 2, true);
    }

    public Vec3 camDir = new Vec3(0f, -1f, 0f);
    public Vec3 tmp = new Vec3(0f, 0f, 0f);
    public Vec3 tmp2 = new Vec3(0f, 0f, 0f);
    public Vec3 tmp3 = new Vec3(0f, 0f, 0f);
    public Vec3 tmp4 = new Vec3(0f, 0f, 0f);
    public Vec3 lights = new Vec3(0f, 0, 1f);

    public Mat3D mtmp = new Mat3D();
    public Mat3D mtmp2 = new Mat3D();

    public void render() {
        Draw.flush();
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);

        Gl.enable(Gl.cullFace);
        Gl.cullFace(Gl.back);

        cam.up.set(Vec3.Y);

        projection.setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        transformation.idt();

        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//        cam.position.set(Core.camera.position.x, Core.camera.position.y, Vars.renderer.getDisplayScale());
        cam.position.set(0, 0, 0);
        cam.direction.set(0, 0,-1f);
        cam.lookAt(0, 0f, -1);
//        cam.lookAt(Core.camera.position.x, 0f, Core.camera.position.y);
//        cam.position.set(Core.camera.position.x, Core.camera.position.y, Vars.renderer.getDisplayScale()+4f);

        cam.update();

        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());


        buffer.begin(Color.clear);
        shader.bind();

        baseRender();

        buffer.end();

        Gl.disable(Gl.cullFace);
        Gl.disable(Gl.depthTest);
        Gl.depthMask(false);

        Draw.blit(buffer, shader);
//        Draw.flush();
    }

    Vec3 scale = new Vec3(1f, 1f, 1f).scl(2f);

    void baseRender() {
        mtmp2.set(transformation);


        for (ObjectMap.Entry<Building, Builds> b : builds) {
            if (!b.key.isAdded()) {
                builds.remove(b.key);
                continue;
            }
            scale.set(1f, 1f, 1f).scl(1/15f);
            tmp2.set(-Mathf.PI/2f,b.value.build.rotdeg()*Mathf.degRad, 0f);
            tmp4.set(b.value.build.getX(), b.value.build.getY(), 0f);

            tmp.set(tmp4);
            tmp.sub(Core.camera.position.x, Core.camera.position.y, 0);
            tmp.y*=-1f;

            tmp.scl(1f/310f);
            tmp.z = -1f/Vars.renderer.getDisplayScale();

            b.value.obj.render(tmp, tmp2, cam.combined, mtmp2, cam, lights, scale);
        }


//        scale.set(1f, 1f, 1f).scl(1/12f);
//        tmp2.set(-Mathf.PI/2f,r*Mathf.degRad, 0f);
//        tmp4.set(400f,400f, 0f);
//
//        tmp.set(tmp4);
//        tmp.sub(Core.camera.position.x, Core.camera.position.y, 0);
//        tmp.y*=-1f;
//
//        tmp.scl(1f/320f);
//        tmp.z = -1f/Vars.renderer.getDisplayScale();
//
//        ohno.render(tmp, tmp2, cam.combined, mtmp2, cam, lights, scale);
    }

    public void dispose() {
        if (shader != null) {
            shader.dispose();
        }
    }

    public static class Builds {
        public Obj obj;
        public Block block;
        public Building build;

        public Builds(Obj obj, Block block, Building build) {
            this.obj = obj;
            this.block = block;
            this.build = build;
        }
    }
}

