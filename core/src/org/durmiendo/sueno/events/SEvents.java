package org.durmiendo.sueno.events;

import arc.util.Log;
import org.durmiendo.sueno.core.SVars;

public class SEvents {
    public static class CampaignOpenEvent {
        public CampaignOpenEvent() {
            SVars.onCampaign = true;
        }
    }

    public static class CampaignCloseEvent {
        public CampaignCloseEvent() {
            SVars.onCampaign = false;
        }
    }
}
