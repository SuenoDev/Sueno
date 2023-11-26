package org.durmiendo.sueno.events;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

public class SEvents {
    public static class CampaignOpenEvent {
        public CampaignOpenEvent() {
            SVars.onCampaign = true;
            Log.info("campaign open");
        }
    }

    public static class CampaignCloseEvent {
        public CampaignCloseEvent() {
            SVars.onCampaign = false;
            Log.info("campaign closed");
        }
    }
}
