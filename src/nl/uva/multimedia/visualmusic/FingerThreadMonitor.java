package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerThreadMonitor {
    protected float x, y;

    public FingerThreadMonitor() {
        this.x = 0;
        this.y = 0;
    }

    public FingerThreadMonitor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static <T>T createInstance(Class c) throws InstantiationException,
            IllegalAccessException {
        T t = (T)c.newInstance();
        return t;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
