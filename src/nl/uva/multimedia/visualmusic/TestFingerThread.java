package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 6/20/13.
 */
public class TestFingerThread extends FingerThread {
    private static final String TAG = "TestFingerThread";

    protected void init() {
        super.init();

        // Init.
    }

    protected void update() {
        super.update();

        // Update.
    }

    protected void finish() {
        super.finish();

        //
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }
}
