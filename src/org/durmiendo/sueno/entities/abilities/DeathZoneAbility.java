package org.durmiendo.sueno.entities.abilities;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.abilities.Ability;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.SLayers;

public class DeathZoneAbility extends Ability {
    public UnitType summon;
    public float spawnTime = 60f, minRange, maxRange;
    public Effect spawnEffect = Fx.spawn;
    public boolean parentizeEffects;
    public int maxSummon = 10;

    protected float timer;

    public DeathZoneAbility(UnitType summon, float spawnTime, float minRange, float maxRange, int summonCount){
        this.summon = summon;
        this.spawnTime = spawnTime;
        this.minRange = minRange;
        this.maxRange = maxRange;
        maxSummon = summonCount;
    }

    public DeathZoneAbility(){
    }

    public void addStats(Table t) {
        t.add("[lightgray]" + Stat.buildTime.localized() + ": [white]" + Strings.autoFixed(this.spawnTime / 60.0F, 2) + " " + StatUnit.seconds.localized());
    }

    public void update(Unit unit) {
        this.timer += Time.delta * Vars.state.rules.unitBuildSpeed(unit.team);
        if (this.timer >= this.spawnTime) {
            int units = Groups.unit.count(a -> a.team == unit.team && a.type == this.summon && a.dst(unit) < 8*20f);
            int tunits = Groups.unit.count(a -> a.team == unit.team && a.type == unit.type);

            if (units <= tunits * maxSummon) {
                float a = Mathf.random(0f, 360f);
                float r = Mathf.random(minRange, maxRange);
                float x = r * Mathf.cos(a) + unit.x;
                float y = r * Mathf.sin(a) + unit.y;
                this.spawnEffect.at(x, y, a, this.parentizeEffects ? unit : null);

                Unit u = this.summon.create(unit.team);
                u.set(x, y);
                u.rotation = unit.rotation + Mathf.random(-10f, 10f);
                Events.fire(new EventType.UnitCreateEvent(u, null, unit));
                if(!Vars.net.client()){
                    u.add();
                }

                this.timer = 0f;
            }
        }
    }

    public Color c = Color.valueOf("79eef2");

    @Override
    public void draw(Unit unit) {
        super.draw(unit);
        Draw.z(SLayers.deadZone);

        Draw.alpha(0.3f);
        Draw.color(c);
        Draw.scl(1f / 64f * maxRange);
        Draw.rect(SVars.core.getRegion("circle"), unit.x, unit.y);
    }

    @Override
    public String localized(){
        return Core.bundle.format("ability.deadzone");
    }
}
