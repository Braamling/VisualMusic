package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by klaplong on 6/21/13.
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private int fingerId;

    private boolean write;

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
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

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;

    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.particleCanvas = particleCanvas;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.surfaceHolder;
    }

    public int getFingerId() {
        return this.fingerId;
    }

    public ParticleCanvas getParticleCanvas() {
        return this.particleCanvas;
    }

    public void activateWrite(){
        this.write = true;
    }

    public void deactivateWrite(){
        this.write = false;
    }

    public boolean getWrite(){
        return this.write;
    }
}