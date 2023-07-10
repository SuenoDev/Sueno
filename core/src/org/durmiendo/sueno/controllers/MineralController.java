package org.durmiendo.sueno.controllers;

import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.type.Item;
import org.durmiendo.sueno.core.SVars;

public class MineralController extends GenericController{
    public MineralController() {
        super(2);
        init();

        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            start();
        });
        Events.on(EventType.ResetEvent.class, e -> {
            stop();
        });

    }

    public void init() {
        minerals = new Seq<>();
    }

    private Seq<Short> minerals;


    @Override
    public void update() {
    }

    public short getMin(Item i, short x, short y) {
        return minerals.get((x + y * Vars.world.width()) * (SVars.minerals.getMineralIndex(i) + 1 ));
    }

    public int getIndexMen(Item i, short x, short y) {
        return (x + y * Vars.world.width()) * (SVars.minerals.getMineralIndex(i) + 1 );
    }

    public short getMinIndex(int index) {
        return minerals.get(index);
    }



    public void setMin(Item it, short x, short y, short count) {
        minerals.set(getIndexMen(it, x, y), count);
    }

    public void setMinOnIndex(int index, short count) {
        minerals.set(getMinIndex(index), count);
    }

    public Seq<Short> getMinSeq() {
        return minerals;
    }

    public void setMineralsSeq(Seq<Short> seq) {
        minerals = seq;
    }
}
