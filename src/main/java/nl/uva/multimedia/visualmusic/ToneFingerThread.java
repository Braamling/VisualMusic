package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class ToneFingerThread extends FingerThread {
    private static final String TAG = "ToneFingerThread";

    private PlayTone mPlayTone;

    protected void init() {
        super.init();

        this.mPlayTone = new PlayTone();
    }

    protected void update() {
        super.update();

        if (this.getX() != 0) {
            double scale = this.getX() / this.getWidth();
            this.mPlayTone.newFreq(scale);
        }
    }

    protected void finish() {
        super.finish();

        this.mPlayTone.stop();
    }

    public void turnOff() {
        /* Execute the actual turnoff, to prevent the thread from terminating
         * before custom stop methods have been called. */
        super.turnOff();
    }
}
