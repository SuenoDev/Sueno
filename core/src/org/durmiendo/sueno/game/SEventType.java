package org.durmiendo.sueno.game;

import arc.util.Log;
import mindustry.game.EventType;
import org.durmiendo.sueno.core.SVars;

public class SEventType extends EventType {
    public enum Trgger{

    }

    public static class CampainOpen{
        public CampainOpen() {
            SVars.onCampain = true;
            Log.info("campain lauched");
        }
    }
    public static class CampainClose{
        public CampainClose() {
            SVars.onCampain = false;
            Log.info("campain exit");
        }
    }

}
