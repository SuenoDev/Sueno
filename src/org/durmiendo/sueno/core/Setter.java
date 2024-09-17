package org.durmiendo.sueno.core;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.io.SaveVersion;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.processors.SuenoInputProcessor;
import org.durmiendo.sueno.settings.SettingsBuilder;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.temperature.TemperatureCustomChunk;
import org.durmiendo.sueno.utils.SLog;

public class Setter {
    public static void load() {
        SLog.mark();

        SLog.loadTime(Setter::loadVars, "vars");
        SLog.loadTime(Setter::loadControllers, "controllers");
        SLog.loadTime(Setter::loadUI, "ui");
        SLog.loadTime(Setter::loadRender, "renderer");
        SLog.loadTime(Setter::loadChunks, "chunks");
        SLog.loadTime(Setter::loadCompatibility, "compatibility");


//        if (SVars.mainDirecory.child("settings.ulk").exists())
            SLog.loadTime(SettingsBuilder::uiBuild, "settings");


        SLog.elapsedInfo("load settings finished");
    }

    private static void loadCompatibility() {
        String ver = "sueno-version";
        String bld = "sueno-builder";
        String oldVersion = Core.settings.getString(ver, "null");
        String oldBuilder = Core.settings.getString(bld, "null");

        String[] builder = SVars.internalFileTree.child("data/builder").readString().split("/");
        Core.settings.put(bld, builder[0]);
        String currentVersion = Vars.mods.getMod(Sueno.class).meta.version + "/" + builder[1];
        Core.settings.put(ver, currentVersion);
        Core.settings.saveValues();



        if (SVars.versionWarning && (!((oldVersion.equals(currentVersion) || oldVersion.equals("null")) && (oldBuilder.equals(builder[0]) || oldBuilder.equals("null"))))) {
            Events.on(EventType.ClientLoadEvent.class, e -> {
                Time.runTask(10f, () -> {
                    SVars.ui.warning("Attention, backward compatibility may not be supported in different versions and builders", 3f);
                });
            });
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
        SLog.load("void space shader");
        Events.run(EventType.Trigger.drawOver, () -> {
            Draw.drawRange(SLayers.voidspace, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
                Vars.renderer.effectBuffer.end();
                Vars.renderer.effectBuffer.blit(SShaders.voidSpaceShader);
            });
        });

        SLog.load("dead zone shader");
        Events.run(EventType.Trigger.drawOver, () -> {
            Draw.drawRange(SLayers.deadZone, 1f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
                Vars.renderer.effectBuffer.end();
                Vars.renderer.effectBuffer.blit(SShaders.deadShader);
            });
        });

        SLog.load("normal shader");
        Events.run(EventType.Trigger.drawOver, () -> {
            Draw.drawRange(42f, () -> Vars.renderer.effectBuffer.begin(Color.clear), () -> {
                Vars.renderer.effectBuffer.end();
                Vars.renderer.effectBuffer.blit(SShaders.normalShader);
            });
        });
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