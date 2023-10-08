package org.durmiendo.sueno.controllers;

import arc.math.Mathf;
import arc.util.Log;
import arc.util.Threads;
import arc.util.Time;

public abstract class GenericController {
    public Thread updateThread;

    private long prevUpd;
    private final long delay;
    public final long frequency;

    private volatile boolean run = false;

    public volatile float delayDelta;

    public GenericController(long f) {
        frequency = f;
        delay = Mathf.ceil(1000000f / frequency);

        updateThread = Threads.daemon(() -> {
            try {
                prevUpd = Time.nanos();
                while (true) {
                    while (!run) {
                        Thread.sleep(delay / 1000, (int) (delay % 1000));
                    }
                    long delta = Time.nanos() - prevUpd;
                    if (delta < delay) {
                        Thread.sleep(delta / 1000, (int) (delta % 1000));
                    }
                    delayDelta = (float) delta / (float) delay;
                    prevUpd = Time.nanos();
                    update();
                }
            } catch (Exception e) {
                Log.err(e);
            }
        });
    }

    public abstract void update();

    public void stop() {
        run = false;
    }

    public void start() {
        run = true;
    }
}
