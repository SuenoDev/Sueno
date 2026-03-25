package org.durmiendo.sueno.ui.fragments;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.temperature.TemperatureController;
import org.durmiendo.sueno.ui.elements.Switch;
import org.durmiendo.sueno.utils.SLog;

public class GodModeFragment extends Table {
    private static final float UI_SIZE = 40f;
    
    public boolean visible = false;
    private boolean drawDebug = false;
    private boolean setUnitT = true;
    
 
    private float radiusVal = 5f;
    private float visibleRadiusVal = 0f;
    private float setTempValue = 0f; // raylib

    private Label tempInfoLabel;
    private Label unitInfoLabel;
    private Table container;
    
    public GodModeFragment() {
        super();
        
        Events.run(EventType.Trigger.draw, this::drawWorldDebug);
        Events.run(EventType.Trigger.update, this::updateLogic);
        Events.on(EventType.WorldLoadEndEvent.class, l -> rebuildBody(0));
        
        visible(() -> visible);
    }
    
    public void create() {
        Vars.ui.hudGroup.fill(t -> {
            t.left();
            // Панель кнопок
            t.table(Tex.buttonEdge4, buttons -> {
                buttons.defaults().size(UI_SIZE).left();
                
                buttons.button(Icon.upOpen, Styles.clearNonei, () -> visible = !visible);
                
                Switch sw1 = new Switch(Icon.pause);
                sw1.click(TemperatureController::setSimulationPaused);
                buttons.add(sw1);
                
                Switch sw2 = new Switch(Icon.eyeSmall);
                sw2.click(b -> drawDebug = !drawDebug);
                buttons.add(sw2);
                
                buttons.button(Icon.refreshSmall, Styles.clearNonei, () -> rebuildBody(0));
                buttons.button(Icon.terminal, Styles.clearNonei, () -> rebuildBody(1));
                buttons.button(Icon.editorSmall, Styles.clearNonei, () -> SVars.ui.dynTexList.show());
                
            }).left();
            
            t.row();

            t.add(this).left();
        });
    }
    
    private void rebuildBody(int page) {
        clear();
        background(Styles.black6);
        
        add("Debug Menu").color(Color.gray).pad(5).left().row();
        
        if (page == 1) {
            buildLogPage();
        } else {
            buildMainPage();
        }
    }
    
    private void buildLogPage() {
        Table logs = new Table();
        logs.top().left();
//        for (String key : SLog.data.keys()) {
//            logs.label(() -> SLog.data.get(key)).left().row();
//        }
        
        ScrollPane pane = new ScrollPane(logs);
        add(pane).size(300, 400).left();
    }
    
    private void buildMainPage() {
        if (SVars.temperatureController == null) {
            add("[red]Controller is NULL").pad(10);
            return;
        }
        
        defaults().left().pad(2);
        
        tempInfoLabel = new Label("");
        unitInfoLabel = new Label("");
        add(tempInfoLabel).row();
        add(unitInfoLabel).row();
        
        add(new Image(Styles.black3)).height(4).fillX().pad(5).row();
        
        check("Re-init on Resize", false, b -> {
            SVars.temperatureController.init(Vars.world.width(), Vars.world.height());
        }).row();
        
        add("Show T Radius:").color(Color.lightGray).row();
        Slider visSlider = new Slider(0, 50, 1, false);
        visSlider.setValue(visibleRadiusVal);
        visSlider.changed(() -> visibleRadiusVal = visSlider.getValue());
        add(visSlider).width(200).row();
        
        add("Brush Radius:").color(Color.lightGray).row();
        Slider rangeSlider = new Slider(0.5f, 30f, 0.5f, false);
        rangeSlider.setValue(radiusVal);
        rangeSlider.changed(() -> radiusVal = rangeSlider.getValue());
        add(rangeSlider).width(200).row();
        
        add("Brush Value (Press J):").color(Color.lightGray).row();
        Slider valSlider = new Slider(-1f, 1f, 0.01f, false);
        valSlider.setValue(setTempValue);
        valSlider.changed(() -> setTempValue = valSlider.getValue());
        
        Table valTable = new Table();
        valTable.add(valSlider).width(160);
        valTable.label(() -> Strings.fixed(valSlider.getValue(), 2)).width(40);
        add(valTable).row();
        
        check("Apply to Units", setUnitT, b -> setUnitT = b).row();
    }
    
    private void updateLogic() {
        if (!visible || SVars.temperatureController == null) return;
        
        Vec2 pos = Core.input.mouseWorld();
        int tx = (int)(pos.x / Vars.tilesize);
        int ty = (int)(pos.y / Vars.tilesize);
        
        float tAt = SVars.temperatureController.getRelativeTemperatureAt(tx, ty);
        tempInfoLabel.setText(Strings.format("Tile T: [@]@",
                getColor(tAt), Strings.fixed(tAt, 2)));
        
        Unit u = Vars.player.unit();
        if (u != null) {
            float uT = SVars.temperatureController.getRelativeTemperatureOf(u);
            unitInfoLabel.setText(Strings.format("Unit T: [@]@",
                    getColor(uT), Strings.fixed(uT, 2)));
        }
        
        if (Core.input.keyTap(KeyCode.j)) {
            applyTemperature(setTempValue, radiusVal);
        }
    }
    
    private void applyTemperature(float value, float radius) {
        float mx = Core.input.mouseWorldX() / Vars.tilesize;
        float my = Core.input.mouseWorldY() / Vars.tilesize;
        
        int r = Mathf.ceil(radius);
        int startX = (int)(mx - r);
        int endX = (int)(mx + r);
        int startY = (int)(my - r);
        int endY = (int)(my + r);
        
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (Mathf.dst2(x, y, mx, my) <= radius * radius) {
                    SVars.temperatureController.setRelativeTemperatureAt(x, y, value);
                }
            }
        }
        
        if (setUnitT) {
            Groups.unit.intersect(
                (mx - r) * Vars.tilesize, (my - r) * Vars.tilesize,
                r * 2 * Vars.tilesize, r * 2 * Vars.tilesize,
                unit -> SVars.temperatureController.setRelativeTemperatureOf(unit, value)
            );
        }
    }
    
    
    private void drawWorldDebug() {
        if (!working()) return;
        
        Draw.draw(Layer.overlayUI, () -> {
            float mx = Core.input.mouseWorldX();
            float my = Core.input.mouseWorldY();
            
            Drawf.dashCircle(mx, my, radiusVal * Vars.tilesize, Pal.accent);
            
            if (visibleRadiusVal > 0) {
                if (Vars.renderer.getScale() < 1.5f) return;
                
                int r = Mathf.ceil(visibleRadiusVal);
                int tileX = (int)(mx / Vars.tilesize);
                int tileY = (int)(my / Vars.tilesize);
                
                for (int x = tileX - r; x <= tileX + r; x++) {
                    for (int y = tileY - r; y <= tileY + r; y++) {
                        if (!SVars.temperatureController.isWithinBounds(x, y)) continue;
                        
                        float temp = SVars.temperatureController.getRelativeTemperatureAt(x, y);
                        
                        Color c = getGradientColor(temp);
                        Fonts.def.draw(Strings.fixed(temp, 2), x * Vars.tilesize, y * Vars.tilesize, c, 0.1f, false, Align.center);
                    }
                }
            }
        });
    }
    
    private boolean working() {
        return visible && SVars.temperatureController != null;
    }
    
    private String getColor(float val) {
        return getGradientColor(val).toString();
    }
    
    private Color getGradientColor(float val) {
        float n = (val + 1f) / 2f; // 0..1
        return Tmp.c1.set(Color.cyan).lerp(Color.red, n);
    }
}