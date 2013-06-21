package nl.uva.multimedia.visualmusic;

import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;

import org.metalev.multitouch.controller.R;

public class MainActivity extends MultitouchActivity {

    private static final String TAG = "MT";

    private RelativeLayout mRootLayout = null;
    private FingerHandler<ToneFingerThread> mFingerHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        this.mFingerHandler = new FingerHandler(ToneFingerThread.class, 10);
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
            this.mFingerHandler.turnOnFinger(fingerId);
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFingerMove(int fingerId, float x, float y) {
        try {
            if (this.mFingerHandler.isFingerOn(fingerId))
                this.mFingerHandler.moveFinger(fingerId, x, y,
                        this.mRootLayout.getWidth());
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFingerUp(int fingerId) {
        try {
            this.mFingerHandler.turnOffFinger(fingerId);
        }
        catch (ImpossibleFingerException e) {
            e.printStackTrace();
        }
    }
}