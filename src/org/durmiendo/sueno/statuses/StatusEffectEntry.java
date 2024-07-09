package org.durmiendo.sueno.statuses;

public class StatusEffectEntry {
    public SStatusEffect effect;
    public float progress;
    public float immunity;
    public boolean active;

    public StatusEffectEntry(SStatusEffect effect, float immunity) {
        this.effect = effect;
        this.progress = 0f;
        this.immunity = immunity;
        this.active = false;
    }
}
