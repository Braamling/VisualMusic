package nl.uva.multimedia.visualmusic;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

/**
 * Created by klaplong on 6/21/13.
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private static final String TAG = "VisualMusicThreadMonitor";

    private ParticleCanvas particleCanvas;
    private boolean active = false, finishing = false, reboot = false;
    private Particles[] particles = null;

    private int begin_color;
    private int end_color;

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

    public void pick_color_scheme(){
        Random r = new Random();
        int id = (r.nextInt(9) +0);

        this.begin_color    = createBeginColor(id);
        this.end_color      = createEndColor(id);
    }

    public int createEndColor(int id){
        switch(id) {
            case 0:
                return Color.argb(255, 255, 52, 0); /* Dark orange */
            case 1:
                return Color.argb(255, 0, 94, 255); /* Medium blue */
            case 2:
                return Color.argb(255, 0, 204, 102); /* Aqua green */
            case 3:
                return Color.argb(255, 232, 232, 232); /* Almost white */
            case 4:
                return Color.argb(255, 255, 255, 0); /* Dark grey */
            case 5:
                return Color.argb(255, 255, 0, 255); /* Purple-pink */
            case 6:
                return Color.argb(255, 255, 0, 72); /* Purple-red */
            case 7:
                return Color.argb(255, 20, 15, 64); /* Dark blue */
            case 8:
                return Color.argb(255, 150, 89, 0);
            case 9:
                return Color.argb(255, 255, 0, 0);
            default:
                return Color.argb(255, 0, 0, 0);
        }
    }

    public int createBeginColor(int id){
        switch(id){
            case 0:
                return Color.argb(255, 255, 255, 0); /* Yellow */
            case 1:
                return Color.argb(255, 0, 201, 255); /* Nearing aqua */
            case 2:
                return Color.argb(255, 0, 204, 41); /* Light green-ish */
            case 3:
                return Color.argb(255, 142, 145, 148); /* Gray (with slight blue) */
            case 4:
                return Color.argb(255, 255, 255, 255); /* Dark red / brown */
            case 5:
                return Color.argb(255, 255, 0, 0); /* Red */
            case 6:
                return Color.argb(255, 131, 117, 255); /* Violet */
            case 7:
                return Color.argb(255, 85, 179, 185); /* Type of light blue */
            case 8:
                return Color.argb(255, 232, 217, 0); /* Kind of yellow */
            case 9:
                return Color.argb(255,40, 40, 40);
            default:
                return Color.argb(255, 0, 255, 0);
        }
    }

    public int getEndColor(){
        return this.end_color;
    }

    public int getBeginColor(){
        return this.begin_color;
    }

}