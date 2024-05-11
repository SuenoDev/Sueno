package org.durmiendo.sueno.entities.bullet;

import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.entities.Mover;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.blocks.ControlBlock;

public class CopyBulletType extends BasicBulletType {
    public float change = 0f;
    public float rotater = 0f;
    public short generationLimit = 1;


    @Override
    public void update(Bullet b) {
        super.update(b);
        SBullet sb = (SBullet) b;
        if (Mathf.random(0f, 1f) <= change && sb.ancestor > 0) {
            SBullet c = (SBullet) create(b, sb, b.team, b.x, b.y, b.vel.angle() + Mathf.random(-rotater/2f, rotater/2f), b.damage, 0.01f, 1.0f, (Object) null, b.mover, b.aimX, b.aimY, false);
            sb.ancestor-=1;
            c.ancestor = sb.ancestor;
        }
    }

    @Nullable
    public Bullet create(Teamc owner, float x, float y, float angle, boolean adam) {
        return this.create(owner, owner.team(), x, y, angle, adam);
    }

    @Nullable
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, boolean adam) {
        return this.create(owner, team, x, y, angle, 1.0F, adam);
    }

    @Nullable
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, float velocityScl, boolean adam) {
        return this.create(owner, team, x, y, angle, -1.0F, velocityScl, 1.0F, (Object)null, adam);
    }

    @Nullable
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, float velocityScl, float lifetimeScl, boolean adam) {
        return this.create(owner, team, x, y, angle, -1.0F, velocityScl, lifetimeScl, (Object)null, adam);
    }

    @Nullable
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, float velocityScl, float lifetimeScl, Mover mover, boolean adam) {
        return this.create(owner, team, x, y, angle, -1.0F, velocityScl, lifetimeScl, (Object)null, mover, adam);
    }

    @Nullable
    public Bullet create(Bullet parent, float x, float y, float angle, boolean adam) {
        return this.create(parent.owner, parent.team, x, y, angle, adam);
    }

    @Nullable
    public Bullet create(Bullet parent, float x, float y, float angle, float velocityScl, float lifeScale, boolean adam) {
        return this.create(parent.owner, parent.team, x, y, angle, velocityScl, lifeScale, adam);
    }

    @Nullable
    public Bullet create(Bullet parent, float x, float y, float angle, float velocityScl, boolean adam) {
        return this.create(parent.owner(), parent.team, x, y, angle, velocityScl, adam);
    }

    @Nullable
    public Bullet create(@Nullable Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, boolean adam) {
        return this.create(owner, team, x, y, angle, damage, velocityScl, lifetimeScl, data, (Mover)null, adam);
    }

    @Nullable
    public Bullet create(@Nullable Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, boolean adam) {
        return this.create(owner, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, -1.0F, -1.0F, adam);
    }

    @Nullable
    public Bullet create(@Nullable Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY, boolean adam) {
        return this.create(owner, owner, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY, adam);
    }

    @Nullable
    public Bullet create(@Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY) {
        return create(owner, shooter, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY, true);
    }

    @Nullable
    public Bullet create(@Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY, boolean adam) {
        if (this.spawnUnit != null) {
            if (!Vars.net.client()) {
                Unit spawned = this.spawnUnit.create(team);
                spawned.set(x, y);
                spawned.rotation = angle;
                if (this.spawnUnit.missileAccelTime <= 0.0F) {
                    spawned.vel.trns(angle, this.spawnUnit.speed);
                }

                UnitController var16 = spawned.controller();
                if (var16 instanceof MissileAI) {
                    MissileAI ai = (MissileAI) var16;
                    if (shooter instanceof Unit) {
                        Unit unit = (Unit) shooter;
                        ai.shooter = unit;
                    }

                    if (shooter instanceof ControlBlock) {
                        ControlBlock control = (ControlBlock) shooter;
                        ai.shooter = control.unit();
                    }
                }

                spawned.add();
            }

            if (this.killShooter && owner instanceof Healthc) {
                Healthc h = (Healthc) owner;
                if (!h.dead()) {
                    h.kill();
                }
            }

            return null;
        } else {
            SBullet bullet = (SBullet) SBullet.create();
            bullet.type = this;
            bullet.owner = owner;
            bullet.team = team;
            bullet.time = 0.0F;
            bullet.originX = x;
            bullet.originY = y;
            bullet.aimTile = Vars.world.tileWorld(aimX, aimY);
            bullet.aimX = aimX;
            bullet.aimY = aimY;
            bullet.initVel(angle, this.speed * velocityScl);
            if (this.backMove) {
                bullet.set(x - bullet.vel.x * Time.delta, y - bullet.vel.y * Time.delta);
            } else {
                bullet.set(x, y);
            }

            bullet.lifetime = this.lifetime * lifetimeScl;
            bullet.data = data;
            bullet.drag = this.drag;
            bullet.hitSize = this.hitSize;
            bullet.mover = mover;
            bullet.damage = (damage < 0.0F ? this.damage : damage) * bullet.damageMultiplier();
            if (bullet.trail != null) {
                bullet.trail.clear();
            }

            bullet.add();
            if (this.keepVelocity && owner instanceof Velc) {
                Velc v = (Velc) owner;
                bullet.vel.add(v.vel());
            }

            if (adam) {
                bullet.ancestor = generationLimit;
            }

            return bullet;
        }
    }
}
