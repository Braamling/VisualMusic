package nl.uva.multimedia.visualmusic;

import android.util.Log;

/**
 * Created by klaplong on 6/20/13.
 */
public class VisualMusicThread extends FingerThread {
    private static final String TAG = "VisualMusicThread";

    private int lastX = -1;
    private int i = 0;
//    private PlayTone mPlayTone = null;

    public static final int N_PARTICLE_GROUPS = 20;
    public static final int PARTICLE_GROUP_SIZE = 20;
    private static final int PARTICLE_AMOUNT = 20;

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
                     new Particles(PARTICLE_GROUP_SIZE, this.monitor.getX(),
                     this.monitor.getY(), 5, 5,500);

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
        /*while(time++ < 20){
            renderFrame(monitor);

        }*/
        Log.e(TAG, "finished");
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

    public void renderFrame(VisualMusicThreadMonitor monitor) {
        // Log.e(TAG, "begin render");
        if (!monitor.canDraw())
            return;
        Log.e(TAG, "can draw");
        try {
            /*SurfaceHolder holder = monitor.getSurfaceHolder();
            canvas = holder.lockCanvas();


            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            if(canvas == null)
                return;

            synchronized (holder) {
                canvas.drawColor(Color.BLACK);
*/
            for(int i = 0; i < particles.length; i++){
                if(particles[i] != null){
                    if(particles[i].isDead()){
                        particles[i] = null;
                    }

                    particles[i].update();
                    particles[i].render(monitor.getParticleCanvas());
                    //canvas.save();
                    //canvas.restore();

                }
            }
            Log.e(TAG, "rendered");
            monitor.deactivateWrite();
            Log.e(TAG, "deactivated write");
            monitor.getParticleCanvas().bufferMonitor(monitor);
            /*}
            holder.unlockCanvasAndPost(canvas);*/
        }
        catch (IllegalMonitorStateException e) {

        }
        finally {
        }
    }



    private int getKey(){
        float part, key;

        part = monitor.getWidth() / ToneFrequency.N_KEYS;
        key = lastX / part;

        return (int) key;
    }
}
