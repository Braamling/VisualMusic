package nl.uva.multimedia.visualmusic;

import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerHandler<T extends FingerThread, M extends FingerThreadMonitor> {
    private static final String TAG = "FingerHandler";

    private int maxFingers;
    private T[] fingerThreads;

    public FingerHandler(Class c, Class m, int maxFingers) {

        this.maxFingers = maxFingers;

        this.fingerThreads = T.createArray(c, maxFingers);
        for (int i = 0; i < maxFingers; i ++) {
            M monitor;

            try {
                this.fingerThreads[i] = T.createInstance(c);

                monitor = M.createInstance(m);
                this.fingerThreads[i].assignMonitor(monitor);

                this.fingerThreads[i].start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void goFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        this.fingerThreads[i].go();
    }

    public void endFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        this.fingerThreads[i].end();
    }

    public boolean isFingerOn(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        return this.fingerThreads[i].isOn();
    }

    private void checkFinger(int i) throws ImpossibleFingerException {
        if (i >= this.maxFingers)
            throw new ImpossibleFingerException();
    }

    public M getMonitor(int i)
            throws ImpossibleFingerException {
        this.checkFinger(i);

        return (M)this.fingerThreads[i].getMonitor();
    }

    public int getMaxFingers() {
        return this.maxFingers;
    }
}
