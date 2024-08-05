package org.durmiendo.sueno.core;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.io.SaveVersion;
import org.durmiendo.sueno.controllers.CelestialBodyController;
import org.durmiendo.sueno.controllers.WeatherController;
import org.durmiendo.sueno.graphics.SLayers;
import org.durmiendo.sueno.graphics.SShaders;
import org.durmiendo.sueno.graphics.VoidStriderCollapseEffectController;
import org.durmiendo.sueno.settings.SettingsBuilder;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.temperature.TemperatureCustomChunk;
import org.durmiendo.sueno.utils.SLog;

public class Setter {
    public static void load() {
        SLog.mark();

        SLog.mark();
        loadVars();
        SLog.elapsedInfo("vars");

        SLog.mark();
        loadControllers();
        SLog.elapsedInfo("controllers");

        SLog.mark();
        loadUI();
        SLog.elapsedInfo("ui");

        SLog.mark();
        loadRender();
        SLog.elapsedInfo("render");

        SLog.mark();
        loadChunks();
        SLog.elapsedInfo("chunks");

        if (SVars.mainDirecory.child("settings.ulk").exists()) {
            SLog.mark();
            SettingsBuilder.uiBuild();
            SLog.elapsedInfo("settings");
        }

        SLog.elapsedInfo("load settings finished");
    }

    private static void loadControllers() {
        SLog.load("weather controller (not used)");

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