package nl.uva.multimedia.visualmusic;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * A thread for the sound and particles for a single finger.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class ThemeSelect extends RadioGroup{
    private FingerHandler fingerHandler;

    public ThemeSelect(Context context) {
        super(context);
    }

    public void setFingerHandler(FingerHandler fingerHandler){
        this.fingerHandler = fingerHandler;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        for(int i = 0; i < 10; i++){

        }

    }
}