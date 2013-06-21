package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerHandler<T extends FingerThread> {
    private static final String TAG = "FingerHandler";

    private Class c;

    private int maxFingers;
    private T[] fingerThreads;

    public FingerHandler(Class c, int maxFingers) {
        this.c = c;
        this.maxFingers = maxFingers;
        this.fingerThreads = T.createArray(c, maxFingers);
    }

    public void turnOnFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        try {
            this.fingerThreads[i] = T.createInstance(this.c);
            this.fingerThreads[i].start();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOffFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        if (!this.fingerThreads[i].isOn())
            return;

        this.fingerThreads[i].turnOff();

        try {
            this.fingerThreads[i].join(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFingerOn(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        return this.fingerThreads[i].isOn();
    }

    public void moveFinger(int i, float x, float y, float width)
            throws ImpossibleFingerException {
        this.checkFinger(i);
    }

    private void checkFinger(int i) throws ImpossibleFingerException {
        if (i >= this.maxFingers)
            throw new ImpossibleFingerException();
    }

    public int getMaxFingers() {
        return this.maxFingers;
    }
}
