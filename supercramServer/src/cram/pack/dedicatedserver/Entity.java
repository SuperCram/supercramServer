package cram.pack.dedicatedserver;

import java.awt.Rectangle;

import org.w3c.dom.css.Rect;

public class Entity
{
	World world = null;
	int x = 0;
	int y = 0;
	float motX = 0;
	float motY = 0;
	float accelX = 0;
	float accelY = 0;
	int health = 0;
    boolean clipping = false;
    boolean gravity = false;
    boolean deleted = false;
    Rect aabb = null;
    int uid = 0;
	        
	public Entity()
	{
		
	}
	public void addedToWorld(World world, int i)
	{
		this.world = world;
		UID = i;
	}
	boolean dead = false;
	public boolean isDead(){return dead;}
	public void tick()
	{
		if(gravity)
			motY += world.gravity;
		if(motX!=0 || motY!=0)
		{
			
		}
		x += motX;
		y += motY;
		
	}
}
