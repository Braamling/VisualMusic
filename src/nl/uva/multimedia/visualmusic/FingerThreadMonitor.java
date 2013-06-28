package nl.uva.multimedia.visualmusic;

/**
 * 
 */
public class FingerThreadMonitor {
    protected float x, y;
    protected int width, height;
    protected int fingerId;

    /**
     * Constructor without arguments.
     * Sets bots x and y to zero.
     */
    public FingerThreadMonitor() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor with an x and y.
     * @param x x position of finger to set the x in the class.
     * @param y y position of finger to set the y in the class.
     */
    public FingerThreadMonitor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a Thread for a finger.
     * @param c Class to be threaded
     * @param <T> Thread of fingerThread class
     * @return Return the Thread.
     * @throws InstantiationException Error on instantiation of the thread
     * @throws IllegalAccessException Error on illegal access of the thread.
     */
    public static <T>T createInstance(Class c) throws InstantiationException,
            IllegalAccessException {
        T t = (T)c.newInstance();
        return t;
    }

    /**
     * Set the fingerId of a fingerThread the monitor is monitoring.
     * @param fingerId The id to be set in the monitor.
     */
    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    /**
     * Get the fingerId of a monitor,
     * @return The fingerId assigned to the monitor.
     */
    public int getFingerId() {
        return this.fingerId;
    }

    /**
     * Set the X and Y positions.
     * @param x The x to be set.
     * @param y The y to be set.
     */
    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the X position.
     * @param x The x to be set.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Set the Y position.
     * @param y The y to be set.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Set the width in the monitor.
     * @param width The width to be set.
     */
    public void setWidth(int width){
        this.width = width;
    }

    /**
     * Set the height in the monitor.
     * @param height The height to be set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the current x position of the finger.
     * @return The x position.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Get the current y position of the finger.
     * @return The y position.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Get the width of the canvas from the monitor.
     * @return the width.
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Get the Height of the canvas from the monitor.
     * @return the height.
     */
    public int getHeight() {
        return this.height;
    }
}
