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
    private int i = 0;
//    private PlayTone mPlayTone = null;



    private final int PARTICLE_AMOUNT = 20;

    Particles[] particles = new Particles[PARTICLE_AMOUNT];

    protected void init() {
        Log.e("new thread", "new Thread");
        super.init();
        // Init.
//        Log.v(TAG, "Finger! =D");
//        mPlayTone = new PlayTone();
        // mPlayTone.play();

    }

    protected void update() {
        super.update();

        if (this.monitor == null)
            return;

        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;
        int newX = (int)monitor.getX();
        if (newX != this.lastX) {
             particles[this.i++ % (PARTICLE_AMOUNT-1)] =
                     new Particles(20, this.monitor.getX(),this.monitor.getY(), 5, 5,500);

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

        renderFrame(monitor);
    }

    protected void finish() {


        VisualMusicThreadMonitor monitor = (VisualMusicThreadMonitor)this.monitor;
        int time = 0;
        while(time++ < 20){
            renderFrame(monitor);

        }
        Log.e("finished", "finished");
        for(int j = 0; j < particles.length; j++){
            particles[j] = null;
        }
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

    public void renderFrame(VisualMusicThreadMonitor monitor){
        Canvas canvas = null;

        try{
            SurfaceHolder holder = monitor.getSurfaceHolder();
            canvas = holder.lockCanvas();


            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            if(canvas == null)
                return;

            synchronized (holder) {
                canvas.drawColor(Color.BLACK);

                for(int i = 0; i < particles.length; i++){
                    if(particles[i] != null){
                        if(particles[i].isDead()){
                            particles[i] = null;
                        }

                        particles[i].update();
                        particles[i].render(canvas, holder);
                        canvas.save();
                        canvas.restore();

                    }
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }catch (IllegalMonitorStateException e) {

        }finally {
            if (canvas != null) {

            }
        }
    }



    private int getKey(){
        float part, key;

        part = monitor.getWidth() / ToneFrequency.N_KEYS;
        key = lastX / part;

        return (int) key;
    }
}
