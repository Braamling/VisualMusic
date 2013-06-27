package nl.uva.multimedia.visualmusic;

/**
 * Handles threads for multi finger applications.
 * <p></p>
 * The thread's behaviour can be programmed by extending the
 * {@link FingerThread} and {@link FingerThreadMonitor} classes.
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Böhmer
 * @version 1.0
 */
public class FingerHandler<T extends FingerThread, M extends FingerThreadMonitor> {
    private static final String TAG = "FingerHandler";

    private int maxFingers;
    private T[] fingerThreads;

    private Class c, m;

    /**
     * FingerHandler constructor. Classes are required for generics.
     * @param c The FingerThread's extended class.
     * @param m The FingerThreadMonitor's extended class.
     * @param maxFingers The maximum amount of supported fingers.
     */
    public FingerHandler(Class c, Class m, int maxFingers) {

        this.c = c;
        this.m = m;

        this.maxFingers = maxFingers;

        this.fingerThreads = T.createArray(c, maxFingers);
        for (int i = 0; i < maxFingers; i ++) {
            M monitor;

            try {
                this.fingerThreads[i] = T.createInstance(c);

                monitor = M.createInstance(m);
                this.fingerThreads[i].assignMonitor(monitor);
                monitor.setFingerId(i);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initialize the handler. Instantiates the threads and monitors.
     */
    public void init() {
        for (int i = 0; i < maxFingers; i ++) {
            M monitor;

            try {
                this.fingerThreads[i] = T.createInstance(this.c);

                monitor = M.createInstance(this.m);
                this.fingerThreads[i].assignMonitor(monitor);
                monitor.setFingerId(i);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start the handler's threads.
     */
    public void start() {
        for (int i = 0; i < this.maxFingers; i ++) {
            this.fingerThreads[i].start();
        }
    }

    /**
     * Start a finger's process.
     * @param i The finger's id.
     * @throws ImpossibleFingerException
     */
    public void goFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        this.fingerThreads[i].go();
    }

    /**
     * Stop a finger's process.
     * @param i The finger's id.
     * @throws ImpossibleFingerException
     */
    public void endFinger(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        this.fingerThreads[i].end();
    }

    /**
     * Check whether a finger's process is running.
     * @param i The finger's id.
     * @return Whether the finger is on.
     * @throws ImpossibleFingerException
     */
    public boolean isFingerOn(int i) throws ImpossibleFingerException {
        this.checkFinger(i);

        return this.fingerThreads[i].isOn();
    }

    /**
     * Check whether a finger id can exist. Throws an ImpossibleFingerException
     * when the id cannot exist.
     * @param i The finger's id.
     * @throws ImpossibleFingerException
     */
    private void checkFinger(int i) throws ImpossibleFingerException {
        if (i >= this.maxFingers)
            throw new ImpossibleFingerException();
    }

    /**
     * Get the monitor of the specified finger.
     * @param i The finger's id.
     * @return The finger's monitor.
     * @throws ImpossibleFingerException
     */
    public M getMonitor(int i)
            throws ImpossibleFingerException {
        this.checkFinger(i);

        return (M)this.fingerThreads[i].getMonitor();
    }

    /**
     * Get the maximum amount of supported fingers.
     * @return The maximum amount of supported fingers.
     */
    public int getMaxFingers() {
        return this.maxFingers;
    }

    /**
     * Stop and kill all threads.
     */
    public void kill() {
        for (int i = 0; i < this.maxFingers; i ++) {
            this.fingerThreads[i].turnOff();

            try {
                this.fingerThreads[i].join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            this.fingerThreads[i] = null;
        }
    }
}
