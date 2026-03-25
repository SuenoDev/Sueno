package org.durmiendo.sueno.graphics.g3d;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
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
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.graphics.g3d.wobj.Obj;
import org.durmiendo.sueno.graphics.g3d.wobj.ObjParser;

import java.lang.reflect.Field;

public class S3Renderer implements Disposable {
    public Camera3D cam;
    public FrameBuffer buffer;

//    Obj ohno = ObjParser.loadObj("bat/models_accum", 2f/5f);

    public ObjectMap<Block, Obj> objs = new ObjectMap<>(){{
        put(SBlocks.mita, ObjParser.loadObj("mita/mita", new Vec3(1f/5f,1f/5f,1f/5f)));
        put(SBlocks.demand, ObjParser.loadObj("core/untitled1", new Vec3(0.1f,0.1f,0.1f)));
    }};

    public ObjectMap<Building, Builds> builds = new ObjectMap<>();

    Mat projection = new Mat();
    Mat transformation = new Mat();

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
        Events.run(EventType.Trigger.preDraw, this::render);
        TextureRegion r = new TextureRegion();
        r.set(buffer.getTexture());
        Events.run(EventType.Trigger.draw, () -> {
            Draw.z(36);
            Draw.color(Color.white);
            Draw.rect(r, Core.camera.position.x, Core.camera.position.y, Core.graphics.getWidth() / Vars.renderer.getDisplayScale(), Core.graphics.getHeight() / Vars.renderer.getDisplayScale());
            Draw.color();
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
    public Vec3 lights = new Vec3(0.5f, -0.5f, -1f).nor();
    
    public Mat3D mtmp = new Mat3D();
    public Mat3D mtmp2 = new Mat3D();
    
    public void render() {
        Draw.flush();
        
        cam.up.set(Vec3.Y);
        projection.setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        transformation.idt();
        
        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.position.set(0, 0, 0);
        cam.direction.set(0, 0, -1f);
        cam.lookAt(0, 0f, -1);
        cam.update();
        
        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        
        buffer.begin(Color.clear);
        
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);
        
        Gl.disable(Gl.cullFace);
        
        Shader sh = SShaders.g3d;
        sh.bind();
        sh.setUniformf("u_campos", cam.position.x, cam.position.y, cam.position.z);
        sh.setUniformf("u_lightdir", lights.x, lights.y, lights.z);
        
        baseRender();
        
        buffer.end();
        
        Gl.disable(Gl.depthTest);
        Gl.depthMask(false);
        
        Gl.activeTexture(Gl.texture0);
        if(Core.atlas != null && Core.atlas.texture() != null) {
            Core.atlas.texture().bind(0);
        }
        
        Draw.flush();
    }
    
    void baseRender() {
        mtmp2.idt(); // View matrix (identity, так как всё делает cam.combined)
        
        for (ObjectMap.Entry<Building, Builds> b : builds) {
            if (!b.key.isAdded()) {
                builds.remove(b.key);
                continue;
            }
            
            // Берем РЕАЛЬНЫЕ мировые координаты тайла
            tmp.set(b.value.build.x, b.value.build.y, 0f);
            
            // ВАЖНО: Mat3D в Arc использует ГРАДУСЫ, а не радианы!
            // -90f поворачивает модель так, чтобы она стояла на земле (если в Blender ось Z - верх)
            tmp2.set(-90f, b.value.build.rotdeg(), 0f);
            
            // Используем статический шейдер, который внутри Obj
            b.value.obj.render(tmp, tmp2, cam.combined, mtmp2, cam, lights);
        }
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

