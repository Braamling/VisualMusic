package nl.uva.multimedia.visualmusic;

import android.os.SystemClock;
import android.util.Log;

/**
 * A thread for the sound and particles for a single finger.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private int lastY = -1;
    private int i = 0;
    private PlayTone mPlayTone = new PlayTone();
    private long last_render_time;
    private boolean new_touch = true;
    private boolean spacingUp = true; /* If rotSpacing should increment, set to true.
                                         If it should decrement, set to false. */

    private long startTime;

    public static final int FRAME_REFRESH_TIME  = 10; /* Time in milliseconds to wait for rendering */

    /* Depending on the amount of touch move events handled in a x amount of time there should be
     * more particles per group and less groups. When the refresh rate is really high the particles
     * group size can even be 1 for the best results.
     */
    public static final int N_PARTICLE_GROUPS   = 600; /* Total number of particle-groups */
    public static final int PARTICLE_GROUP_SIZE = 1;  /* Number of unique particles in a single group */

    public static final int N_KEYS = 14;
    private static final int LOW_OCTAVE = 2;
    public static final float BLACK_HEIGHT = 0.6f;

    private int particleLifetime; /* Maximum life time of a single particle */
    private int particleRadiusBase; /* Value should be set after screen dimensions are known */
    private int particleRadius; /* Value of the radius based on the frequency */

    ParticleBurst[] particles = new ParticleBurst[N_PARTICLE_GROUPS];

    /**
     * Initialize all the parameters for the thread to run.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    protected void init() {
        super.init();

        /* Get the start time for the particle render delay */
        this.last_render_time = SystemClock.currentThreadTimeMillis();

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;

        /* Activate the monitor and pick a color scheme for the particles */
        monitor.setActive(true);


    }

    /**
     * Update both the sound and particles state.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    protected void update() {
        super.update();
        float freq, y_scale;

        if (this.monitor == null) {
            return;
        }

        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;


        /**
         * Set all the envelop variables to the PlayTone, the number of overtones is also set.
         */
        this.mPlayTone.setTime(SystemClock.currentThreadTimeMillis() -
                this.startTime);
        this.mPlayTone.setAttack(monitor.getAttack());
        this.mPlayTone.setDecay(monitor.getDecay());
        this.mPlayTone.setSustain(monitor.getSustain());
        this.mPlayTone.setRelease(monitor.getRelease());
        this.mPlayTone.setOvertones(monitor.getOvertones());


        /* Check whether this is a touchdown to change the color scheme */
        if (new_touch) {
            this.startTime = SystemClock.currentThreadTimeMillis();

            monitor.pickColorScheme(monitor.getParticleTheme());
            new_touch = false;

            /* Set the rotation spacing for particles */
            monitor.setRotSpacing(0);

        }

        /* Determine particle max radius. This cannot be done in the init() method
         * because the canvas size is not yet known at that time. */
        if (this.particleRadiusBase == 0) {
            this.particleRadiusBase = (monitor.getParticleCanvas().getHeight() > 0) ?
                    ((monitor.getParticleCanvas().getWidth() * 
                      monitor.getParticleCanvas().getHeight()) / 110000) : 20;
            this.particleRadius = this.particleRadiusBase;
        }

        int newX = (int)monitor.getX();
        int newY = (int)monitor.getY();

        /* Update the look of the upcoming particles based on the x position */
        setParticleParameters(monitor.getWidth(), monitor.getX());

        /* Update the rotation spacing */
        if (Math.abs(newX - this.lastX) > 1 || Math.abs(newY - this.lastY) > 1) {
            monitor.setRotSpacing(0);
            this.spacingUp = true;
        } else {
            /* Decide whether rotSpacing should go the other way */
            if (spacingUp)
                spacingUp = (monitor.getRotSpacing() >= 400) ? false : true;
            else
                spacingUp = (monitor.getRotSpacing() <= 0) ? true : false;

            /* Now increment or decrement rotSpacing */
            if (spacingUp)
                monitor.setRotSpacing(monitor.getRotSpacing() + 1);
            else
                monitor.setRotSpacing(monitor.getRotSpacing() - 1);
        }

        /* The 0 in this if statement can be changed to a higher setting if
         * it is decided that an unmoving finger should not generate particles,
         * or that vertical movement is not allowed. */
        if (Math.abs(newX - this.lastX) >= 0) {
            particles[this.i ++ % N_PARTICLE_GROUPS] =
                    new ParticleBurst(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                    this.monitor.getY(), this.particleRadius, particleLifetime,
                            monitor.getBeginColor(), monitor.getEndColor(),
                            monitor.getRotation(), monitor.getRotSpacing());
            this.lastX = newX;
        }
        this.lastY = newY;


        try {
            mPlayTone.setFreq(ToneFrequency.fromKey(this.getKey()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        renderFrame(monitor);
    }

    /**
     * Set the particle's lifetime and radius depending on the width and x-position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param width The phones screen width.
     * @param x The finger's x-position.
     */
    protected void setParticleParameters (int width, float x) {
        float div = 1 - (x / width); /* Between 0 and 1, indicator of how far
                                      * on the screen the finger is (on x-axis) */
        float ftr = (float) (div + 0.75); /* Between 0.75 and 1.75 */

        this.particleLifetime = Math.round(75 + (75 * div)); /* High frequency = long lifetime */
        this.particleRadius   = Math.round(this.particleRadiusBase * ftr);
    }

    /**
     * Start the finishing sequence of the thread. When there are still particles on the canvas
     * their movement will be finished before ending the finishing sequence.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    protected void finish() {

        new_touch = true;

        VisualMusicThreadMonitor monitor =
                (VisualMusicThreadMonitor)this.monitor;
        boolean stillAlive;

        monitor.setFinishing(true);

        this.startTime = SystemClock.currentThreadTimeMillis();
        mPlayTone.startRelease();

        while (true) {
            if (monitor.needsReboot()) {
                monitor.setFinishing(false);
                monitor.setReboot(false);
                this.going = true;
                return;
            }


            stillAlive = false;
            for (int i = 0; i < this.particles.length; i ++) {
                if (this.particles[i] == null) {
                    continue;
                }
                if (!this.particles[i].isDead()) {
                    stillAlive = true;
                    break;
                }
            }

            if (this.mPlayTone.isReleasing()) {
                this.mPlayTone.setTime(SystemClock.currentThreadTimeMillis() -
                        this.startTime);
                this.mPlayTone.sample();
                stillAlive = true;
            }

            if (!stillAlive) {
                break;
            }

            renderFrame(monitor);
        }

        this.mPlayTone.stop();
        monitor.setFinishing(false);
        monitor.setActive(false);
        super.finish();
    }

    /**
     * Prevent the thread from terminating before custom stop methods have been called,
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public void turnOff() {
        super.turnOff();
    }

    /**
     *  Render a single frame, only continues if enough time has elapsed. This time can be found
     * in FRAME_REFRESH_TIME.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param monitor the visual music thread monitor.
     */
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

    /**
     * Get the number of the pressed key.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return key number of the pressed key.
     */
    private Key getKey() {
        int octave, ySplit, y, blackHeight, key;
        float part;
        int[] blackKeys = {3, 7, 15, 19, 23};
        boolean[] isBlackKey = new boolean[28];

        octave = LOW_OCTAVE;

        ySplit = this.monitor.getHeight() / 2;
        y = this.lastY;
        if (y > ySplit)
            y -= ySplit;
        else
            octave += N_KEYS / 7;

        blackHeight = (int)(BLACK_HEIGHT * ySplit);

        for (int i = 0; i < blackKeys.length; i ++) {
            isBlackKey[blackKeys[i]] = true;
        }

        part = monitor.getWidth() / (N_KEYS * 4.0f);
        key = (int)(this.lastX / part) % (7 * 4);
        if ((y < blackHeight) && (isBlackKey[key] ||
                ((key > 0) && isBlackKey[key - 1]))) {
            if (!isBlackKey[key])
                key --;

            int i;
            for (i = 0; (i < blackKeys.length) && (blackKeys[i] != key);
                 i ++);

            octave += (this.lastX / part) / (7 * 4);

            return new Key(true, i, octave);
        }

        part = monitor.getWidth() / N_KEYS;
        key = (int)(this.lastX / part) % 7;
        octave += (this.lastX / part) / 7;
        return new Key(false, key, octave);
    }
}
