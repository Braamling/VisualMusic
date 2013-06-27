package nl.uva.multimedia.visualmusic;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Particle canvas controls all rendering of the particles and keys to the canvas.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class ParticleCanvas extends SurfaceView
        implements SurfaceHolder.Callback{

    private static final String TAG = "ParticleCanvas";

    private MainActivity activity;
    private int max_fingers = MainActivity.N_FINGER_THREADS;
    public SurfaceHolder holder;

    private int nFingers, cFingers;
    private int circleBufferSize = VisualMusicThread.N_PARTICLE_GROUPS *
            VisualMusicThread.PARTICLE_GROUP_SIZE * max_fingers;
    private Circle[] circleBuffer = new Circle[circleBufferSize + 1];
    private int circleBufferPointer = 0;

    private Paint whitePaint = new Paint();
    private Paint textPaint = new Paint();

    private VisualMusicThreadMonitor[] monitors =
            new VisualMusicThreadMonitor[max_fingers];

    private ParticleCanvasThread mThread = null;

    /**
     * Initialize the particle canvas with the main activity.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param activity the context for communicating back to the main activity.
     */
    public ParticleCanvas(MainActivity activity) {
        super(activity);

        this.whitePaint.setColor(Color.WHITE);
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(10.0f);
        this.textPaint.setTextAlign(Paint.Align.CENTER);

        this.activity = activity;
        getHolder().addCallback(this);
        setFocusable(true);
    }

    /**
     * Get the monitors of each thread.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param handler The FingerHandler containing the fingers and monitors.
     */
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

    /**
     * Initiate all the needed surface values with the newly create surface holder
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param holder The surface holder containing the the canvas.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;

        this.mThread = new ParticleCanvasThread();
        this.mThread.setParticleCanvas(this);
        this.mThread.start();
    }

    public void render() {
        if (this.holder == null)
            return;

        while (true) {
            Canvas canvas;

            this.gatherParticles();

            canvas = this.holder.lockCanvas();
            if (canvas == null)
                return;

            canvas.drawColor(Color.BLACK);

            this.drawKeys(canvas);
            this.drawParticles(canvas);
        }
    }

    /**
     * Gather and render all the particles in all particle bursts.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public void gatherParticles() {
        for (int i = 0; i < this.max_fingers; i ++) {
            VisualMusicThreadMonitor monitor;

            monitor = this.monitors[i];

            if ((!monitor.isActive()) ||
                    (monitor.getParticles() == null))
                continue;

            for (Particles particles : monitor.getParticles()) {
                if (particles != null)
                    particles.render(this);
            }
        }
    }

    /**
     * Draw the keys on the canvas
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param canvas The canvas to draw the keys on.
     */
    public void drawKeys(Canvas canvas) {
        int keys;
        float keyWidth;

        keys = VisualMusicThread.N_KEYS;
        keyWidth = this.getWidth() / keys;

        for (int i = 0; i <= keys; i ++) {
            canvas.drawText(ToneFrequency.getToneName(i),
                    i * keyWidth + 0.5f * keyWidth, 10.0f, this.textPaint);

            canvas.drawText(ToneFrequency.getToneName(i),
                    i * keyWidth + 0.5f * keyWidth, this.getHeight()/2 + 10.0f, this.textPaint);

            if (i > 0)
                canvas.drawLine(i * keyWidth, 0, i * keyWidth, this.getHeight(),
                        this.whitePaint);
        }
        canvas.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/ 2,
                this.whitePaint);
    }

    /**
     * Draw the particles on the canvas
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param canvas The canvas to draw the particles on.
     */
    public void drawParticles(Canvas canvas) {
        /* Draw particles. */
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

    /**
     * Put a circle with it's values in the circle buffer.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param cx The circle's x-position.
     * @param cy The circle's y-position.
     * @param radius The circle's radius.
     * @param paint The circle's paint containing it's color.
     */
    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        this.circleBuffer[(this.circleBufferPointer ++) %
                circleBufferSize] = new Circle(cx, cy, radius, paint);
    }

    /**
     * Kill the particle canvas thread.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
    public void kill() {
        this.mThread.setRunning(false);

        try {
            this.mThread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        // this.mThread.start();
    }
}

/**
 * Contains all the values of a circle to be drawn to a canvas later on.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
class Circle {
    private float cx, cy, radius;
    private Paint paint;

    /**
     * Initialize a circle with it's position, radius and paint.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param cx The circle's x-position.
     * @param cy The circle's y-position.
     * @param radius The circle's radius.
     * @param paint The circle's paint containing it's color.
     */
    public Circle(float cx, float cy, float radius, Paint paint) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.paint = paint;
    }

    /**
     * Return the circle's x-position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's x-position.
     */
    public float getCx() {
        return this.cx;
    }

    /**
     * Return the circle's y-position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's y-position.
     */
    public float getCy() {
        return this.cy;
    }

    /**
     * Return the circle's radius.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's radius.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Return the circle's paint containing it's color..
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's paint containing it's color..
     */
    public Paint getPaint() {
        return this.paint;
    }
}