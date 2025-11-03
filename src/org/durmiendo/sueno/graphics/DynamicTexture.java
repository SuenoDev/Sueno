package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Disposable;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.graphics.Shaders;
import org.durmiendo.sueno.core.SVars;

public class DynamicTexture implements Disposable {
    public static Seq<DynamicTexture> all = new Seq<>();
    
    public static DynamicTexture create(int width, int height, String pathToShader, Cons<Shader> uniformSets) {
        DynamicTexture dyn = new DynamicTexture(
                width,
                height,
                null
        );
        dyn.apply = uniformSets;
        dyn.shader = new Shader(
                Shaders.getShaderFi("screenspace.vert"),
                SVars.internalFileTree.child(pathToShader)
        ){
            @Override
            public void apply() {
                dyn.apply.get(this);
            }
        };
        return dyn;
    }
    
    static {
        Events.run(EventType.Trigger.draw, () -> {
            all.each(DynamicTexture::preDraw);
        });
    }
    
    public int width, height, realWidth, realHeight;
    public FrameBuffer frameBuffer;
    public Shader shader;
    public Cons<Shader> apply = s -> {};
    
    private boolean itDrawn;
    private final TextureRegion textureRegion;
    
    private DynamicTexture(int width, int height, Shader shader) {
        this.width = width;
        this.height = height;
        this.shader = shader;
        
        frameBuffer = new FrameBuffer(width, height, true);
        textureRegion = new TextureRegion(frameBuffer.getTexture(), width, height);
        
        
        setResolution();
        
        all.add(this);
    }
    
    public void setResolution() {
        realWidth = Mathf.round(width * SVars.getQuality());
        realHeight = Mathf.round(height * SVars.getQuality());
        
        frameBuffer.resize(realWidth, realHeight);
        
        textureRegion.setWidth(width);
        textureRegion.setHeight(height);
    }
    
    public void preDraw() {
        itDrawn = false;
        draw();
    }
    
    public TextureRegion getTextureRegion() {
        draw();
        return textureRegion;
    }
    
    public void draw() {
        if (!itDrawn) {
            forceDraw();
            itDrawn = true;
        }
    }
    
    public void forceDraw() {
        Draw.flush();
        Draw.reset();
        
        setResolution();
        
        shader.apply();
        frameBuffer.begin(Color.clear);
        
        Draw.blit(shader);
        Draw.flush();
        
        frameBuffer.end();
        textureRegion.set(frameBuffer.getTexture());
    }
    
    @Override
    public void dispose() {
        frameBuffer.dispose();
    }
}