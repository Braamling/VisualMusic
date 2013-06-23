package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.widget.RelativeLayout;

public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MT";

    private RelativeLayout mRootLayout = null;
    private FingerHandler<VisualMusicThread, VisualMusicThreadMonitor> mFingerHandler = null;
    private ParticleCanvas pCanvas;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pCanvas = new ParticleCanvas(this, this);
        setContentView(pCanvas);


        //canvas.drawCircle(500,500, 300, new Paint(Color.GREEN));

        this.mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        this.mFingerHandler = new FingerHandler<VisualMusicThread, VisualMusicThreadMonitor>(
                VisualMusicThread.class, VisualMusicThreadMonitor.class, 10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onFingerDown(int fingerId, float x, float y) {
        try {
            VisualMusicThreadMonitor monitor =
                    this.mFingerHandler.getMonitor(fingerId);

            monitor.setCanvas(this.canvas);
            if(surfaceHolder != null)
                monitor.setSurfaceHolder(surfaceHolder);
            else
                monitor.setSurfaceHolder(this.pCanvas.getHolder());


            this.mFingerHandler.goFinger(fingerId);

            pCanvas.activateFinger();
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFingerMove(int fingerId, float x, float y) {
        try {
            VisualMusicThreadMonitor monitor =
                    this.mFingerHandler.getMonitor(fingerId);

            monitor.setX(x);
            monitor.setY(y);
            //monitor.setWidth(mRootLayout.getWidth());
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFingerUp(int fingerId) {
        try {
            pCanvas.deactivateFinger();
            this.mFingerHandler.endFinger(fingerId);
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    public void setSurfaceHolder(SurfaceHolder holder){
        this.surfaceHolder = holder;
        //Log.e("setSur", "Sursetted" + surfaceHolder.lockCanvas());

    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
        this.surfaceHolder.unlockCanvasAndPost(canvas);
        //Log.e("setSur", "Sursetted" + surfaceHolder.lockCanvas());

    }
}