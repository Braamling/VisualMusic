package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 6/21/13.
 */
public class TestFingerThreadMonitor extends FingerThreadMonitor {
    private int fingerId;

    public TestFingerThreadMonitor() {
        super();

        this.fingerId = -1;
    }

    public TestFingerThreadMonitor(int fingerId) {
        super();

        this.fingerId = fingerId;
    }

    public TestFingerThreadMonitor(float x, float y, int fingerId) {
        super(x, y);

        this.fingerId = fingerId;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public int getFingerId() {
        return this.fingerId;
    }
}
