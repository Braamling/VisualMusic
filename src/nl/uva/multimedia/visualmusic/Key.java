package nl.uva.multimedia.visualmusic;

/**
 * A key for the synthesizer.
 */
public class Key {
    private static final float[] WHITE_FREQUENCIES =
            {16.35f, 18.35f, 20.60f, 21.82f, 24.49f, 27.50f, 30.86f};
    private static final float[] BLACK_FREQUENCIES =
            {17.32f, 19.44f, 23.12f, 25.85f, 29.13f};

    private boolean black;
    private int key, octave;

    /**
     * Constructor.
     * @param black Whether the key is black.
     * @param key The keynumber (0-7 for white keys, 0-28 for black keys).
     * @param octave The octave on the keyboard.
     */
    public Key(boolean black, int key, int octave) {
        this.black = black;
        this.key = key;
        this.octave = octave;
    }

    /**
     * Check whether the key is black.
     * @return Whether the key is black.
     */
    public boolean isBlack() {
        return this.black;
    }

    /**
     * Get the key number.
     * @return The key number.
     */
    public int getKey() {
        return this.key;
    }

    /**
     * Get the key's octave.
     * @return The octave.
     */
    public int getOctave() {
        return this.octave;
    }

    /**
     * Calculate the key's frequency.
     * @return The frequency.
     */
    public float getFrequency() {
        float frequency;

        if (this.black)
            frequency = BLACK_FREQUENCIES[this.key];
        else
            frequency = WHITE_FREQUENCIES[this.key];

        for (int i = 0; i < this.octave; i ++)
            frequency *= 2;

        return frequency;
    }
}
