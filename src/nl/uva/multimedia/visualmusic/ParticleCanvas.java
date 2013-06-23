package nl.uva.multimedia.visualmusic;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ParticleCanvas extends SurfaceView implements
SurfaceHolder.Callback{

	//private ParticleThread thread;
	//Particles[] particles = new Particles[1500];
    private MainActivity activity;
    private VisualMusicThreadMonitor[] monitorBuffer;
    private int monitorBufferPos = 0;
    private int max_fingers = 10;

    private int fingers;
    private int circleBufferSize = VisualMusicThread.N_PARTICLE_GROUPS *
            VisualMusicThread.PARTICLE_GROUP_SIZE * 10;
    private Circle[] circleBuffer = new Circle[circleBufferSize];
    private int circleBufferPointer = 0;

	public ParticleCanvas(Context context, MainActivity activity) {
		super(context);
        this.activity = activity;
		getHolder().addCallback(this);
		setFocusable(true);
        this.monitorBuffer = new VisualMusicThreadMonitor[max_fingers];
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
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        Canvas canvas =  holder.lockCanvas();
        this.activity.setSurfaceHolder(holder);
        this.activity.setCanvas(canvas);
    }

    public void activateFinger(){
        this.fingers++;
    }

    public void deactivateFinger(){
        this.fingers--;
    }

    public void addMonitorBuffer(VisualMusicThreadMonitor monitor){
        if(this.monitorBufferPos >= max_fingers)
            return;

        this.monitorBuffer[this.monitorBufferPos++] = monitor;
    }

    private void emptyAndActivateBuffers(){
        for(int i = 0; i < this.monitorBuffer.length; i++){
            if(this.monitorBuffer[i] != null){
                this.monitorBuffer[i].activateWrite();
                this.monitorBuffer[i] = null;
            }
        }
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        this.circleBuffer[this.circleBufferPointer ++] = new Circle(cx, cy,
                radius, paint);
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
