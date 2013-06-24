package nl.uva.multimedia.visualmusic;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class Particle {
    private static final String TAG = "Particle";

	protected float   x_pos;
	protected float   y_pos;
	protected float   x_pos_start;
	protected float   y_pos_start;
	protected float   x_speed;
	protected float   y_speed;
	protected int     age;
	protected int     life_time;
	protected int     radius;
	protected int     radius_start;
	protected Paint   paint;
	protected boolean dead;
	
	public Particle(float x_pos, float y_pos, int max_radius, float max_speed, int max_life_time) {
		Random r = new Random();

		this.x_pos_start  = x_pos;
		this.y_pos_start  = y_pos;
		this.radius_start = r.nextInt(max_radius-1) + 1;

		this.x_pos     = x_pos;
		this.y_pos     = y_pos;
		this.radius    = this.radius_start;
		this.life_time = r.nextInt(max_life_time-1) + 1;
		this.x_speed   = max_speed * (float)r.nextDouble();
		this.y_speed   = max_speed * (float)r.nextDouble();
		this.age       = 0;
		
		this.paint = new Paint();
		this.paint.setColor(Color.argb(255,255,128,0));
	}
	
	public void update() {
		float ratio = (float) age / life_time;
		int alpha, green;

		if (!isDead()) {
			this.x_pos += this.x_speed;
			this.y_pos += this.y_speed;

			alpha = 255 - (int)(255 * ratio);
			green = alpha / 2;

			this.paint.setARGB(alpha, 255, green, 0);
			this.radius = this.radius_start - (int)(this.radius_start * ratio);
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
