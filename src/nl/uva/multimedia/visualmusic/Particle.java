package nl.uva.multimedia.visualmusic;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class Particle {
	protected float x_pos;
	protected float y_pos;
	protected float x_speed;
	protected float y_speed;
	protected int age;
	protected int life_time;
	protected int radius;
	protected Paint paint;
	protected boolean dead;
	
	public Particle(float x_pos, float y_pos, int max_radius, float max_speed, int max_life_time){
		Random r = new Random();
		
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.radius = r.nextInt(max_radius-1) + 1;
		this.life_time = r.nextInt(max_life_time-1) + 1;
		this.x_speed = (-max_speed) + (max_speed - (-max_speed)) * (float)r.nextDouble();
		this.y_speed = (-max_speed) + (max_speed - (-max_speed)) * (float)r.nextDouble();
		this.age = 0;
		
		this.paint = new Paint();
		this.paint.setColor(Color.argb(255,255,60,0));
	}
	
	public void update(){
		if(!isDead()){
			this.x_pos += this.x_speed;
			this.y_pos += this.y_speed;

			this.paint.setAlpha(255 - (int)(255 * ((float)age / life_time)));
		}else{
			this.paint.setColor(Color.alpha(0));
		}
		
		this.age++;
	}
	
	public boolean isDead(){
		if(age >= life_time){
			dead = true;
			return true;
		}else{
			return false;
		}
	}
	
	public void draw(ParticleCanvas particleCanvas) {
		particleCanvas.drawCircle(this.x_pos, this.y_pos, this.radius,
                this.paint);
	}
}
