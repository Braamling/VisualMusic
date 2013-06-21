package nl.uva.multimedia.visualmusic;

import android.util.Log;

import java.lang.reflect.Array;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerThread extends Thread {
    private static final String TAG = "FingerThread";

    protected FingerThreadMonitor monitor = null;
    private boolean going, on = true;

    protected void init() {}

    protected void update() {}

    protected void finish() {}

    public void go() {
        this.init();
        this.going = true;
    }

    public void run() {
        while (this.on && (!this.isInterrupted())) {
            if (!this.going)
                continue;

            this.update();
        }
    }

    public void end() {
        this.going = false;
        this.finish();
    }

    public boolean isOn() {
        return this.on;
    }

    public void turnOff() {
        this.on = false;
    }

    public static <T>T createInstance(Class c) throws InstantiationException,
            IllegalAccessException {
        T t = (T)c.newInstance();
        return t;
    }

    public static <T>T[] createArray(Class c, int n) {
        T[] tArray = (T[])Array.newInstance(c, n);
        return tArray;
    }

    public void assignMonitor(FingerThreadMonitor monitor) {
        this.monitor = monitor;
    }

    public FingerThreadMonitor getMonitor() {
        return this.monitor;
    }
}
