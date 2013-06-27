package nl.uva.multimedia.visualmusic;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.util.Log;

/**
 * A Class to generate tones and play them.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class PlayTone {
    private static final String TAG = "PlayTone";

    private final int sampleRate = 44100;
    private final int bufferSize = sampleRate / 50;

    private int attack = 250;
    private int decay = 250;
    private float sustain = 0.7f;
    private int release = 250;

    private int overtones = 8;

    private AudioTrack mAudio;
    private int sampleCount = bufferSize;
    private PresetReverb effect = null;
    private float freq;
    private long time;
    private boolean releasing;

    /**
     * Initialize the AudioTrack and write an empty buffer.
     */
    public PlayTone() {
        mAudio = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                bufferSize,
                AudioTrack.MODE_STATIC
        );
        this.stop();
    }

    /**
     * Fill up the buffer with zero's, this stops the music.
     */
    public void stop() {
        if (mAudio.getState() == AudioTrack.STATE_UNINITIALIZED) {
            return;
        }

        /* Write an empty buffer. */
        mAudio.write(new byte[this.bufferSize], 0, this.bufferSize);
    }


    public void sample() {
        this.setFreq(this.freq, 0);
    }

    /**
     * Fill the buffer with a generated sinoïd and start playing.
     * @param freq the frequency of the sinoïd.
     * @param scale the amount of saw sinoïd present in the resulting sinoïd.
     */
    public void setFreq(float freq, float scale) {
        float amplitude = 0.0f;

        this.freq = freq;

        if (this.releasing) {
            if (this.time >= this.release) {
                this.stop();
                this.releasing = false;
            }
            else
                amplitude = this.sustain - (this.sustain / this.release) * this.time;
        }
        else if (this.time < this.attack) {
            amplitude = (1.0f / this.attack) * this.time;
        }
        else if (this.time < (this.attack + this.decay)) {
            amplitude = 1.0f - ((1.0f - this.sustain) / this.decay) *
                    (this.time - this.attack);
        }
        else {
            amplitude = this.sustain;
        }

        try {
            int x = (int)((double)bufferSize * freq / sampleRate);
            this.sampleCount = (int)((double)x * sampleRate / freq);

            byte[] samples = new byte[this.sampleCount];

            for( int i = 0; i < this.sampleCount; i ++) {
                double a, t, adt, f;

                a = 1.0 / freq;
                t = (double)i * (1.0 / sampleRate);
                adt = t / a;

                /* The waves. */
                double fPart = 1.0 - 0.1 * this.overtones;
                f = 0.8 * fPart * Math.sin(t * 2 * Math.PI * freq);
                f += 0.2 * fPart * (adt - (int)(0.5 + adt));

                /* Overtones. */
                for (int j = 0; j < this.overtones; j ++) {
                    adt = t / (1.0 / (freq * (j + 2)));
                    f += 0.08 * Math.sin(t * 2 * Math.PI * (j + 2) * freq);
                    f += 0.04 * (adt - (int)(0.5 + adt));
                }

                samples[i] = (byte)(f * 127 * amplitude);
            }

            if (mAudio.getState() == AudioTrack.STATE_INITIALIZED)
                mAudio.play();
            mAudio.write(samples, 0, samples.length);
            mAudio.setLoopPoints(0, this.sampleCount, -1);
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the attack of the tone.
     * @param attack the attack.
     */
    public void setAttack(int attack){
        this.attack = attack;
    }

    /**
     * Set the decay of the tone.
     * @param decay the decay.
     */
    public void setDecay(int decay){
        this.decay = decay;
    }

    /**
     * Set the sustain of the tone.
     * @param sustain the sustain.
     */
    public void setSustain(float sustain){
        this.sustain = sustain;
    }

    /**
     * set the release of the tone.
     * @param release the release.
     */
    public void setRelease(int release){
        this.release = release;
    }

    /**
     * Set the number of overtones.
     * @param overtones the number of overtones.
     */
    public void setOvertones(int overtones){
        this.overtones = overtones;
    }

    /**
     * Set how long the finger currently has been touching the screen.
     * @param time The time in ms.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Set the release boolean to true indicating that the finger has been released.
     */
    public void startRelease() {
        this.releasing = true;
    }

    /**
     * Get a boolean indicating if the tone is being released
     * @return a true or false boolean with the status of the tone.
     */
    public boolean isReleasing() {
        return this.releasing;
    }
}
