package cram.pack.dedicatedserver;

import java.awt.Rectangle;

public class Entity
{
	public boolean collidesWith(Entity ent)
	{
		return ent.aabb.intersects(aabb);
	}
	double motX = 0;
	double motY = 0;
	boolean clipping = true;
	boolean gravity = true;
	boolean deleted = false;
	boolean onGround = false;
	int UID = -1;
	Rectangle aabb = null;
	World world = null;
	public byte type = 1;
	public Entity(){
		aabb = new Rectangle();
	}
	public void addedToWorld(World newWorld, int newUID)
	{
		world = newWorld;
		UID = newUID;
	}
	public boolean[] update()
	{
		
		if(gravity)
			motY += world.getGravity();
		boolean hitH = false;
		boolean hitV = false;
		onGround = false;
		if(motX!=0 || motY!=0)
		{
			for(Rectangle sprite : world.getClips())
			{
				if(sprite.intersects(aabb.x+motX, aabb.y, aabb.width, aabb.height))
				{
					hitH = true;
					if(motX>0)
					{
						aabb.x = sprite.x-aabb.width;
						motX = 0;
					}
					if(motX<0)
					{
						aabb.x = sprite.x+sprite.width;
						motX = 0;
					}
				}
				if(sprite.intersects(aabb.x, aabb.y+motY, aabb.width, aabb.height))
				{
					hitV = true;
					if(motY>0)
					{
						aabb.y = sprite.y-aabb.height;
						motY = 0;
					}
					if(motY<0)
					{
						aabb.y = sprite.y+sprite.height;
						motY = 0;
						onGround = true;
					}
				}
			}
		}
		if(!hitH)
		{
			aabb.x += motX;
		}
		if(!hitV)
		{
			aabb.y += motY;
		}
		if(aabb.y > 1000)
		{
			aabb.y = 200;
			motY = 0;
		}
		return new boolean[]{hitH, hitV};
	}
	
	public void setXY(int[] randomPlayerSpawn) {
		aabb.x = randomPlayerSpawn[0];
		aabb.y = randomPlayerSpawn[1];
	}
	
	public void remove()
	{
		deleted = true;
	}
}
