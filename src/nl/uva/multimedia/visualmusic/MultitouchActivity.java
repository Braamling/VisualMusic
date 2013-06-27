package nl.uva.multimedia.visualmusic;

import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * Created by klaplong on 6/20/13.
 *
 *
 */

/**
 * An activity with multi touch handling.
 * <p></p>
 * A special activity that provides an interface for Android multitouch. The
 * touch events are handled and distributed over three events: finger down (for
 * when a finger touches the screen), finger move and finger up (for when a
 * finger leaves the screen). Making an activity a subclass of this class, one
 * does only need to implement these events to handle multitouch events.
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Böhmer
 * @version 1.0
 */
public class MultitouchActivity extends Activity {
    private static final String TAG = "MultitouchActivity";

    public int getBarHeight() {
        int actionBarHeight;
        TypedValue tv;

        tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
                true)) {
            actionBarHeight =
                    TypedValue.complexToDimensionPixelSize(tv.data,
                            getResources().getDisplayMetrics());
        }
        else
            actionBarHeight = 0;

        return actionBarHeight;
    }

    /**
     * A touch event has occurred. Inspect the event to determine what has
     * happened and which/what finger/fingers caused it.
     * @param event The event.
     * @return Whether the event has been handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            MultiTouch multiTouch;
            int action, pointer, actionBarHeight;
            int[] pointers, indices;

            /* Compensate for the action bar.
             * Source http://stackoverflow.com/a/13216807. */
            actionBarHeight = this.getBarHeight();

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
                            event.getY(indices[pointer]) - actionBarHeight);
                }
            }

            /* A finger is pushed down on the screen, fire the event. */
            else if (action == MotionEvent.ACTION_DOWN) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerDown(pointer, event.getX(indices[pointer]),
                        event.getY(indices[pointer]) - actionBarHeight);
            }

            /* A finger has been removed from the screen, fire the event. */
            else if (action == MotionEvent.ACTION_UP) {
                pointer = pointers[event.getActionIndex()];
                this.onFingerUp(pointer, event.getX(indices[pointer]),
                        event.getY(indices[pointer]) - actionBarHeight);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Dummy function for the finger down event.
     * @param fingerId The finger's id.
     */
    public void onFingerDown(int fingerId) {}

    /**
     * Dummy function for the finger down event. Redirects to the event without
     * x- and y-parameter.
     * @param fingerId The finger's id.
     * @param x The x-position of the finger.
     * @param y The y-position of the finger.
     */
    public void onFingerDown(int fingerId, float x, float y) {
        this.onFingerDown(fingerId);
    }

    /**
     * Dummy function for the finger move event.
     * @param fingerId The finger's id.
     * @param x The x-position of the finger.
     * @param y The y-position of the finger.
     */
    public void onFingerMove(int fingerId, float x, float y) {

    }

    /**
     * Dummy function for the finger up event.
     * @param fingerId The finger's id.
     */
    public void onFingerUp(int fingerId) {

    }

    /**
     * Dummy function for the finger up event. Redirects to the event without
     * x- and y-parameters.
     * @param fingerId The finger's id.
     * @param x The x-position of the finger.
     * @param y The y-position of the finger.
     */
    public void onFingerUp(int fingerId, float x, float y) {
        this.onFingerUp(fingerId);
    }
}
