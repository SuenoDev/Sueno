package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.SettingsMenuDialog;


public class SliderSetting extends SettingsMenuDialog.SettingsTable.Setting {
    float def;
    float min;
    float max;
    float step;
    StringProcessorf sp;

    public SliderSetting(String name, float def, float min, float max, float step, StringProcessorf s) {
        super(name);
        this.def = def;
        this.min = min;
        this.max = max;
        this.step = step;
        this.sp = s;
    }

    public void add(SettingsMenuDialog.SettingsTable table) {
        Slider slider = new Slider(min, max, step, false);
        slider.setValue(Core.settings.getFloat(name));
        Label value = new Label("", Styles.outlineLabel);
        Table content = new Table();
        content.add(title, Styles.outlineLabel).left().growX().wrap();
        content.add(value).padLeft(10.0F).right();
        content.margin(3.0F, 33.0F, 3.0F, 33.0F);
        content.touchable = Touchable.disabled;
        slider.changed(() -> {
            Core.settings.put(name, slider.getValue());
            value.setText(sp.get(slider.getValue()));
        });
        slider.change();
        addDesc(table.stack(new Element[]{slider, content}).width(Math.min(Core.graphics.getWidth() / 1.2F, 460.0F)).left().padTop(4.0F).get());
        table.row();

        Core.settings.defaults(name, def);
    }
    public interface StringProcessorf {
        String get(float var1);
    }
}

