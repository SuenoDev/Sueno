package org.durmiendo.sueno.events;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

public class CampainClose {
    public CampainClose() {
        SVars.onCampain = false;
        Log.info("campain closed");
    }
}