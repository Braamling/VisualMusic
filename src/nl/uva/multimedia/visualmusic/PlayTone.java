package nl.uva.multimedia.visualmusic;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
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

    public PlayTone() {
        mAudio = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                bufferSize,
                AudioTrack.MODE_STATIC);
    }

    public void play() {
        mAudio.reloadStaticData();
        mAudio.setLoopPoints(0, this.sampleCount, -1);
        mAudio.play();
    }

    public void stop() {
        mAudio.pause();
        mAudio.flush();
        mAudio.stop();
        mAudio.release();
    }

    public void setFreq(double freq) {
        try {
            int x = (int)((double)bufferSize * freq / sampleRate);
            this.sampleCount = (int)((double)x * sampleRate / freq);

            byte[] samples = new byte[this.sampleCount];

            for( int i = 0; i != this.sampleCount; i ++) {
                double t = (double)i * (1.0 / sampleRate);
                double f = Math.sin(t * 2 * Math.PI * freq);

                samples[i] = (byte)(f * 127);
            }

            mAudio.write(samples, 0, this.sampleCount);
            mAudio.setLoopPoints(0, this.sampleCount, -1);
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
