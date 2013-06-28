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
import android.widget.TextView;

/**
 * Activity of the program.
 * <p></p>
 * The program is ran from here.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MainActivity";

    /**
     * Number of fingers that are to be supported by the program.
     */
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
//        MenuInflater inflater;
//        MenuItem synth_options;
//
//        inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        synth_options = menu.findItem(R.id.synth_options);

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

    /**
     * Initialize the synth settings menu.
     */
    private void initSynthSettings() {
        LayoutInflater inflater =
                (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.popup,
                (ViewGroup)findViewById(R.id.popupRoot));

        /* Set the text and slider for the attack of a tone. */
        final SeekBar attackSlider =
                (SeekBar)layout.findViewById(R.id.attackSlider);
        attackSlider.setProgress(200);
        final TextView attackText = (TextView)layout.findViewById(R.id.textView);
        attackText.setText("Attack: " + 200 + "ms");

        attackSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                /* Set textView's text. */
                attackText.setText("Attack: " + progress + "ms");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        /**
         * Set the text and slider for the decay of a tone.
         */
        final SeekBar decaySlider =
                (SeekBar)layout.findViewById(R.id.decaySlider);
        decaySlider.setProgress(200);

        final TextView decayText = (TextView)layout.findViewById(R.id.textView2);
        decayText.setText("Decay: " + 200 + "ms");

        decaySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                /* Set textView's text. */
                decayText.setText("Decay: " + progress + "ms");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        /**
         * Set the text and slider for the sustain of a tone.
         */
        final SeekBar sustainSlider =
                (SeekBar)layout.findViewById(R.id.sustainSlider);
        sustainSlider.setProgress(7);

        final TextView sustainText = (TextView)layout.findViewById(R.id.textView3);
        sustainText.setText("Sustain: " + 0.7);

        sustainSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                /* Set textView's text. */
                sustainText.setText("Sustain: " + progress / 10.0);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        /**
         * Set the text and slider for the release of a tone.
         */
        final SeekBar releaseSlider =
                (SeekBar)layout.findViewById(R.id.releaseSlider);
        releaseSlider.setProgress(250);

        final TextView releaseText = (TextView)layout.findViewById(R.id.textView4);
        releaseText.setText("Release: " + 250 + "ms");

        releaseSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                /* Set textView's text. */
                releaseText.setText("Release: " + progress + "ms");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        /**
         * Set the text and slider for the overtone of a tone.
         */
        final SeekBar overtoneSlider =
                (SeekBar)layout.findViewById(R.id.overtoneSlider);
        overtoneSlider.setProgress(8);

        final TextView overtoneText = (TextView)layout.findViewById(R.id.textView5);
        overtoneText.setText("Overtones: " + 8);

        overtoneSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                /* Set textView's text. */
                overtoneText.setText("Overtones: " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        /**
         * Create an AlertDialog with a listener that saves the settings in all the
         * fingerThreadMonitors when "OK" is pressed.
         */
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
                            e.printStackTrace();
                        }
                    }
                })
                .create();
    }

    /**
     * Initialize the particle settings menu.
     */
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

                        try {
                            for (int index = 0; index < N_FINGER_THREADS;
                                    index ++) {
                                monitor = mFingerHandler.getMonitor(index);
                                monitor.setParticleTheme(particleTheme);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();
    }

    @Override
    public void onFingerDown(int fingerId, float x, float y) {
        try {
            VisualMusicThreadMonitor monitor =
                    this.mFingerHandler.getMonitor(fingerId);

            monitor.setX(x);
            monitor.setY(y);
            monitor.setWidth(this.pCanvas.getWidth());
            monitor.setHeight(this.pCanvas.getHeight() - this.getBarHeight());

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