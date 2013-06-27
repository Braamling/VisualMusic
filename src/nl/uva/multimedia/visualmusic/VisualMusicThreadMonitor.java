package nl.uva.multimedia.visualmusic;

import android.graphics.Color;

import java.util.Random;

/**
 * A monitor to store and manage all the values of the VisualMusicThread.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class VisualMusicThreadMonitor extends FingerThreadMonitor {
    private static final String TAG = "VisualMusicThreadMonitor";

    private ParticleCanvas particleCanvas;
    private boolean active = false, finishing = false, reboot = false;
    private ParticleBurst[] particles = null;

    private int begin_color;
    private int end_color;
    private int rotation = 360; /* Should range from 90 to 720 */
    private int rotSpacing; /* Increments with every thread update, indicates
                             * the spacing for the particle rotations */
    private int theme;

    private int attack = 250;
    private int decay = 250;
    private float sustain = 0.7f;
    private int release = 250;
    private int overtones = 8;

    /**
     * Initialize the visual music thread without any values.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public VisualMusicThreadMonitor() {
        super();

        this.fingerId = -1;
    }

    /**
     * Initialize the visual music thread with just a fingerId.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public VisualMusicThreadMonitor(int fingerId) {
        super();

        this.fingerId = fingerId;
    }

    /**
     * Initialize the visual music thread with both a finger id and x and y position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public VisualMusicThreadMonitor(float x, float y, int fingerId) {
        super(x, y);

        this.fingerId = fingerId;
    }

    /**
     * Set the particle canvas object.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param particleCanvas The particle canvas to draw the particles on.
     */
    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.particleCanvas = particleCanvas;
    }


    /**
     * Get the particle canvas object.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The particle canvas to draw the particles on.
     */
    public ParticleCanvas getParticleCanvas() {
        return this.particleCanvas;
    }

    /**
     * Set a boolean to determent whether the thread is finishing or not.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param finishing True when the thread is finishing and false when the thread is not finishing.
     */
    public void setFinishing(boolean finishing) {
        this.finishing = finishing;
    }

    /**
     * Determent if a thread is finishing.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return True when the thread is finishing and false when the thread is not finishing.
     */
    public boolean isFinishing() {
        return this.finishing;
    }

    /**
     * Set if a thread needs rebooting
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param reboot True when the threads has to reboot and false when should no reboot.
     */
    public void setReboot(boolean reboot) {
        this.reboot = reboot;
    }

    /**
     * Determent if a thread needs rebooting
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return True when the threads has to reboot and false when should no reboot.
     */
    public boolean needsReboot() {
        return this.reboot;
    }

    /**
     * Set the particle burst array to use in visual music thread.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param particles The particle burst array to use in visual music thread
     */
    public void setParticles(ParticleBurst[] particles) {
        this.particles = particles;
    }

    /**
     * Get the particle burst array to use in visual music thread.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The particle burst array to use in visual music thread.
     */
    public ParticleBurst[] getParticles() {
        return this.particles;
    }

    /**
     * Activate the visual music thread.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param active True when active, false when inactive.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Determent whether a visual music thread is active.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return True when active, false when inactive.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Pick a color scheme (begin and end color) for the individual particles in a particle burst.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public void pickColorScheme() {
        pickColorScheme(5);
    }

    /**
     * Pick a color scheme (begin and end color) for the individual particles in a particle burst.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param group: determines what subset of colors to pick colors from
     */
    public void pickColorScheme(int group) {
        int id;
        Random r = new Random();
        if (group == 0)
            id = (r.nextInt(10) + 0); /* All colors */
        else if (group == 1)
            id = (r.nextInt(2) + 0); /* Smoke + potion */
        else if (group == 2)
            id = (r.nextInt(2) + 2); /* Fire + water */
        else if (group == 3)
            id = (r.nextInt(4) + 1); /* Potion + fire + water + silver */
        else if (group == 4)
            id = (r.nextInt(3) + 4); /* Silver + gold + yellow-white */
        else
            id = (r.nextInt(3) + 7); /* Pink + purple + red */

        this.begin_color = createBeginColor(id);
        this.end_color   = createEndColor(id);
    }

    /**
     * Create an end color based on the given id value.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param id An id between 0 and 9. All other values will black.
     * @return An integer representing and argb color.
     */
    private int createEndColor(int id){
        switch(id) {
            case 0:
                return Color.argb(255, 20, 15, 64); /* Dark blue */ /* SMOKE */
            case 1:
                return Color.argb(255, 0, 204, 102); /* Aqua green */ /* POTION */
            case 2:
                return Color.argb(255, 255, 52, 0); /* Dark orange */ /* FIRE */
            case 3:
                return Color.argb(255, 0, 94, 255); /* Medium blue */ /* WATER */
            case 4:
                return Color.argb(255, 232, 232, 232); /* Almost white */ /* SILVER */
            case 5:
                return Color.argb(255, 150, 89, 0); /* Brown */ /* GOLD */
            case 6:
                return Color.argb(255, 255, 255, 0); /* Perfect yellow */ /* YELLOW WHITE */
            case 7:
                return Color.argb(255, 255, 0, 255); /* Purple-pink */ /* PINK */
            case 8:
                return Color.argb(255, 255, 0, 72); /* Purple-red */ /* PURPLE */
            case 9:
                return Color.argb(255, 255, 0, 0); /* Perfect red */ /* RED */
            default:
                return Color.argb(255, 255, 52, 0); /* Dark orange */
        }
    }

    /**
     * Create a begin color based on the given id value.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param id An id between 0 and 9. All other values will green.
     * @return An integer representing and argb color.
     */
    private int createBeginColor(int id){
        switch(id){
            case 0:
                return Color.argb(255, 85, 179, 185); /* Type of light blue */ /* SMOKE */
            case 1:
                return Color.argb(255, 193, 222, 31); /* Light yellow / green */ /* POTION */
            case 2:
                return Color.argb(255, 255, 255, 0); /* Yellow */ /* FIRE */
            case 3:
                return Color.argb(255, 0, 201, 255); /* Nearing aqua */ /* WATER */
            case 4:
                return Color.argb(255, 142, 145, 148); /* Gray (with slight blue) */ /* SILVER */
            case 5:
                return Color.argb(255, 232, 217, 0); /* Kind of yellow */ /* GOLD */
            case 6:
                return Color.argb(255, 255, 255, 255); /* White */ /* YELLOW WHITE */
            case 7:
                return Color.argb(255, 255, 0, 0); /* Red */ /* PINK */
            case 8:
                return Color.argb(255, 131, 117, 255); /* Violet */ /* PURPLE */
            case 9:
                return Color.argb(255, 40, 40, 40); /* Dark gray */ /* RED */
            default:
                return Color.argb(255, 255, 255, 0); /* Yellow */
        }
    }

    /**
     * Get the generated end color.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return An integer representing an argb end color.
     */
    public int getEndColor(){
        return this.end_color;
    }

    /**
     * Get the generated begin color.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return An integer representing the rotation.
     */
    public int getBeginColor(){
        return this.begin_color;
    }

    /**
     * Get the generated rotation.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return An integer representing the rotation.
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Set the size of the rotation.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The size of the rotation. Range from 0 - 300.
     */
    public int getRotSpacing() {
        return this.rotSpacing;
    }

    /**
     * Set the size of the rotation.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param r The size of the rotation. Range from 0 - 300.
     */
    public void setRotSpacing(int r) {
        this.rotSpacing = r;
    }

    /**
     * Get the theme of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param group the theme value from the radio button. Integer between 0 and 5.
     */
    public void setParticleTheme(int group){
        this.theme = theme;
    }

    /**
     * Get the theme of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return the theme value from the radio button. Integer between 0 and 5.
     */
    public int GetParticleTheme(){
        return this.theme;
    }

    /**
     * Set the attack of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * get the attack of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public int getAttack() {
        return this.attack;
    }

    /**
     * Set the decay of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public void setDecay(int decay) {
        this.decay = decay;
    }

    /**
     * get the decay of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public int getDecay() {
        return this.decay;
    }

    /**
     * Set the sustain of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public void setSustain(float sustain) {
        this.sustain = sustain;
    }

    /**
     * get the sustain of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public float getSustain() {
        return this.sustain;
    }

    /**
     * Set the release of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public void setRelease(int release) {
        this.release = release;
    }

    /**
     * get the release of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public int getRelease() {
        return this.release;
    }

    /**
     * Set the number of overtones of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public void setOvertones(int overtones) {
        this.overtones = overtones;
    }

    /**
     * get the number of overtones of a tone.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     */
    public int getOvertones() {
        return this.overtones;
    }

}