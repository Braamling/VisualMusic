package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;

/**
 * The thread for the particle canvas.
 */
public class ParticleCanvasThread extends Thread {
    private ParticleCanvas mParticleCanvas;
    private boolean running;
    private long lastRenderTime;

    /**
     * Rendering refresh time.
     */
    public static final int FRAME_REFRESH_TIME  = 10; /* Time in milliseconds to wait for rendering */

    /**
     * Constructor, assigns canvas.
     * @param particleCanvas The canvas.
     */
    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.mParticleCanvas = particleCanvas;
    }

    /**
     * Set whether the thread is running.
     * @param running The state.
     */
    public void setRunning(boolean running) {
        this.running = running;

    }

    /**
     * The run loop. Renders the particles.
     */
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

                canvas.drawColor(Color.rgb(70, 70, 70));

                this.mParticleCanvas.drawKeys(canvas);
                this.mParticleCanvas.drawParticles(canvas);
            }
        }
    }

    /**
     * Get the frame refresh time.
     * @return The time.
     */
    public long getFrameRefreshTime(){
        return this.FRAME_REFRESH_TIME;
    }
}
