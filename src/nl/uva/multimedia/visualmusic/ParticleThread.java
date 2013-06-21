package nl.uva.multimedia.visualmusic;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
 
@SuppressLint("WrongCall")
public class ParticleThread extends Thread {
		private SurfaceHolder surfaceHolder;
		private ParticleCanvas gamePanel;
		private boolean running;

		public void setRunning(boolean running) {
			this.running = running;
		}

		public ParticleThread(SurfaceHolder surfaceHolder, ParticleCanvas gamePanel) {
			super();
			this.surfaceHolder = surfaceHolder;
			this.gamePanel = gamePanel;
		}

		@Override
		public void run() {
			Canvas canvas;

			while (running) {
				canvas = null;
				// try locking the canvas for exclusive pixel editing
				// in the surface
				try {
					canvas = this.surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						this.gamePanel.update();
						this.gamePanel.render(canvas);				
					
					}
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				//SystemClock.sleep(200);
			}
		}

}