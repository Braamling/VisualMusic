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
 * Random button.
 */

import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class MyButton extends Button implements View.OnClickListener {
	/* Necessary constructors */
	public MyButton (Context context) {
		super(context);
		setup();
	}

	public MyButton (Context context, AttributeSet attrs) {
		super(context,attrs);
		setup();
	}

	public MyButton (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	private void setup() {
		setOnClickListener(this);
        this.updateText();
	}
	
	public void onClick(View view) {
        Log.v("button", "buttonclick");
    }

    private void updateText() {

    }
}
