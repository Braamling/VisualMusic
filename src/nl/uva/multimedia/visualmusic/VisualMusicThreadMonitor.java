package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by klaplong on 6/21/13.
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private static final String TAG = "VisualMusicThreadMonitor";

    private int fingerId;
    private boolean draw;
    private ParticleCanvas particleCanvas;

    public VisualMusicThreadMonitor() {
        super();

        this.fingerId = -1;
    }

    public VisualMusicThreadMonitor(int fingerId) {
        super();

        this.fingerId = fingerId;
    }

    public VisualMusicThreadMonitor(float x, float y, int fingerId) {
        super(x, y);

        this.fingerId = fingerId;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.particleCanvas = particleCanvas;
    }

    public int getFingerId() {
        return this.fingerId;
    }

    public ParticleCanvas getParticleCanvas() {
        return this.particleCanvas;
    }

    public void setDraw(boolean draw){
        this.draw = draw;
    }

    public boolean canDraw(){
        return this.draw;
    }
}