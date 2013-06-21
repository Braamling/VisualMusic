package nl.uva.multimedia.visualmusic;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by klaplong on 6/20/13.
 */
public class MultitouchActivity extends Activity {
    private static final String TAG = "MultitouchActivity";

    /* A touch event has occurred. Inspect the event to determine what has
     * happened and which/what finger/fingers caused it. */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            MultiTouch multiTouch;
            int action, pointer;
            int[] pointers, indices;

            /* A MultiTouch object initializes with an event, detecting multi-
             * touches and simplifying the input for further processing. */
            multiTouch = new MultiTouch(event);
            action = multiTouch.getAction();

            pointers = multiTouch.getPointers();
            indices = multiTouch.getIndices();

            if (action == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < pointers.length; i ++) {
                    pointer = pointers[i];
                    this.onFingerMove(pointer, event.getX(indices[pointer]),
                            event.getY(indices[pointer]));
                }
            }
            else if (action == MotionEvent.ACTION_DOWN) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerDown(pointer,
                        event.getX(indices[pointer]),
                        event.getY(indices[pointer]));
            }
            else if (action == MotionEvent.ACTION_UP) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerUp(pointer,
                        event.getX(indices[pointer]),
                        event.getY(indices[pointer]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void onFingerDown(int fingerId) {}
    public void onFingerDown(int fingerId, float x, float y) {
        this.onFingerDown(fingerId);
    }

    public void onFingerMove(int fingerId, float x, float y) {}

    public void onFingerUp(int fingerId) {}
    public void onFingerUp(int fingerId, float x, float y) {
        this.onFingerUp(fingerId);
    }
}
