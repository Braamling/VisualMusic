package nl.uva.multimedia.visualmusic;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by klaplong on 6/21/13.
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private static final String TAG = "VisualMusicThreadMonitor";

    private ParticleCanvas particleCanvas;
    private boolean active = false, finishing = false, reboot = false;
    private Particles[] particles = null;

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

    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.particleCanvas = particleCanvas;
    }

    public ParticleCanvas getParticleCanvas() {
        return this.particleCanvas;
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

    public void setParticles(Particles[] particles) {
        this.particles = particles;
    }

    public Particles[] getParticles() {
        return this.particles;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public int getEndColor(){
        switch(this.fingerId) {
            case 0:
                return Color.argb(255, 255, 0, 0);
            case 1:
                return Color.argb(255, 0, 255, 0);
            case 2:
                return Color.argb(255, 0, 0, 255);
            case 3:
                return Color.argb(255, 255, 255, 0);
            case 4:
                return Color.argb(255, 0, 255, 255);
            case 5:
                return Color.argb(255, 255, 0, 255);
            case 6:
                return Color.argb(255, 255, 0, 0);
            case 7:
                return Color.argb(255, 0, 255, 0);
            case 8:
                return Color.argb(255, 0, 0, 255);
            case 9:
                return Color.argb(255, 255, 255, 0);
            default:
                return Color.argb(255, 255, 0, 0);
        }
    }

    public int getBeginColor(){
        switch(this.fingerId){
            case 0:
                return Color.argb(255, 0, 255, 0);
            case 1:
                return Color.argb(255, 0, 0, 255);
            case 2:
                return Color.argb(255, 255, 255, 0);
            case 3:
                return Color.argb(255, 0, 255, 255);
            case 4:
                return Color.argb(255, 255, 0, 255);
            case 5:
                return Color.argb(255, 255, 0, 0);
            case 6:
                return Color.argb(255, 0, 255, 0);
            case 7:
                return Color.argb(255, 0, 0, 255);
            case 8:
                return Color.argb(255, 255, 255, 0);
            case 9:
                return Color.argb(255, 255, 0, 0);
            default:
                return Color.argb(255, 0, 255, 0);
        }
    }
}