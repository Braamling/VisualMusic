package nl.uva.multimedia.visualmusic;

import java.util.Random;
import java.lang.Math;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Particle controls a single particles movement, size and color.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class Particle {
    private static final String TAG = "Particle";

    private float   x_pos;
    private float   y_pos;
    private float   x_speed;
    private float   y_speed;
    private float   rot_x_offset;
    private float   rot_y_offset;
    private int     age;
    private int     life_time;
    private int     radius;
    private int     radius_start;
    private int     rot_radius;
    private int 	rot_dir;
    private int     rotation;
    private int     rotSpacing;
    private Paint   paint;
    private boolean dead;
    private int     begin_color;
    private int     end_color;

    /**
     * Initialization of a Particle object.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param x_pos The particle's x-position to start.
     * @param y_pos The particle's y-position to start.
     * @param max_radius The Particle's maximum radius.
     * @param max_speed The maximum speed of a single particle
     * @param max_life_time The maximum life time of a single particle
     * @param begin_color The begin color the particle
     * @param end_color The end color the particle
     * @param rotation indicates the amount of degrees a particle should rotate
     * @param rotSpacing should be used to determine the radius at with particles rotate
     */

	public Particle(float x_pos, float y_pos, int max_radius, float max_speed, int max_life_time,
                    int begin_color, int end_color, int rotation, int rotSpacing) {
		Random r = new Random();

        // TODO Fix this, max_radius sometimes is negative, also max_life_time.

        if (max_radius <= 1)
            max_radius = 2;
        if (max_life_time <= 1)
            max_life_time = 2;

		this.x_pos        = x_pos;
		this.y_pos        = y_pos;
		this.radius_start = r.nextInt(max_radius - 1) + 1;
		this.radius       = this.radius_start;
		this.life_time    = r.nextInt(max_life_time-1) + 1;
		this.x_speed      = max_speed * (float)r.nextDouble();
		this.y_speed      = max_speed * (float)r.nextDouble();
		this.age          = 0;
		this.rot_radius   = 0;
		this.rot_dir      = r.nextDouble() < 0.5 ? -1 : 1;
		this.rot_x_offset = 0;
		this.rot_y_offset = 0;
        this.begin_color  = begin_color;
        this.end_color    = end_color;
        this.rotation     = rotation;
        this.rotSpacing   = rotSpacing;
		
		this.paint = new Paint();
        this.paint.setColor(begin_color);
	}

    /**
     * Update the position, color and size of a particle
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     */
	public void update() {
		float ratio = (float) age / life_time;
		float degrees;

		if (!isDead()) {
			/* Start curling the particles */
			if (this.rot_radius == 0) {
				this.rot_radius   = (4 + (rotSpacing / 35)) * this.radius_start;
				this.rot_x_offset = this.x_pos;
				this.rot_y_offset = this.y_pos;
			}

            degrees = (float) (this.rotation * ratio);

			this.x_pos = (float)(this.rot_x_offset + 
					(this.rot_dir * this.rot_radius * Math.cos(Math.toRadians(degrees))));
			this.y_pos = (float)(this.rot_y_offset + 
					(this.rot_dir * this.rot_radius * Math.sin(Math.toRadians(degrees))));

            /* Change the radius and color */
            this.radius = this.radius_start - (int)(this.radius_start * ratio);
            this.paint.setColor(nextColor(ratio));

		} else {
			this.paint.setColor(Color.alpha(0));
		}
		
		this.age++;
	}

    /**
     * Get the next color of the ratio between the begin and end color.
     *
     * @param ratio A value between 0 and 1. 0 being the begin color and 1 being the end color.
     * @return the updated argb color value.
     */
    public int nextColor(float ratio){
        if(ratio < 0.5F){
            ratio = (ratio * 2);
        }else{
            ratio = 1.0F;
        }
        int red = (int) (Color.red(this.begin_color) * ratio + Color.red(this.end_color) * (1 - ratio));
        int green = (int) (Color.green(this.begin_color) * ratio + (Color.green(this.end_color) * (1 - ratio)));
        int blue = (int) (Color.blue(this.begin_color) * ratio + (Color.blue(this.end_color)  * (1 - ratio)));
        return Color.argb(255, red, green, blue);
    }

    /**
     * Return Whether a particle has exceeded his lifetime.
     *
     * @return True when the particle is dead. False when the particle is still alive.
     */
	public boolean isDead() {
		if (age >= life_time)
			return (dead = true); /* Setting it, not comparing it. */
		return false;
	}

    /**
     * Draw the particle to the canvas by calling drawCircle on the particle canvas.
     *
     * @param particleCanvas The particle canvas containing the canvas.
     */
    public void draw(ParticleCanvas particleCanvas) {
        particleCanvas.drawCircle(this.x_pos, this.y_pos, this.radius,
                this.paint);
    }
}
