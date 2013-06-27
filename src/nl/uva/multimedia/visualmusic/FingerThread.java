package nl.uva.multimedia.visualmusic;

import java.lang.reflect.Array;

/**
 * A thread for multi touch.
 * <p></p>
 * Extending this class provides three functions one can override:
 * {@link #init() init}, {@link #update() update} and {@link #finish() finish}.
 */
public class FingerThread extends Thread {
    private static final String TAG = "FingerThread";

    protected FingerThreadMonitor monitor = null;
    private boolean on = true;
    protected boolean going, finished = true;

    /**
     * Executed when the finger's process starts.
     */
    protected void init() {

    }

    /**
     * Executed repeatedly until the finger's process stops.
     */
    protected void update() {

    }

    /**
     * Executed when the finger's process ends.
     */
    protected void finish() {}

    /**
     * Start the finger's process.
     */
    public void go() {
        this.init();
        this.going = true;
    }

    /**
     * The finger's run loop.
     */
    public void run() {
        while (this.on && (!this.isInterrupted())) {
            if (!this.going) {
                if (!this.finished) {
                    this.finish();
                    this.finished = true;
                }
                continue;
            }

            this.update();
        }
    }

    /**
     * End the finger's process.
     */
    public void end() {
        this.going = false;
        this.finished = false;
    }

    /**
     * Check whether the finger's process is running.
     * @return Whether the finger's process is on.
     */
    public boolean isOn() {
        return this.on;
    }

    /**
     * Switch the finger's process.
     * @param on The finger's state (on or off).
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Turn the finger's process off.
     */
    public void turnOff() {
        this.on = false;
    }

    /**
     * Create an instance of the class that has overridden this class.
     * @param c The class.
     * @param <T> The class' type.
     * @return The instance.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T>T createInstance(Class c)
            throws InstantiationException, IllegalAccessException {
        T t = (T)c.newInstance();
        return t;
    }

    /**
     * Create an array of the class that has overridden this class.
     * @param c The class.
     * @param n The number of objects in the array.
     * @param <T> The class' type.
     * @return The array.
     */
    public static <T>T[] createArray(Class c, int n) {
        T[] tArray = (T[])Array.newInstance(c, n);
        return tArray;
    }

    /**
     * Assign a monitor to the thread.
     * @param monitor The monitor.
     */
    public void assignMonitor(FingerThreadMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Get the thread's monitor.
     * @return The monitor.
     */
    public FingerThreadMonitor getMonitor() {
        return this.monitor;
    }
}
