package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private int i = 0;
//    private PlayTone mPlayTone = null;

    public static final int N_PARTICLE_GROUPS = 50;
    public static final int PARTICLE_GROUP_SIZE = 20;
    private static final int PARTICLE_AMOUNT = 50;

    Particles[] particles = new Particles[PARTICLE_AMOUNT];

    protected void init() {
        super.init();
        // Init.

        ((VisualMusicThreadMonitor)this.monitor).getParticleCanvas().addFinger();
    }

    protected void update() {
        super.update();

        if (this.monitor == null)
            return;

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;

        if (!monitor.canDraw())
            return;

        int newX = (int)monitor.getX();
        if (newX != this.lastX) {
             particles[this.i++ % (PARTICLE_AMOUNT-1)] =
                     new Particles(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                     this.monitor.getY(), 5, 5,200);
        }

        renderFrame(monitor);
    }

    protected void finish() {
        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;

        int time = 0;
        while (time++ < 150) {
            time++;
            renderFrame(monitor);
        }

        monitor.getParticleCanvas().removeFinger();
        super.finish();
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }

    public void renderFrame(VisualMusicThreadMonitor monitor) {
        if (!monitor.canDraw())
            return;

        try {
            for (int i = 0; i < particles.length; i++) {
                if (particles[i] != null){
                    if(particles[i].isDead()) {
                        particles[i] = null;
                        continue;
                    }

                    particles[i].update();
                    try {
                        particles[i].render(monitor.getParticleCanvas());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            monitor.setDraw(false);
            monitor.getParticleCanvas().fingerBuffered();
        }
        catch (IllegalMonitorStateException e) {

        }
    }

    private int getKey(){
        float part, key;

        part = monitor.getWidth() / ToneFrequency.N_KEYS;
        key = lastX / part;

        return (int) key;
    }
}
