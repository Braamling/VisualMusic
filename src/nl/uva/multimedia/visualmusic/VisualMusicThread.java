package nl.uva.multimedia.visualmusic;

import android.app.ActivityManager;
import android.os.SystemClock;
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

    // TODO this line below is temporary, it shouldn't be static, I'll fix that later
    public static int fingerDirection = -1; /* 0 is downwards, 1 is upwards */

    public static final int FRAME_REFRESH_TIME  = 10; /* Time in milliseconds to wait for rendering */

    /* Depending on the amount of touch move events handled in a x amount of time there should be
     * more particles per group and less groups. When the refresh rate is really high the particles
     * group size can even be 1.
     */
    public static final int N_PARTICLE_GROUPS   = 300; /* Total number of particle-groups */
    public static final int PARTICLE_GROUP_SIZE = 1;  /* Number of unique particles in a single group */

    private static float particleMaxSpeed       = 2;  /* Maximum speed of a single particle */
    private static int particleLifetime         = 300; /* Maximum life time of a single particle */
    private static int particleRadiusBase       = 0; /* Value should be set after screen dimensions are known */
    private static int particleRadius           = 0; /* Value of the radius based on the frequency */

    private boolean new_touch = true;
    public static final int N_KEYS = 36;
    private static final int LOW_OCTAVE = 2;
    private long last_render_time;

    Particles[] particles = new Particles[N_PARTICLE_GROUPS];

    protected void init() {
        super.init();
        // Init.
        this.last_render_time = SystemClock.currentThreadTimeMillis();
        ((VisualMusicThreadMonitor)this.monitor).setActive(true);

        ((VisualMusicThreadMonitor)this.monitor).pick_color_scheme();

    }

    protected void update() {
        super.update();
        float freq, y_scale;

        if (this.monitor == null) {
            return;
        }

        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;

        /* Check whether this is a touchdown to change the color scheme */
        if(new_touch){
            monitor.pick_color_scheme();
            new_touch = false;
        }

        /* Determine particle max radius. This cannot be done in the init() method
         * because the canvas size is not yet known at that time. */
        if (VisualMusicThread.particleRadiusBase == 0) {
            VisualMusicThread.particleRadiusBase = (monitor.getParticleCanvas().getHeight() > 0) ?
                    (monitor.getParticleCanvas().getHeight() / 50) : 27;
            VisualMusicThread.particleRadius = VisualMusicThread.particleRadiusBase;        
        }

        int newX = (int)monitor.getX();
        int newY = (int)monitor.getY();

        /* Update the look of the upcoming particles based on the x position */
        setParticleParameters(monitor.getWidth(), monitor.getX());


        /* The 0 in this if statement can be changed to a higher setting if
         * it is decided that an unmoving finger should not generate particles,
         * or that vertical movement is not allowed. */
        if (Math.abs(newX - this.lastX) >= 0) {
            particles[this.i++ % N_PARTICLE_GROUPS] =
                    new Particles(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                    this.monitor.getY(), VisualMusicThread.particleRadius,
                            particleMaxSpeed, particleLifetime, monitor.getBeginColor(),
                            monitor.getEndColor());
            this.lastX = newX;
        }

        /* Which way is the finger going? Upwards or downwards? */
        fingerDirection = (newY > this.lastY) ? 0 : 1;

        try {
            int key = this.getKey(), scale = LOW_OCTAVE;
            while (key >= 12) {
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


        renderFrame(monitor);

    }

    protected void setParticleParameters (int width, float x) {
        float div = 1 - (x / width); /* Between 0 and 1, indicator of how far
                                      * on the screen the finger is (on x-axis) */
        float ftr = (float) (div + 0.75); /* Between 0.75 and 1.75 */

        VisualMusicThread.particleMaxSpeed = (float)(1 + (9 * div)); /* High frequency = high speed */
        VisualMusicThread.particleLifetime = Math.round(50 + (50 * div)); /* High frequency = long lifetime */
        VisualMusicThread.particleRadius   = Math.round(VisualMusicThread.particleRadiusBase * ftr);
    }


    protected void finish() {

        new_touch = true;

        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;
        boolean stillAlive;

        monitor.setFinishing(true);

        mPlayTone.stop();

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
        monitor.setActive(false);
        super.finish();
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }

    public void renderFrame(VisualMusicThreadMonitor monitor) {
        if(SystemClock.currentThreadTimeMillis() - last_render_time > this.FRAME_REFRESH_TIME){
            last_render_time = SystemClock.currentThreadTimeMillis();
            try {
                for (int i = 0; i < particles.length; i ++) {
                    if (particles[i] != null) {
                        if (particles[i].isDead()) {
                            particles[i] = null;
                            continue;
                        }

                        particles[i].update();
                    }
                }
            }
            catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }

            monitor.setParticles(this.particles);
        }
    }

    private int getKey(){
        float part;
        int key;

        part = monitor.getWidth() / N_KEYS;
        key = (int)(lastX / part);

        return key;
    }

}
