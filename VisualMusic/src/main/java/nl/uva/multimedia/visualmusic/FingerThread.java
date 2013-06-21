package nl.uva.multimedia.visualmusic;

import java.lang.reflect.Array;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerThread extends Thread {
    private static final String TAG = "FingerThread";

    private boolean on;

    protected void init() {
        this.on = true;

        // Other initialization.
    }

    protected void update() {
        // Update.

    }

    protected void finish() {
        // Finish.
    }

    public void run() {
        this.init();

        while (true) {
            if ((!this.on) || this.isInterrupted())
                break;

            this.update();
        }

        this.finish();
    }

    public boolean isOn() {
        return this.on;
    }

    public void turnOff() {
        this.on = false;
        this.interrupt();
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
}
