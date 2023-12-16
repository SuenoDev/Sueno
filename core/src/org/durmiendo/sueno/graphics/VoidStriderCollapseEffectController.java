package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.game.EventType;
import org.durmiendo.sueno.core.SVars;

public class VoidStriderCollapseEffectController {
    public static Seq<Effect.EffectContainer> containers = new Seq<>();
    public static FrameBuffer buffer = new FrameBuffer();
    public static TextureRegion collapseCircle;

    static {
        Events.run(EventType.Trigger.postDraw, VoidStriderCollapseEffectController::draw);
    }

    public static void at(float x, float y, float lifeTime, float range) {
        Effect.EffectContainer container = new Effect.EffectContainer();
        container.x = x;
        container.y = y;
        container.lifetime = lifeTime;
        container.data = range;
        containers.add(container);
    }

    public static void draw() {
        Draw.sort(false);

        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        buffer.begin(Color.clear);
        Draw.proj(Core.camera);

        for (Effect.EffectContainer container : containers) {
            container.time += Time.delta;

            if (container.time >= container.lifetime) {
                containers.remove(container);
                continue;
            }

            if (collapseCircle == null)
                collapseCircle = SVars.core.getRegion("void-strider-collapse-effect");

            float size = container.fin() * (float) container.data;
            Draw.rect(collapseCircle, container.x, container.y, size, size);
        }

        Draw.flush();
        buffer.end();

        /*Gl.bindTexture(Gl.texture2d, 1);
        Gl.copyTexImage2D(Gl.texture2d, 0, Gl.rgba, 0, 0, Core.graphics.getWidth(), Core.graphics.getHeight(), 0);*/

        Draw.sort(true);

        buffer.blit(SShaders.voidStriderCollapseEffectShader);

        Draw.rect(SVars.core.getRegion("void-strider-collapse-effect"), Vars.player.x, Vars.player.y, 32, 32);
        Draw.rect(new TextureRegion(buffer.getTexture()), Core.camera.position.x, Core.camera.position.y, Core.camera.width, Core.camera.height);

        Draw.flush();

        //Draw.sort(true);
    }
}
