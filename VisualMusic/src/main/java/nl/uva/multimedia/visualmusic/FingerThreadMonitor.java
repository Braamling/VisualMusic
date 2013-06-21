package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 6/20/13.
 */
public class FingerThreadMonitor {
    private float x, y;

    public FingerThreadMonitor() {
        this.x = 0;
        this.y = 0;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
