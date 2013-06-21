package nl.uva.multimedia.visualmusic;

import android.util.Log;
import nl.uva.multimedia.visualmusic.ToneFrequency;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private Playtone mPlayTone = null;

    protected void init() {
        super.init();
        // Init.
        Log.v(TAG, "Finger! =D");
        mPlayTone = new Playtone();
        mPlayTone.play();
    }

    protected void update() {
        super.update();

        if (this.monitor == null)
            return;

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;
        int newX = (int)monitor.getX();
        if (newX != this.lastX) {
            this.lastX = newX;
            Log.v(TAG, "Finger moved (" + newX + ")! =O");
            /* This is where the playtone's hertz is adjusted.*/
            float hertz;
            hertz = ToneFrequency.fromKey(getKey, 4);
        }
    }

    protected void finish() {
        super.finish();
        mPlayTone.stop();
        mPlayTone = null;
        // Finish.
        Log.v(TAG, "No finger! =(");
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }

    private int getKey(){
        float part, key;

        part = monitor.getWidth() / ToneFrequency.N_KEYS;
        key = lastX / part;

        return (int) key;
    }
}
