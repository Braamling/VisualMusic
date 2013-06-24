package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private int i = 0;
    private PlayTone mPlayTone = new PlayTone();

    public static final int N_PARTICLE_GROUPS = 50; /* Total number of particle-groups */
    public static final int PARTICLE_GROUP_SIZE = 10; /* Number of unique particles in a single group */
    private static final int PARTICLE_AMOUNT = 50; /* Same as N_PARTICLE_GROUPS ? */

    Particles[] particles = new Particles[PARTICLE_AMOUNT];

    protected void init() {
        super.init();
        // Init.

        ((VisualMusicThreadMonitor)this.monitor).getParticleCanvas().addFinger();
    }

    protected void update() {
        super.update();
        float freq;

        if (this.monitor == null) {
            return;
        }

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;

        if (!monitor.canDraw()) {
            return;
        }

        int newX = (int)monitor.getX();

        /* The 0 in this if statement can be changed to a higher setting if
         * it is decided that an unmoving finger should not generate particles,
         * or that verticle movement is not allowed. */
        if (Math.abs(newX - this.lastX) >= 0) {
            particles[this.i++ % PARTICLE_AMOUNT] =
                    new Particles(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                    this.monitor.getY(), 10, 3, 120);
            this.lastX = newX;
        }

        // try {
        //     ToneFrequency newFrequency = ToneFrequency.fromKey(this.getKey(), 4);
        //      freq = newFrequency.get();
        //      mPlayTone.setFreq((double)freq);
        //  }catch (Exception e){
        //     e.printStackTrace();
        //  }
        // mPlayTone.play();

        renderFrame(monitor);
    }

    protected void finish() {
        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;
        boolean stillAlive;

        monitor.setFinishing(true);

        mPlayTone.stop();

        int time = 0;
        while (true) {
            if (monitor.needsReboot()) {
                monitor.setFinishing(false);
                monitor.setReboot(false);
                this.going = true;
                return;
            }

            stillAlive = false;
            for (int i = 0; i < this.particles.length; i ++) {
                if (this.particles[i] == null)
                    continue;
                if (!this.particles[i].isDead()) {
                    stillAlive = true;
                    break;
                }
            }
            if (!stillAlive)
                break;

            renderFrame(monitor);
        }

        monitor.setFinishing(false);
        monitor.getParticleCanvas().removeFinger();
        super.finish();
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }

    private void reboot() {
        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;

        monitor.setFinishing(false);
        this.run();
    }

    public void renderFrame(VisualMusicThreadMonitor monitor) {
        if (!monitor.canDraw()) {
            return;
        }

        try {
            for (int i = 0; i < particles.length; i ++) {
                if (particles[i] != null){
                    if (particles[i].isDead()) {
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
        Log.v(TAG, monitor.getWidth() + "," + key + "," + lastX);

        return (int) key;
    }
}
