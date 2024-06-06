package org.durmiendo.sueno.content;

import mindustry.type.StatusEffect;
import org.durmiendo.sueno.type.effects.SStatusEffect;

public class SStatusEffects {
    public StatusEffect test1;
    public SStatusEffect test2;

    public void load() {
        test1 = new StatusEffect("test1"){{
            show = true;
        }};

        test2 = new SStatusEffect("test2"){{
            show = true;
        }};
    }
}
