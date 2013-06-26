package nl.uva.multimedia.visualmusic;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.util.Log;

/**
 * Created by klaplong on 6/19/13.
 */
public class PlayTone {
    private static final String TAG = "PlayTone";

    private final int sampleRate = 44100;
    private final int bufferSize = sampleRate / 50;

    private AudioTrack mAudio;
    private int sampleCount = bufferSize;
    private PresetReverb effect = null;

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
//        effect = new PresetReverb(0, mAudio.getAudioSessionId());
//        effect.setPreset(PresetReverb.PRESET_LARGEHALL);
//        effect.setEnabled(true);
//        mAudio.attachAuxEffect(effect.getId());
//        mAudio.setAuxEffectSendLevel(1.0f);
    }

    public void play() {
//        mAudio.reloadStaticData();
//        mAudio.setLoopPoints(0, this.sampleCount, -1);
//        mAudio.play();
    }

    public void stop() {
        if (mAudio.getState() == AudioTrack.STATE_UNINITIALIZED) {
            return;
        }

        /* Write an empty buffer. */
        mAudio.write(new byte[this.bufferSize], 0, this.bufferSize);
    }

    public void setFreq(float freq, float scale) {
        scale *= 0.6;

        try {
            int x = (int)((double)bufferSize * freq / sampleRate);
            this.sampleCount = (int)((double)x * sampleRate / freq);

            byte[] samples = new byte[this.sampleCount];

            for( int i = 0; i != this.sampleCount; i ++) {
                double a, t, adt, f;

                a = 1.0 / freq;
                t = (double)i * (1.0 / sampleRate);
                adt = t / a;

                /* The waves. */
                f = scale * Math.sin(t * 2 * Math.PI * freq);
                f += (0.6 - scale) * 2 * (adt - (int)((1.0 / 2.0) + adt));

                /* Overtones. */
                f += 0.1 * Math.sin(t * 2 * Math.PI * 2 * freq);
                f += 0.1 * Math.sin(t * 2 * Math.PI * 3 * freq);
                f += 0.1 * Math.sin(t * 2 * Math.PI * 4 * freq);
                f += 0.1 * Math.sin(t * 2 * Math.PI * 5 * freq);

                samples[i] = (byte)(f * 127);
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
}
