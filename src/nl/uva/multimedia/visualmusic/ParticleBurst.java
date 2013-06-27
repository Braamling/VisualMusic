package nl.uva.multimedia.visualmusic;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * ParticleBurst controls a burst of 1 or more particles.
 *
 * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
 * @version 1.0
 */
public class ParticleBurst {
    private static final String TAG = "ParticleBurst";

	protected Particle[] particles;
	
	protected float x_pos;
	protected float y_pos;
	protected int max_radius;
	protected int max_life_time;
	protected int age;
    private int begin_color;
    private int end_color;
    private int rotation;
    private int rotSpacing;
	
	public ParticleBurst() {
		
	}

    /**
     * Initialization of a ParticleBurst object.
     *
     * @author Abe Wiersma, Bas van den Heuvel, Bram van den Akker, Mats ten Bohmer
     * @version 1.0
     *
     * @param particles_amount The amount of particles in the burst of particles.
     * @param x_pos The particle's x-position to start.
     * @param y_pos The particle's y-position to start.
     * @param max_radius The Particle's maximum radius.
     * @param max_life_time The maximum life time of a single particle
     * @param begin_color The begin color a single particle.
     * @param end_color The end color a single particle.
     * @param rotation indicates the amount of degrees the particles should rotate
     * @param rotSpacing should be used to determine the radius at with particles rotate
     */
	public ParticleBurst(int particles_amount, float x_pos, float y_pos, int max_radius,
                         int max_life_time, int begin_color, int end_color, int rotation,
                         int rotSpacing) {
		this.particles 		= new Particle[particles_amount];
		this.x_pos 			= x_pos;
		this.y_pos 			= y_pos;
		this.max_radius 	= max_radius;
		this.max_life_time 	= max_life_time;
		this.age 			= 0;
        this.begin_color    = begin_color;
        this.end_color      = end_color;
        this.rotation       = rotation;
        this.rotSpacing     = rotSpacing;
		
		this.init();
	}

    /**
     * Initialize the particles of the particle burst.
     */
	public void init() {
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i] = new Particle(x_pos, y_pos, max_radius, max_life_time,
                    begin_color, end_color, rotation, rotSpacing);
		
	}

    /**
     * Update the particles in the particle burst.
     */
	public void update() {
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i].update();
		
		this.age++;
	}

    /**
     * Return whether a particle burst has exceeded his lifetime.
     *
     * @return True when the particle burst is dead. False when the particle burst is still alive.
     */
	public boolean isDead() { 
		if(age >= max_life_time)
			return true;
		else
			return false;
	}

    /**
     * Render (draw) all the particles to the particle canvas.
     */
	public void render(ParticleCanvas particleCanvas) {
		for(int i = 0; i < this.particles.length; i++)
			this.particles[i].draw(particleCanvas);
	}
}
