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

	//private ParticleThread thread;
	//Particles[] particles = new Particles[1500];
    private MainActivity activity;


	public ParticleCanvas(Context context, MainActivity activity) {
		super(context);
        this.activity = activity;
		getHolder().addCallback(this);
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        Canvas canvas =  holder.lockCanvas();

        canvas.drawCircle(500,500, 300, paint);
        Log.e("holder", holder + " " + canvas);
        Canvas canvas1 = canvas;
        Log.e("holder", holder + " " + canvas1);


        this.activity.setSurfaceHolder(holder);
        this.activity.setCanvas(canvas);

		// create the game loop thread
//		thread = new ParticleThread(getHolder(), this);
//
//		for(int i = 0; i < particles.length; i++)
//			particles[i] = new Particles(10, 10*i,500, 5, 5,500);
//
//		// at this point the surface is created and
//		// we can safely start the game loop
//		thread.setRunning(true);
//		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
//		boolean retry = true;
//		while (retry) {
//			try {
////				thread.setRunning(false);
////				thread.join();
//				retry = false;
//			} catch (InterruptedException e) {
//				// try again shutting down the thread
//			}
//		}
	}
	
	
	
//	public void render(Canvas canvas) {
//        if(canvas == null)
//            return;
//
//		canvas.drawColor(Color.BLACK);
//
//		Paint paint = new Paint();
//		paint.setColor(Color.GREEN);
//		for(int i = 0; i < particles.length; i++){
//			if(particles[i] != null){
//				particles[i].update();
//				particles[i].render(canvas);
//			}
//
//			if(particles[i].isDead()){
//				particles[i] = null;
//			}
//
//		}
		

		//canvas.drawCircle((float)400.0,(float)400.0,(float)400.0, paint);
		
//	}

	public void update() {
	
	}

}
