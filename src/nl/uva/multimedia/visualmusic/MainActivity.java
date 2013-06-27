package nl.uva.multimedia.visualmusic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MainActivity";

    public static final int N_FINGER_THREADS = 5;

    private RelativeLayout mRootLayout = null;
    private FingerHandler<VisualMusicThread, VisualMusicThreadMonitor> mFingerHandler = null;
    private ParticleCanvas pCanvas;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private WaveFile sample = null;
    private MyButton synth_option_button = null;
    private AlertDialog synthSettings;
    private AlertDialog particleSettings;

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

        this.initSynthSettings();
        this.initParticleSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater;
        MenuItem synth_options;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        synth_options = menu.findItem(R.id.synth_options);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.synth_options:
                this.synthSettings.show();
                return true;
            case R.id.particle_options:
                this.particleSettings.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void initSynthSettings() {
        LayoutInflater inflater =
                (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup,
                (ViewGroup)findViewById(R.id.popupRoot));

        final SeekBar attackSlider =
                (SeekBar)layout.findViewById(R.id.attackSlider);
        attackSlider.setProgress(250);

        final SeekBar decaySlider =
                (SeekBar)layout.findViewById(R.id.decaySlider);
        decaySlider.setProgress(250);

        final SeekBar sustainSlider =
                (SeekBar)layout.findViewById(R.id.sustainSlider);
        sustainSlider.setProgress(7);

        final SeekBar releaseSlider =
                (SeekBar)layout.findViewById(R.id.releaseSlider);
        releaseSlider.setProgress(250);

        final SeekBar overtoneSlider =
                (SeekBar)layout.findViewById(R.id.overtoneSlider);
        overtoneSlider.setProgress(8);

        this.synthSettings = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VisualMusicThreadMonitor monitor;
                        try{
                            for(int index = 0; index < N_FINGER_THREADS; index ++){
                                monitor = mFingerHandler.getMonitor(index);
                                monitor.setAttack(attackSlider.getProgress());
                                monitor.setDecay(decaySlider.getProgress());
                                monitor.setSustain((float)(sustainSlider.getProgress() / 10.0));
                                monitor.setRelease(releaseSlider.getProgress());
                                monitor.setOvertones(overtoneSlider.getProgress());

                            }
                        }catch (Exception e){
                        }
                    }
                })
                .create();
    }

    private void initParticleSettings() {
        LayoutInflater inflater =
                (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.particle_popup,
                (ViewGroup)findViewById(R.id.particleRoot));

        final RadioGroup radiobuttons =
                (RadioGroup) layout.findViewById(R.id.particleRadio);
        radiobuttons.check(0);
        this.particleSettings = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VisualMusicThreadMonitor monitor;
                        int count = radiobuttons.getChildCount();
                        int particleTheme = 0;
                        for (int j = 0; j < count; j++) {
                            View o = radiobuttons.getChildAt(j);
                            if (o instanceof RadioButton) {
                                RadioButton radioBtn =  (RadioButton)o;
                                if(radioBtn.isChecked()){
                                    particleTheme = j;
                                    break;
                                }
                            }
                        }

                        try{
                            for(int index = 0; index < N_FINGER_THREADS; index ++){
                                monitor = mFingerHandler.getMonitor(index);
                                monitor.setParticleTheme(radiobuttons.getCheckedRadioButtonId());
                                Log.e(TAG, "button" + radiobuttons.getCheckedRadioButtonId());
                            }
                        }catch (Exception e){
                        }
                    }
                })
                .create();
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