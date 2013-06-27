package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 2013-06-21.
 */
public class ToneFrequency {
    private static final float[] WHITE_FREQUENCIES =
            {16.35f, 18.35f, 20.60f, 21.82f, 24.49f, 27.50f, 30.86f};
    private static final float[] BLACK_FREQUENCIES =
            {17.32f, 19.44f, 23.12f, 25.85f, 29.13f};

    public static float fromKey(Key key) {
        float frequency;

        if (key.isBlack())
            frequency = BLACK_FREQUENCIES[key.getKey()];
        else
            frequency = WHITE_FREQUENCIES[key.getKey()];

        for (int i = 0; i < key.getOctave(); i ++)
            frequency *= 2;

        return frequency;
    }
}
