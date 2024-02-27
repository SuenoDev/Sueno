package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.struct.Seq;
import mindustry.game.EventType;
import org.durmiendo.sueno.ui.scene.BufferRegionDrawable;

public class VoidStriderCollapseEffectController {
    public static FrameBuffer effectsBuffer = new FrameBuffer();
    public static FrameBuffer screenBuffer = new FrameBuffer();

    public static Seq<SEffect.Container> containers = new Seq<>();
    public static BufferRegionDrawable uiDrawable;

    public static void init() {
        Events.run(EventType.Trigger.preDraw, () -> {
            screenBuffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
            screenBuffer.begin(Color.clear);
        });
        Events.run(EventType.Trigger.postDraw, () -> {
            screenBuffer.end();
            draw();
        });
    }

    public static void at(float x, float y, SEffect effect) {
        containers.add(effect.at(x, y));
    }

    public static void draw() {
        Draw.sort(false);
        Draw.proj(Core.camera);

        effectsBuffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        if (uiDrawable != null)
            uiDrawable.bufferSizeChanged();
        effectsBuffer.begin(Color.clear);

        for (int i = 0; i < containers.size; i++) {
            if (containers.get(i).render()) {
                containers.remove(i);
                i--;
            }
        }

        Draw.reset();
        Draw.flush();
        effectsBuffer.end();

        Draw.blit(SShaders.voidStriderCollapseEffectShader);
    }
}
