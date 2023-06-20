package org.durmiendo.sueno.controllers;

import arc.util.Log;

public class TemperatureController extends GenericController{
    public TemperatureController() {
        super(2);
        start();
    }

    @Override
    public void update() {
        Log.info("temparature update");
    }
}
