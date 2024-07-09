package org.durmiendo.sueno.statuses;

import arc.util.Time;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.gen.Unit;
import org.durmiendo.sueno.core.SVars;

public class SStatusEffect extends Content {
    @Override
    public ContentType getContentType() {
        return ContentType.status;
    }

    public void work(StatusEffectEntry entry) {

    }

    public void update(StatusEffectEntry entry) {
        if (entry.active) {
            if (entry.progress == 0) entry.active = false;
            else work(entry);
        }
        else if (entry.progress >= entry.immunity) {
            entry.active = true;
            work(entry);
        }

        if (entry.progress > 0) entry.progress = Math.min(0, entry.progress - 1.5f * Time.delta);
    }

    public void apply(Unit u) {
        float i = SVars.statusEffectsController.immunity(u.type);
        StatusEffectEntry entry = new StatusEffectEntry(this, i);
        SVars.statusEffectsController.apply(entry, u);
    }
}
