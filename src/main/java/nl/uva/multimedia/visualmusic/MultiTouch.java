package nl.uva.multimedia.visualmusic;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by klaplong on 6/20/13.
 */
public class MultiTouch {
    private MotionEvent event;
    private int action, maxPointerId;
    private int[] pointers, indices;

    public MultiTouch(MotionEvent event) {
        this.event = event;
        this.action = this.findAction();
        this.maxPointerId = 0;
        this.pointers = this.findPointers();
        this.indices = this.findIndices();
    }

    private int findAction() {
        int action;

        action = this.event.getAction();

        if ((action == MotionEvent.ACTION_DOWN) ||
                (action == MotionEvent.ACTION_MOVE) ||
                (action == MotionEvent.ACTION_UP)) {
            return action;
        }
        else {
            int maskedAction;

            maskedAction = event.getActionMasked();
            if (maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
                return MotionEvent.ACTION_DOWN;
            }
            else if (maskedAction == MotionEvent.ACTION_POINTER_UP) {
                return MotionEvent.ACTION_UP;
            }
        }

        return -1;
    }

    private int[] findPointers() {
        int nPointers;
        int[] pointers;

        nPointers = this.event.getPointerCount();
        pointers = new int[nPointers];

        for (int i = 0; i < nPointers; i ++) {
            int pointerId;

            pointerId = event.getPointerId(i);
            pointers[i] = pointerId;

            if (pointerId > this.maxPointerId)
                this.maxPointerId = pointerId;
        }

        return pointers;
    }

    private int[] findIndices() {
        int nIndices;
        int[] indices;

        nIndices = this.event.getPointerCount();
        indices = new int[this.maxPointerId + 1];

        for (int i = 0; i < nIndices; i ++) {
            indices[this.pointers[i]] = i;
        }


        return indices;
    }

    public int getAction() {
        return this.action;
    }

    public int[] getPointers() {
        return this.pointers;
    }

    public int[] getIndices() {
        return this.indices;
    }
}
