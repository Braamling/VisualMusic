package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by klaplong on 2013-06-26.
 */
public class ParticleCanvasThread extends Thread {
    private ParticleCanvas mParticleCanvas;
    private boolean running;

    public void setParticleCanvas(ParticleCanvas particleCanvas) {
        this.mParticleCanvas = particleCanvas;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        this.running = true;
        while (this.running) {
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
