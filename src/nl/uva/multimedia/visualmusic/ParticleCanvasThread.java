package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by klaplong on 2013-06-26.
 */
public class ParticleCanvasThread extends Thread {
    private ParticleCanvas mParticleCanvas;
    private boolean running;

    public static final int FRAME_REFRESH_TIME  = 10; /* Time in milliseconds to wait for rendering */

    private long lastRenderTime;


    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.mParticleCanvas = particleCanvas;
    }

    public void setRunning(boolean running) {
        this.running = running;

    }

    public void run() {
        this.running = true;
        while (this.running) {
            if(SystemClock.currentThreadTimeMillis() - lastRenderTime > this.FRAME_REFRESH_TIME){
                lastRenderTime = SystemClock.currentThreadTimeMillis();
                Canvas canvas;

                this.mParticleCanvas.gatherParticles();

                canvas = this.mParticleCanvas.holder.lockCanvas();
                if (canvas == null)
                    return;

                canvas.drawColor(Color.BLACK);

                this.mParticleCanvas.drawKeys(canvas);
                this.mParticleCanvas.drawParticles(canvas);
            }
        }
    }

    public long getFrameRefreshTime(){
        return this.FRAME_REFRESH_TIME;
    }
}
