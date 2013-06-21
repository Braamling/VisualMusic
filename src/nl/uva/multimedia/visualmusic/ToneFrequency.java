package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 2013-06-21.
 */
public class ToneFrequency {
    private static final float[] frequencies =
            {16.35f, 17.32f, 18.35f, 19.44f, 20.60f, 21.82f, 23.12f, 24.49f,
            25.85f, 27.50f, 29.13f, 30.86f};
    public static final int N_KEYS = 12;

    private float frequency;

    ToneFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float get() {
        return this.frequency;
    }

    public void set(float frequency) {
        this.frequency = frequency;
    }

    public static ToneFrequency fromKey(int key, int octave)
            throws NotAKeyException, NotAnOctaveException {
        if ((key < 0) || (key >= 12))
            throw new NotAKeyException();

        if (octave < 0)
            throw new NotAnOctaveException();

        float frequency = frequencies[key];
        for (int i = 0; i < octave; i ++)
            frequency *= 2;

        return new ToneFrequency(frequency);
    }
}
