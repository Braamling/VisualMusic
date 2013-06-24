package nl.uva.multimedia.visualmusic;

import java.util.Random;
import java.lang.Math;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class Particle {
    private static final String TAG = "Particle";

	protected float   x_pos;
	protected float   y_pos;
	protected float   x_speed;
	protected float   y_speed;
	protected float   rot_x_offset;
	protected float   rot_y_offset;
	protected int     age;
	protected int     life_time;
	protected int     radius;
	protected int     radius_start;
	protected int     rot_radius;
	protected int 	  rot_dir;
	protected Paint   paint;
	protected boolean dead;
	
	public Particle(float x_pos, float y_pos, int max_radius, float max_speed, int max_life_time) {
		Random r = new Random();


		this.x_pos        = x_pos;
		this.y_pos        = y_pos;
		this.radius_start = r.nextInt(max_radius-1) + 1;
		this.radius       = this.radius_start;
		this.life_time    = r.nextInt(max_life_time-1) + 1;
		this.x_speed      = max_speed * (float)r.nextDouble();
		this.y_speed      = max_speed * (float)r.nextDouble();
		this.age          = 0;
		this.rot_radius   = 0;
		this.rot_dir      = r.nextDouble() < 0.5 ? -1 : 1;
		this.rot_x_offset = 0;
		this.rot_y_offset = 0;
		
		this.paint = new Paint();
		this.paint.setColor(Color.argb(255,255,128,0));
	}
	
	public void update() {
		float ratio = (float) age / life_time;
		float degrees;

		if (!isDead()) {
			if (ratio < 0.2) {
				/* The path of the particles is linear */
				this.x_pos += this.x_speed;
				this.y_pos += this.y_speed;
			} else {
				/* Start curling the particles */
				if (this.rot_radius == 0) {
					this.rot_radius   = 3 * this.radius_start;
					this.rot_x_offset = this.x_pos;
					this.rot_y_offset = this.y_pos;
				}

				//degrees = (float) (270 + (337 * (ratio - 0.2))); // should be this
				degrees = (float) (270 + (450 * (ratio - 0.5))); // somehow looks better
				/* If the fingerdirection is upwards (1), make the particles circle
				 * the other way */
				if (VisualMusicThread.fingerDirection == 1)
					degrees += 180;

				this.x_pos = (float)(this.rot_x_offset + 
						(this.rot_dir * this.rot_radius * Math.cos(Math.toRadians(degrees))));
				this.y_pos = (float)(this.rot_y_offset + 
						(this.rot_dir * this.rot_radius * Math.sin(Math.toRadians(degrees))));
			}

			/* Change the radius and color */
			this.radius = this.radius_start - (int)(this.radius_start * ratio);
			this.paint.setARGB(255, 255, (255 - (int)(255 * ratio)) / 2, 0);
		} else {
			this.paint.setColor(Color.alpha(0));
		}
		
		this.age++;
	}
	
	public boolean isDead() {
		if (age >= life_time)
			return (dead = true); /* Setting it, not comparing it. */
		return false;
	}

    public void draw(ParticleCanvas particleCanvas) {
        //Log.v(TAG, "draw");
        particleCanvas.drawCircle(this.x_pos, this.y_pos, this.radius,
                this.paint);
    }
}
