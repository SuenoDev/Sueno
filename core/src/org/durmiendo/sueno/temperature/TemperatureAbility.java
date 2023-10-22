package org.durmiendo.sueno.temperature;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.ui.Bar;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.Colorated;

public class TemperatureAbility extends Ability {

    public float temperature;
    public float ceiling;
    public float freezingSpeed;

    @Override
    public void draw(Unit unit) {
        super.draw(unit);

    }

    @Override
    public void displayBars(Unit unit, Table bars){
        bars.row();
        bars.add(new Bar(
                () -> "Температура " + Math.round(temperature) + "°C\n" + Math.round(freezingSpeed) + "°C / сек",
                () -> Colorated.gradient(Color.cyan, Color.red, (temperature + 200) / 400),
                () -> (temperature + 200) / 400
        ));
        bars.row();
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        freezingSpeed+=SVars.freezingPower / Core.graphics.getFramesPerSecond();


    }
}
