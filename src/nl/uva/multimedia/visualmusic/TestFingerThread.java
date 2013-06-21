package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class TestFingerThread extends FingerThread {
    private static final String TAG = "TestFingerThread";

    int lastX = -1;

    protected void init() {
        super.init();

        // Init.
        Log.v(TAG, "Finger! =D");
    }

    protected void update() {
        super.update();

        if (this.monitor == null)
            return;

        TestFingerThreadMonitor monitor = (TestFingerThreadMonitor)this.monitor;
        int newX = (int)monitor.getX();
        if (newX != this.lastX) {
            this.lastX = newX;
            Log.v(TAG, "Finger moved (" + newX + ")! =O");
        }
    }

    protected void finish() {
        super.finish();

        // Finish.
        Log.v(TAG, "No finger! =(");
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }
}
