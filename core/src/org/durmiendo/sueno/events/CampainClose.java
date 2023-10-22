package org.durmiendo.sueno.events;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

public class CampainClose {
    public CampainClose() {
        SVars.onCampaign = false;
        Log.info("campain closed");
    }
}