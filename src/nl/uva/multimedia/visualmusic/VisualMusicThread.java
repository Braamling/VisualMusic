package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private int lastY = -1;
    private int i = 0;
    private PlayTone mPlayTone = new PlayTone();

    // this line below is temporary, it shouldn't be static, I'll fix that later
    public static int fingerDirection = -1; /* 0 is downwards, 1 is upwards */

    public static final int N_PARTICLE_GROUPS = 50; /* Total number of particle-groups */
    public static final int PARTICLE_GROUP_SIZE = 8; /* Number of unique particles in a single group */
//    private static final int PARTICLE_AMOUNT = 50; /* Same as N_PARTICLE_GROUPS ? */
    private static final int PARTICLE_LIFE_TIME = 50; /* Maximum life time of a single particle */
    private static final int PARTICLE_MAX_SPEED = 3; /* Maximum speed of a single particle */
    private static final int PARTICLE_RADIUS = 13; /* Maximum radius of a single particle */

    Particles[] particles = new Particles[N_PARTICLE_GROUPS];

    protected void init() {
        super.init();
        // Init.

        ((VisualMusicThreadMonitor)this.monitor).getParticleCanvas().addFinger();
    }

    protected void update() {
        super.update();
        float freq, y_scale;

        if (this.monitor == null) {
            return;
        }

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;

        if (!monitor.canDraw()) {
            return;
        }

        int newX = (int)monitor.getX();
        int newY = (int)monitor.getY();

        int begin_color = monitor.getBeginColor();
        int end_color = monitor.getEndColor();

        /* The 0 in this if statement can be changed to a higher setting if
         * it is decided that an unmoving finger should not generate particles,
         * or that verticle movement is not allowed. */
        if (Math.abs(newX - this.lastX) >= 0) {
            particles[this.i++ % N_PARTICLE_GROUPS] =
                    new Particles(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                    this.monitor.getY(), PARTICLE_RADIUS, PARTICLE_MAX_SPEED, PARTICLE_LIFE_TIME,
                            begin_color, end_color);
            this.lastX = newX;
        }

        /* Which way is the finger going? Upwards or downwards? */
        fingerDirection = (newY > this.lastY) ? 0 : 1;

        try {
            int key = this.getKey(), scale = 3;
            if (key >= 12) {
                key -= 12;
                scale ++;
            }
            ToneFrequency newFrequency = ToneFrequency.fromKey(key, scale);
            freq = newFrequency.get();
            y_scale = monitor.getY() / (float)monitor.getHeight();

            mPlayTone.setFreq(freq, y_scale);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mPlayTone.play();

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

//            Log.v(TAG, "drw " + monitor.canDraw());

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
//        Log.v(TAG, "done");

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
                if (particles[i] != null) {
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
        float part;
        int key;

        part = monitor.getWidth() / (int)(1.5 * ToneFrequency.N_KEYS);
        key = (int)(lastX / part);

        return (int) key;
    }
}
