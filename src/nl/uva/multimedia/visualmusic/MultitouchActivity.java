package nl.uva.multimedia.visualmusic;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by klaplong on 6/20/13.
 *
 * A special activity that provides an interface for Android multitouch. The
 * touch events are handled and distributed over three events: finger down (for
 * when a finger touches the screen), finger move and finger up (for when a
 * finger leaves the screen). Making an activity a subclass of this class, one
 * does only need to implement these events to handle multitouch events.
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

            /* When a move is performed, all fingers should fire an event. */
            if (action == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < pointers.length; i ++) {
                    pointer = pointers[i];
                    this.onFingerMove(pointer, event.getX(indices[pointer]),
                            event.getY(indices[pointer]));
                }
            }

            /* A finger is pushed down on the screen, fire the event. */
            else if (action == MotionEvent.ACTION_DOWN) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerDown(pointer, event.getX(indices[pointer]),
                        event.getY(indices[pointer]));
            }

            /* A finger has been removed from the screen, fire the event. */
            else if (action == MotionEvent.ACTION_UP) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerUp(pointer, event.getX(indices[pointer]),
                        event.getY(indices[pointer]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /* Dummy function for the touch event. */
    public void onFingerDown(int fingerId) {}
    public void onFingerDown(int fingerId, float x, float y) {
        this.onFingerDown(fingerId);
    }

    /* Dummy function for the move event. */
    public void onFingerMove(int fingerId, float x, float y) {}

    /* Dummy function for the release event. */
    public void onFingerUp(int fingerId) {}
    public void onFingerUp(int fingerId, float x, float y) {
        this.onFingerUp(fingerId);
    }
}
