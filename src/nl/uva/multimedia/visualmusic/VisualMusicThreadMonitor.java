package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/21/13.
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private static final String TAG = "VisualMusicThreadMonitor";

    private int fingerId;
    private boolean draw;
    private ParticleCanvas particleCanvas;
    private boolean finishing = false, reboot = false;
    private Particles[] particles;

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
        // if (draw == false) {
        //     StackTraceElement el =
        //             Thread.currentThread().getStackTrace()[3];
        //     Log.v(TAG, el.getFileName() + ": " + el.getLineNumber());
        // }

        this.draw = draw;
    }

    public boolean canDraw(){
        return this.draw;
    }

    public void setFinishing(boolean finishing) {
        this.finishing = finishing;
    }

    public boolean isFinishing() {
        return this.finishing;
    }

    public void setReboot(boolean reboot) {
        this.reboot = reboot;
    }

    public boolean needsReboot() {
        return this.reboot;
    }

    public void initParticlesArray(int nParticleGroups){
        this.particles = new Particles[nParticleGroups];
    }

    public Particles getParticles(int index){
        return this.particles[index];
    }
}