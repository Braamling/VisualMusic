package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import nl.uva.multimedia.visualmusic.ToneFrequency;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
//    private PlayTone mPlayTone = null;



    private final int PARTICLE_AMOUNT = 300;

    Particles[] particles = new Particles[PARTICLE_AMOUNT];

    protected void init() {
        super.init();
        // Init.
//        Log.v(TAG, "Finger! =D");
//        mPlayTone = new PlayTone();
        // mPlayTone.play();

        for(int i = 0; i < particles.length; i++)
            particles[i] = new Particles(10, 10*i,500, 5, 5,500);
    }

    protected void update() {
        super.update();

        Canvas canvas;

        if (this.monitor == null)
            return;

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;
        int newX = (int)monitor.getX();
        if (newX != this.lastX) {
//            this.lastX = newX;
//            Log.v(TAG, "Finger moved (" + newX + ")! =O");
//            /* This is where the playtone's hertz is adjusted.*/
//            float freq;
//            try {
//                ToneFrequency newFrequency = ToneFrequency.fromKey(getKey(), 4);
//                freq = newFrequency.get();
//                mPlayTone.setFreq((double)freq);
//            }catch (Exception e){
//
//            }
//
//            this.mPlayTone.play();


        }

            // levert null op :( WAAROM!? :(
        //canvas = monitor.getSurfaceHolder().lockCanvas();

        SurfaceHolder surface = monitor.getSurfaceHolder();
        canvas = monitor.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        canvas.drawCircle((float)300.0,(float)300.0,(float)30.0,paint);

        synchronized (surface) {
            if(canvas == null)
                return;

            canvas.drawColor(Color.BLACK);


            //werkt niet :( WAAROM?! :(
            canvas.drawCircle((float)300.0,(float)300.0,(float)30.0,paint);

            for(int i = 0; i < particles.length; i++){
                if(particles[i] != null){
                    if(particles[i].isDead()){
                        particles[i] = null;
                    }

                    particles[i].update();
                    particles[i].render(canvas);
                }
            }
        }
    }

    protected void finish() {
        super.finish();
//        mPlayTone.stop();
//        mPlayTone = null;
//        // Finish.
//        Log.v(TAG, "No finger! =(");
    }

    public void turnOff() {
        /* To prevent the thread from terminating before custom stop methods
         * have been called, do your own stuff before the super turnoff. */
        super.turnOff();
    }



    private int getKey(){
        float part, key;

        part = monitor.getWidth() / ToneFrequency.N_KEYS;
        key = lastX / part;

        return (int) key;
    }
}
