package org.durmiendo.sueno.extend;


import arc.graphics.Color;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import org.durmiendo.sueno.content.SAttributes;



public class SuenoBlock extends Block  {



    public SuenoBlock(String name) {
        super(name);
        size = 1;
    }




    @Override
    public void setStats(){
        super.setStats();
        this.stats.add(SuenoStat.temperature, " from @ to @", this.attributes.get(SAttributes.temperatureMin), this.attributes.get(SAttributes.temperatureMax));
    }


    @Override
    public void setBars() {
        super.setBars();

        addBar("uses", (Temperature entity) -> new Bar(
                () -> "Temperature",
                () -> Color.white,
                () -> (entity.temperature + Math.abs(attributes.get(SAttributes.temperatureMin)))/((attributes.get(SAttributes.temperatureMax) + Math.abs(attributes.get(SAttributes.temperatureMin)))/100)/100)
        );
    }

    public static class Temperature extends Building {
        public float temperature = 0f;
    }
}
/*addBar("uses",
(TerraformerBuild entity) -> new Bar(
() -> Core.bundle.format("bar.uses", maxUses - entity.used),
 () -> Pal.accent,
  () -> ((float) maxUses - entity.used) / maxUses));
*/
//attributes.get(SAttributes.temperature) + Math.abs(attributes.get(SAttributes.temperatureMin)))/((attributes.get(SAttributes.temperatureMax) + Math.abs(attributes.get(SAttributes.temperatureMin)))/100)/100
