package nl.uva.multimedia.visualmusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ParticleCanvas extends SurfaceView implements
SurfaceHolder.Callback{

	private ParticleThread thread;
	Particle_group[] particles = new Particle_group[1500];


	public ParticleCanvas(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// create the game loop thread
		thread = new ParticleThread(getHolder(), this);
		
		for(int i = 0; i < particles.length; i++)
			particles[i] = new Particle_group(10, 10*i,500, 5, 5,500);
		
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.setRunning(false);
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}
	
	
	
	public void render(Canvas canvas) {

		if(canvas == null)
            return;
		
		canvas.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		for(int i = 0; i < particles.length; i++){
            if(particles[i].isDead()){
                particles[i] = null;
            }else if(particles[i] != null){
				particles[i].update();
				particles[i].render(canvas);
			}
					

			
		}
		

		//canvas.drawCircle((float)400.0,(float)400.0,(float)400.0, paint);
		
	}

	public void update() {
	
	}
}
