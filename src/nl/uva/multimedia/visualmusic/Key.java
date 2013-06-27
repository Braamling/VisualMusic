package nl.uva.multimedia.visualmusic;

/**
 * Created by klaplong on 2013-06-27.
 */
public class Key {
    private boolean black;
    private int key, octave;

    public Key(boolean black, int key, int octave) {
        this.black = black;
        this.key = key;
        this.octave = octave;
    }

    public boolean isBlack() {
        return this.black;
    }

    public int getKey() {
        return this.key;
    }

    public int getOctave() {
        return this.octave;
    }
}
