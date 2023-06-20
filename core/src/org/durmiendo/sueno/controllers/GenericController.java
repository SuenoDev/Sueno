package org.durmiendo.sueno.controllers;

import arc.math.Mathf;
import arc.util.Log;
import arc.util.Threads;
import arc.util.Time;

public abstract class GenericController {
    public Thread updateThread;

    private long prevUpd;
    private final long delay;
    private final long frequency;

    private final Run run = new Run();

    private static final class Run {
        private transient boolean v = false;
    }

    public GenericController(long f) {
        frequency = f;
        delay = Mathf.ceil(1000000f / frequency);

        updateThread = Threads.daemon(() -> {
            try {
                prevUpd = Time.nanos();
                while (true) {
                    if (!run.v)
                        run.wait();
                    long delta = Time.nanos() - prevUpd;
                    if (delta < delay) {
                        Thread.sleep(delta / 1000, (int) (delta % 1000));
                    }
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
        run.v = false;
    }

    public void start() {
        run.v = true;
        run.notifyAll();
    }
}
