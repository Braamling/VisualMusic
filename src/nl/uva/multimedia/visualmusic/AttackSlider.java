package nl.uva.multimedia.visualmusic;
/* 
 * Framework for camera processing and visualisation
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 *
 * Extended By:
 * Abe Wiersma
 * Bas van den Heuvel
 *
 * Used to give the angle of rotation.
 */

import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;
import android.util.AttributeSet;

class AttackSlider extends SeekBar {
    private static final String TAG = "AttackSlider";
    
	/* Necessary constructors */
	public AttackSlider(Context context) {
		super(context);
		setup();
	}
	
	public AttackSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public AttackSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

    /* Set the max to 1500 for 1,5 seconds of attack.*/
	private void setup() {
        setMax(1500);
	}
}
