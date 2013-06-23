package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Particles {
	protected Particle[] particles;
	
	protected float x_pos;
	protected float y_pos;
	protected int max_radius;
	protected float max_speed;
	protected int max_life_time;
	protected int age;
	
	public Particles(){
		
	}
	
	public Particles(int particles_amount, float x_pos, float y_pos, int max_radius, float max_speed, int max_life_time){
		this.particles 		= new Particle[particles_amount];
		this.x_pos 			= x_pos;
		this.y_pos 			= y_pos;
		this.max_radius 	= max_radius;
		this.max_speed 		= max_speed;
		this.max_life_time 	= max_life_time;
		this.age 			= 0;
		
		this.init();
	}
	
	public void init(){
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i] = new Particle(x_pos, y_pos, max_radius, max_speed, max_life_time);
		
	}
	
	public void update(){
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i].update();
		
		this.age++;
	}
	
	public boolean isDead(){
		if(age >= max_life_time){
			return true;
		}else{
			return false;
		}
	}
	
	public void render(ParticleCanvas particleCanvas){
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i].draw(particleCanvas);
	}
}
