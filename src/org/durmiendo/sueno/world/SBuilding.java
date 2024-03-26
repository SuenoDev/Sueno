package org.durmiendo.sueno.world;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;

public class SBuilding extends Building {
    public void draw() {
        if (this.block.variants != 0 && this.block.variantRegions != null) {
            Draw.rect(this.block.variantRegions[Mathf.randomSeed((long)this.tile.pos(), 0, Math.max(0, this.block.variantRegions.length - 1))], this.x, this.y, this.drawrot());
        } else {
            Draw.rect(this.block.region, this.x, this.y, this.drawrot());
        }

        this.drawTeamTop();
    }

    public void drawConfigure() {
        Draw.color(Pal.accent);
        Lines.stroke(1.0F);
        Lines.square(this.x, this.y, (float)(this.block.size * 8) / 2.0F + 1.0F);
        Draw.reset();
    }

    public void drawCracks() {
        if (this.block.drawCracks && this.damaged() && this.block.size <= 7) {
            int id = this.pos();
            TextureRegion region = Vars.renderer.blocks.cracks[this.block.size - 1][Mathf.clamp((int)((1.0F - this.healthf()) * 8.0F), 0, 7)];
            Draw.colorl(0.2F, 0.1F + (1.0F - this.healthf()) * 0.6F);
            Draw.rect(region, this.x, this.y, (float)(id % 4 * 90));
            Draw.color();
        }
    }

    public void drawDisabled() {
        Draw.color(Color.scarlet);
        Draw.alpha(0.8F);
        float size = 6.0F;
        Draw.rect(Icon.cancel.getRegion(), this.x, this.y, size, size);
        Draw.reset();
    }

    public void drawLight() {
        Liquid liq = this.block.hasLiquids && this.block.lightLiquid == null ? this.liquids.current() : this.block.lightLiquid;
        if (this.block.hasLiquids && this.block.drawLiquidLight && liq.lightColor.a > 0.001F) {
            this.visualLiquid = Mathf.lerpDelta(this.visualLiquid, this.liquids.get(liq) >= 0.01F ? 1.0F : 0.0F, 0.06F);
            this.drawLiquidLight(liq, this.visualLiquid);
        }

    }

    public void drawLiquidLight(Liquid liquid, float amount) {
        if (amount > 0.01F) {
            Color color = liquid.lightColor;
            float fract = 1.0F;
            float opacity = color.a * fract;
            if (opacity > 0.001F) {
                Drawf.light(this.x, this.y, (float)this.block.size * 30.0F * fract, color, opacity * amount);
            }
        }

    }

    public void drawSelect() {
        this.block.drawOverlay(this.x, this.y, this.rotation);
    }

    public void drawStatus() {
        if (this.block.enableDrawStatus && this.block.consumers.length > 0) {
            float multiplier = this.block.size > 1 ? 1.0F : 0.64F;
            float brcx = this.x + (float)(this.block.size * 8) / 2.0F - 8.0F * multiplier / 2.0F;
            float brcy = this.y - (float)(this.block.size * 8) / 2.0F + 8.0F * multiplier / 2.0F;
            Draw.z(71.0F);
            Draw.color(Pal.gray);
            Fill.square(brcx, brcy, 2.5F * multiplier, 45.0F);
            Draw.color(this.status().color);
            Fill.square(brcx, brcy, 1.5F * multiplier, 45.0F);
            Draw.color();
        }

    }

    public void drawTeam() {
        Draw.color(this.team.color);
        Draw.rect("block-border", this.x - (float)(this.block.size * 8) / 2.0F + 4.0F, this.y - (float)(this.block.size * 8) / 2.0F + 4.0F);
        Draw.color();
    }

    public void drawTeamTop() {
        if (this.block.teamRegion.found()) {
            if (this.block.teamRegions[this.team.id] == this.block.teamRegion) {
                Draw.color(this.team.color);
            }

            Draw.rect(this.block.teamRegions[this.team.id], this.x, this.y);
            Draw.color();
        }

    }
}
