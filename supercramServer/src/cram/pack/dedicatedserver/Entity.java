package cram.pack.dedicatedserver;

import java.awt.Rectangle;
import java.util.Iterator;

public class Entity
{
	double motX = 0;
	double motY = 0;
	boolean clipping = true;
	boolean gravity = true;
	boolean deleted = false;
	boolean onGround = false;
	long UID = -1;
	Rectangle aabb = null;
	World world = null;
	public Entity(){}
	public void addedToWorld(World newWorld, int newUID)
	{
		world = newWorld;
		UID = newUID;
	}
	public boolean[] update()
	{
		if(gravity)
			motY += world.gravity;
		boolean hitH = false;
		boolean hitV = false;
		onGround = false;
		if(motX!=0 && motY!=0)
		{
			Iterator<WorldSprite> spr = world.clipIterator();
			while(spr.hasNext())
			{
				WorldSprite sprite = spr.next();
				if(sprite.aabb.intersects(aabb.x+motX, aabb.y, aabb.width, aabb.height))
				{
					hitH = true;
					if(motX>0)
					{
						aabb.x = sprite.aabb.x-aabb.width;
						motX = 0;
					}
					if(motX<0)
					{
						aabb.x = sprite.aabb.x+sprite.aabb.width;
						motX = 0;
					}
				}
				if(sprite.aabb.intersects(aabb.x, aabb.y+motY, aabb.width, aabb.height))
				{
					hitV = true;
					if(motY>0)
					{
						aabb.y = sprite.aabb.y-aabb.height;
						motY = 0;
					}
					if(motY<0)
					{
						aabb.y = sprite.aabb.y+sprite.aabb.height;
						motY = 0;
						onGround = true;
					}
				}
			}
			if(!hitH)
				aabb.x += motX;
			if(!hitV)
				aabb.y += motY;
		}
		return new boolean[]{hitH, hitV};
	}
}
