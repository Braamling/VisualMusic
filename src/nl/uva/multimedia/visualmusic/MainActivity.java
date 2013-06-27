package nl.uva.multimedia.visualmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Canvas;
import android.database.Cursor;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MainActivity";

    public static final int N_FINGER_THREADS = 5;

    private RelativeLayout mRootLayout = null;
    private FingerHandler<VisualMusicThread, VisualMusicThreadMonitor> mFingerHandler = null;
    private ParticleCanvas pCanvas;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private WaveFile sample = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pCanvas = new ParticleCanvas(this);
        setContentView(pCanvas);

        this.mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        this.mFingerHandler =
                new FingerHandler<VisualMusicThread, VisualMusicThreadMonitor>(
                        VisualMusicThread.class, VisualMusicThreadMonitor.class,
                        N_FINGER_THREADS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void onActivityResult(int request_code, int result_code, Intent data) {
        super.onActivityResult(request_code, result_code, data);

        switch(request_code) {
        }
    }

    @Override
    public void onFingerDown(int fingerId, float x, float y) {
        try {
            VisualMusicThreadMonitor monitor =
                    this.mFingerHandler.getMonitor(fingerId);

            monitor.setX(x);
            monitor.setY(y);
            monitor.setWidth(this.pCanvas.getWidth());
            monitor.setHeight(this.pCanvas.getHeight());

            if (monitor.isFinishing())
                monitor.setReboot(true);
            else
                this.mFingerHandler.goFinger(fingerId);
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
            monitor.setWidth(this.pCanvas.getWidth());
            monitor.setHeight(this.pCanvas.getHeight());
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFingerUp(int fingerId) {
        try {
            VisualMusicThreadMonitor monitor =
                    this.mFingerHandler.getMonitor(fingerId);

            this.mFingerHandler.endFinger(fingerId);
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        this.mFingerHandler.init();
        this.pCanvas.setMonitors(this.mFingerHandler);
        this.mFingerHandler.start();

        this.pCanvas.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        this.mFingerHandler.kill();
    }
}