package nl.uva.multimedia.visualmusic;

import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Canvas;
import android.database.Cursor;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.RelativeLayout;

public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MainActivity";

    public static final int N_FINGER_THREADS = 10;

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
        pCanvas = new ParticleCanvas(this, this);
        setContentView(pCanvas);

        this.mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        this.mFingerHandler =
                new FingerHandler<VisualMusicThread, VisualMusicThreadMonitor>(
                        VisualMusicThread.class, VisualMusicThreadMonitor.class,
                        N_FINGER_THREADS);

        this.pCanvas.setMonitors(this.mFingerHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater;
        MenuItem     select_sample;
        
        inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        select_sample = menu.findItem(R.id.select_sample);  

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_sample:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/x-wav");
                Intent chooser = Intent.createChooser(intent, "Select soundfile");
                startActivityForResult(chooser,1);
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void onActivityResult(int request_code, int result_code, Intent data) {
        super.onActivityResult(request_code, result_code, data);
        switch(request_code){
            case 1: //this is a constant, in your case I think it should be '1'
                Uri uri = data.getData();
                String path;
                if ("content".equalsIgnoreCase(uri.getScheme())) {
                    /*
                     * Source:
                     * http://www.androidsnippets.com/get-file-path-of-gallery-image
                     */
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = managedQuery(uri, proj, // Which columns to
                                                            // return
                            null, // WHERE clause; which rows to return (all rows)
                            null, // WHERE clause selection arguments (none)
                            null); // Order-by clause (ascending by name)
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();

                    path = cursor.getString(column_index);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    path = uri.getPath();
                } else {
                    Log.e("MainActivity", "Something went wrong..");
                    return;
                }

                /* Check that the file is a WAVE-file and not an mp3-file */
                if (!WaveFile.isWaveFile(path)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .create();
                    alertDialog.setTitle("Invalid file-type");
                    alertDialog.setMessage("The selected file is not a valid wave file.");
                    alertDialog.show();
                } else {
                    sample = new WaveFile(path);
                }
            return;
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
}