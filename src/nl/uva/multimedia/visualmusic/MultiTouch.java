package nl.uva.multimedia.visualmusic;

import android.view.MotionEvent;

/**
 * A handler for a {@link MotionEvent} for multi touch applications.
 */
public class MultiTouch {
    private MotionEvent event;
    private int action, maxPointerId;
    private int[] pointers, indices;

    /**
     * Constructor. Processes the event.
     * @param event The event.
     */
    public MultiTouch(MotionEvent event) {
        this.event = event;
        this.action = this.findAction();
        this.maxPointerId = 0;
        this.pointers = this.findPointers();
        this.indices = this.findIndices();
    }

    /**
     * Determines the touch's action.
     * @return The action number. -1 if the action is unknown.
     */
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

    /**
     * Determine the event's pointers.
     * @return An array with all the pointers' id's.
     */
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

    /**
     * Find the pointers' indices.
     * @return An array of the indices.
     */
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

    /**
     * Get the event's action.
     * @return The action.
     */
    public int getAction() {
        return this.action;
    }

    /**
     * Get the event's pointers.
     * @return The pointers.
     */
    public int[] getPointers() {
        return this.pointers;
    }

    /**
     * Get the event's indices.
     * @return The indices.
     */
    public int[] getIndices() {
        return this.indices;
    }
}
