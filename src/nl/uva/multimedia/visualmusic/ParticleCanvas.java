package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Particle canvas controls all rendering of the particles and keys to the
 * canvas.
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class ParticleCanvas extends SurfaceView
        implements SurfaceHolder.Callback {
    private int max_fingers = MainActivity.N_FINGER_THREADS;
    public SurfaceHolder holder;

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
     * @param activity The context for communicating back to the main activity.
     */
    public ParticleCanvas(MainActivity activity) {
        super(activity);

        this.whitePaint.setColor(Color.WHITE);
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(10.0f);
        this.textPaint.setTextAlign(Paint.Align.CENTER);

        getHolder().addCallback(this);
        setFocusable(true);
    }

    /**
     * Get the monitors of each thread.
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
                               int height) { /* NOP */ }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) { /* NOP */ }

    /**
     * Initiate all the needed surface values with the newly create surface
     * holder.
     * @param holder The surface holder containing the the canvas.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;

        this.mThread = new ParticleCanvasThread();
        this.mThread.setParticleCanvas(this);
        this.mThread.start();
    }

    /**
     * Run the rendering process.
     */
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
     */
    public void gatherParticles() {
        for (int i = 0; i < this.max_fingers; i ++) {
            VisualMusicThreadMonitor monitor;

            monitor = this.monitors[i];

            if ((!monitor.isActive()) ||
                    (monitor.getParticles() == null))
                continue;

            for (ParticleBurst particleBursts : monitor.getParticles()) {
                if (particleBursts != null)
                    particleBursts.render(this);
            }
        }
    }

    /**
     * Draw the keys on the canvas
     * @param canvas The canvas to draw the keys on.
     */
    public void drawKeys(Canvas canvas) {
        int height, ySplit, blackHeight, keyWidth, blackWidth;
        float blackPart;
        int[] blackKeys = {3, 7, 15, 19, 23};
        boolean[] isBlackKey = new boolean[28];

        height = canvas.getHeight();

        ySplit = height / 2;
        blackHeight = (int)(VisualMusicThread.BLACK_HEIGHT * ySplit);

        keyWidth = canvas.getWidth() / (VisualMusicThread.N_KEYS);
        blackPart = keyWidth / 4.0f;
        blackWidth = keyWidth / 2;

        for (int i = 0; i < blackKeys.length; i ++) {
            isBlackKey[blackKeys[i]] = true;
        }

        for (int i = 0; i < (VisualMusicThread.N_KEYS * 4); i ++) {
            int iWhite;

            iWhite = i / 4;

            if (isBlackKey[i % 28]) {
                canvas.drawLine(i * blackPart, 0, i * blackPart, blackHeight,
                        this.whitePaint);
                canvas.drawLine(i * blackPart + blackWidth, 0,
                        i * blackPart + blackWidth, blackHeight,
                        this.whitePaint);
                canvas.drawLine(i * blackPart, blackHeight,
                        i * blackPart + blackWidth, blackHeight,
                        this.whitePaint);

                canvas.drawLine(i * blackPart, ySplit, i * blackPart,
                        ySplit + blackHeight, this.whitePaint);
                canvas.drawLine(i * blackPart + blackWidth, ySplit,
                        i * blackPart + blackWidth, ySplit + blackHeight,
                        this.whitePaint);
                canvas.drawLine(i * blackPart, ySplit + blackHeight,
                        i * blackPart + blackWidth, ySplit + blackHeight,
                        this.whitePaint);
            }

            if ((i % 4) == 0) {
                int keyHeight, keyStart;

                keyHeight = ySplit;
                keyStart = 0;
                if ((i > 0) && (isBlackKey[(i - 1) % 28])) {
                    keyHeight -= blackHeight;
                    keyStart += blackHeight;
                }

                canvas.drawLine(iWhite * keyWidth, keyStart, iWhite * keyWidth,
                        keyStart + keyHeight, this.whitePaint);
                canvas.drawLine(iWhite * keyWidth, ySplit + keyStart,
                        iWhite * keyWidth, ySplit + keyStart + keyHeight,
                        this.whitePaint);
            }
        }

        canvas.drawLine(0, ySplit, canvas.getWidth(), ySplit, whitePaint);
    }

    /**
     * Draw the particles on the canvas
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

    /**
     * @deprecated Not used anymore.
     */
    public void start() { /* NOP */ }
}


