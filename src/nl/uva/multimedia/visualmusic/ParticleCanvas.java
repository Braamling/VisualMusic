package nl.uva.multimedia.visualmusic;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ParticleCanvas extends SurfaceView
        implements SurfaceHolder.Callback{

    private static final String TAG = "ParticleCanvas";

    private MainActivity activity;
    private int max_fingers = MainActivity.N_FINGER_THREADS;
    private SurfaceHolder holder;

    private int nFingers, cFingers;
    private int circleBufferSize = VisualMusicThread.N_PARTICLE_GROUPS *
            VisualMusicThread.PARTICLE_GROUP_SIZE * max_fingers;
    private Circle[] circleBuffer = new Circle[circleBufferSize + 1];
    private int circleBufferPointer = 0;

    private VisualMusicThreadMonitor[] monitors =
            new VisualMusicThreadMonitor[max_fingers];

	public ParticleCanvas(Context context, MainActivity activity) {
		super(context);
        this.activity = activity;
		getHolder().addCallback(this);
		setFocusable(true);
	}

    public void setMonitors(FingerHandler handler) {
        for (int i = 0; i < max_fingers; i ++) {
            try {
                this.monitors[i] =
                        (VisualMusicThreadMonitor)handler.getMonitor(i);
                this.monitors[i].setParticleCanvas(this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
	public void surfaceCreated(SurfaceHolder holder) {
        //Paint paint = new Paint();
        //paint.setColor(Color.GREEN);
        //Canvas canvas =  holder.lockCanvas();
        //this.activity.setSurfaceHolder(holder);
        //this.activity.setCanvas(canvas);

        this.holder = holder;
    }

    public void addFinger() {
        this.nFingers ++;
        if (this.nFingers > this.max_fingers)
            this.nFingers = this.max_fingers;

        if (this.nFingers == 1)
            this.startBuffering();
    }

    public void removeFinger() {
        this.nFingers --;

        // StackTraceElement el =
        //         Thread.currentThread().getStackTrace()[3];
        // Log.v(TAG, el.getFileName() + ": " + el.getLineNumber());
    }

    public void fingerBuffered() {
        this.cFingers --;

        if (this.cFingers == 0) {
            this.drawBuffer();
            this.startBuffering();
        }
    }

    private void startBuffering() {
        if (this.nFingers == 0)
            return;

        this.cFingers = this.nFingers;
        for (int i = 0; i < max_fingers; i ++) {
            this.monitors[i].setDraw(true);
        }
    }

    private void drawBuffer() {
        Canvas canvas = this.holder.lockCanvas();
        canvas.drawColor(Color.BLACK);

        /* Draw keys. */
        float keyWidth;
        keyWidth = this.getWidth() / 18;
        Paint white;
        white = new Paint();
        white.setColor(Color.WHITE);
        for (int i = 1; i < 18; i ++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, this.getHeight(),
                    white);
        }

        for (; this.circleBufferPointer > 0; this.circleBufferPointer --) {
            Circle c = this.circleBuffer[this.circleBufferPointer - 1];

            if(c != null && c.getPaint() != null){
                canvas.drawCircle(c.getCx(), c.getCy(), c.getRadius(),
                        c.getPaint());
                this.circleBuffer[this.circleBufferPointer - 1] = null;
            }
        }

        canvas.save();
        canvas.restore();
        this.holder.unlockCanvasAndPost(canvas);
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        this.circleBuffer[(this.circleBufferPointer ++) %
                circleBufferSize] = new Circle(cx, cy, radius, paint);
    }

    public void flushBuffer() {
        for (; this.circleBufferPointer > 0; this.circleBufferPointer --)
            this.circleBuffer[this.circleBufferPointer - 1] = null;
    }
}

class Circle {
    private float cx, cy, radius;
    private Paint paint;

    public Circle(float cx, float cy, float radius, Paint paint) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.paint = paint;
    }

    public float getCx() {
        return this.cx;
    }

    public float getCy() {
        return this.cy;
    }

    public float getRadius() {
        return this.radius;
    }

    public Paint getPaint() {
        return this.paint;
    }
}
