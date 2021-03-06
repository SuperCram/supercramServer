package cram.pack.dedicatedserver;

import java.awt.Rectangle;

public class Entity
{
	public boolean collidesWith(Entity ent)
	{
		return ent.aabb.intersects(aabb);
	}
	boolean forceUpdateX = false;
	boolean forceUpdateY = false;
	boolean forceUpdateMotX = false;
	boolean forceUpdateMotY = false;
	
	
	float motX = 0;
	float motY = 0;
	boolean clipping = true;
	boolean gravity = true;
	boolean deleted = false;
	boolean onGround = false;
	boolean facingRight = true;
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
		forceUpdateX = false;
		forceUpdateY = false;
		forceUpdateMotX = false;
		forceUpdateMotY = false;
		float lastMotY = motY;
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
						forceUpdateX = true;
						motX = 0;
						forceUpdateMotX = true;
					}
					if(motX<0)
					{
						aabb.x = sprite.x+sprite.width;
						forceUpdateX = true;
						motX = 0;
						forceUpdateMotX = true;
					}
				}
				if(sprite.intersects(aabb.x, aabb.y+motY, aabb.width, aabb.height))
				{
					hitV = true;
					if(motY>0)
					{
						aabb.y = sprite.y-aabb.height;
						forceUpdateY = true;
						if(lastMotY!=0)
							forceUpdateMotY = true;
						motY = 0;
					}
					if(motY<0)
					{
						aabb.y = sprite.y+sprite.height;
						if(lastMotY!=0)
							forceUpdateMotY = true;
						motY = 0;
						forceUpdateY = true;
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
			forceUpdateMotY = true;
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
