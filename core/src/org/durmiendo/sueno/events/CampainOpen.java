package org.durmiendo.sueno.events;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

public class CampainOpen{
    public CampainOpen() {
        SVars.onCampaign = true;
        Log.info("campain open");
    }
}