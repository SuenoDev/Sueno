package org.durmiendo.sueno.core;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.io.SaveVersion;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.graphics.NTexture;
import org.durmiendo.sueno.graphics.SBlockRenderer;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.processors.SuenoInputProcessor;
import org.durmiendo.sueno.settings.SettingsBuilder;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.temperature.TemperatureCustomChunk;
import org.durmiendo.sueno.utils.SLog;

import java.lang.reflect.Field;

public class Setter {
    public static void load() {
        SLog.mark();

        SLog.loadTime(Setter::loadVars, "vars");
        SLog.loadTime(Setter::loadControllers, "controllers");
        SLog.loadTime(Setter::loadUI, "ui");
        SLog.loadTime(Setter::loadRender, "renderer");
        SLog.loadTime(Setter::loadChunks, "chunks");
        SLog.loadTime(Setter::loadCompatibility, "compatibility");

        SLog.loadTime(SettingsBuilder::uiBuild, "settings");

        SLog.elapsedInfo("load settings finished");
    }

    private static void loadCompatibility() {
        String ver = "sueno-version";
        String bld = "sueno-builder";
        String oldVersion = Core.settings.getString(ver, "null");
        String oldBuilder = Core.settings.getString(bld, "null");

        SLog.load("oldVer " + oldVersion);
        SLog.load("oldBuilder " + oldBuilder);

        String[] builder = SVars.internalFileTree.child("data/builder").readString().split("/");
        String currentVersion = Vars.mods.getMod(Sueno.class).meta.version + "/" + builder[1];
        SLog.load("curVer " + currentVersion);

        Core.settings.put(bld, builder[0]);
        SLog.load("curBuilder " + builder[0]);

        SLog.load("verWarning " + SVars.versionWarning);

        Core.settings.put(ver, currentVersion);
        Core.settings.saveValues();


        if (!((oldVersion.equals(currentVersion) || oldVersion.equals("null")) && (oldBuilder.equals(builder[0]) || oldBuilder.equals("null")))) {
            SLog.load("ver or builder do not match!");
            if (SVars.versionWarning) {
                Events.on(EventType.ClientLoadEvent.class,
                        e -> Time.runTask(10f,
                                () -> SVars.ui.warning(
                                        "Attention, backward compatibility may not be supported in different versions and builders",
                                        3f
                                )
                        )
                );
            } else {
                SLog.load("attetion dont show, user ");
            }
        }
    }

    private static void loadControllers() {
        SLog.load("touch input processor");
        SVars.input = new SuenoInputProcessor();
        Core.input.addProcessor(SVars.input);


        SLog.load("celestial body controller (not used)");
        SVars.celestialBodyController = new CelestialBodyController();

//        SLog.load("status effects controller");
//        SVars.statusEffectsController = new StatusEffectsController();

        SLog.load("void strider collapse effect controller");
        VoidStriderCollapseEffectController.init();
    }

    private static void loadChunks() {
        SLog.load("temperature custom chunk");
        SaveVersion.addCustomChunk("sueno-temperature-chunk", new TemperatureCustomChunk());
    }

    private static void loadRender() {
        try {
            Field f = Vars.renderer.getClass().getDeclaredField("blocks");
            f.setAccessible(true);
            f.set(Vars.renderer, new SBlockRenderer());
        } catch (Exception e) {
            SLog.err("Renderer dont load :(");
//            throw new RuntimeException(e);
        }

        Events.on(EventType.ClientLoadEvent.class, e -> {
            SLog.loadTime(() -> {
                final int[] loaded = {0};
                Core.atlas.getRegionMap().each((s, atlasRegion) -> {
                    TextureAtlas.AtlasRegion n = Core.atlas.find(s + "-normal");
                    if (Core.atlas.isFound(n)) {
                        loaded[0]++;
                        SLog.load("normal texture, founded: " + s);
                        n.texture = new NTexture(atlasRegion.texture, n.texture);
                    }
                });
                SLog.info(loaded[0] + " normal textures loaded!");
                Draw.shader(SShaders.normalShader, true);
            }, "normal texture load");
        });

        Shader[] last = new Shader[1];

        Events.run(EventType.Trigger.drawOver, () -> {
//            last[0] = Draw.getShader();

        });

        Events.run(EventType.Trigger.uiDrawBegin, () -> Draw.shader(last[0]));




//        SLog.load("test shader");
//        Events.run(EventType.Trigger.drawOver, () -> {
//            Draw.drawRange(Layer.flyingUnit, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
//                Vars.renderer.effectBuffer.end();
//                Vars.renderer.effectBuffer.blit(SShaders.blackHoleShader);
//            });
//        });
//        SLog.load("void space shader");
//        Events.run(EventType.Trigger.drawOver, () -> {
//            Draw.drawRange(SLayers.voidspace, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
//                Vars.renderer.effectBuffer.end();
//                Vars.renderer.effectBuffer.blit(SShaders.voidSpaceShader);
//            });
//        });
//
//        SLog.load("dead zone shader");
//        Events.run(EventType.Trigger.drawOver, () -> {
//            Draw.drawRange(SLayers.deadZone, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
//                Vars.renderer.effectBuffer.end();
//                Vars.renderer.effectBuffer.blit(SShaders.deadShader);
//            });
//
//
//        });

//
//        SLog.load("normals");

//        fb = new FrameBuffer();
//        nb = new FrameBuffer();
//        rnb = new FrameBuffer();
//        fb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//        nb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//        rnb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//        Events.on(EventType.ResizeEvent.class, e -> {
//            fb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//            nb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//            rnb.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
//        });
//
//        Events.run(EventType.Trigger.draw, () -> {
//            Draw.draw(Layer.min, () -> {
//                fb.begin(Color.clear);
//            });
//
//            Draw.draw(Layer.light-0.1f,() -> {
//                fb.end();
//                fb.blit(Shaders.screenspace);
//            });

//            Draw.drawRange(Layer.light+1, () -> {
//                rnb.begin(Color.black);
//            }, () -> {
//                rnb.end();
//            });

//            Draw.drawRange(Layer.light+2, () -> {
//                nb.begin(Color.clear);
//            }, () -> {
//                nb.end();
//                nb.blit(SShaders.normalShader);
//            });
//        });
    }

    private static void loadVars() {
        SLog.load("temperature controller hooks");
        Events.on(EventType.WorldLoadBeginEvent.class, e -> {
            if (SVars.temperatureController == null) SVars.temperatureController = new TemperatureController();
            SVars.temperatureController.init(Vars.world.width(), Vars.world.height());
        });

        SLog.load("Setting maxZoom to 5x");
        Vars.renderer.maxZoom *= 5f;

        SLog.load("Setting minZoom to x/5");
        Vars.renderer.minZoom /= 5f;
    }

    private static void loadUI() {
        SLog.load("ui");
        SVars.ui.build();
    }
}