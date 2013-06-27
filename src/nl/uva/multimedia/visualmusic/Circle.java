package nl.uva.multimedia.visualmusic;

import android.graphics.Paint;

/**
 * Contains all the values of a circle to be drawn to a canvas later on.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
class Circle {
    private float cx, cy, radius;
    private Paint paint;

    /**
     * Initialize a circle with it's position, radius and paint.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param cx The circle's x-position.
     * @param cy The circle's y-position.
     * @param radius The circle's radius.
     * @param paint The circle's paint containing it's color.
     */
    public Circle(float cx, float cy, float radius, Paint paint) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.paint = paint;
    }

    /**
     * Return the circle's x-position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's x-position.
     */
    public float getCx() {
        return this.cx;
    }

    /**
     * Return the circle's y-position.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's y-position.
     */
    public float getCy() {
        return this.cy;
    }

    /**
     * Return the circle's radius.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's radius.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Return the circle's paint containing it's color..
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @return The circle's paint containing it's color..
     */
    public Paint getPaint() {
        return this.paint;
    }
}