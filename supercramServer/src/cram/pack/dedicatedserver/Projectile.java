package cram.pack.dedicatedserver;

import java.awt.Rectangle;

public class Projectile extends Entity
{
	public Projectile(int x, int y, int width, int height)
	{
		aabb = new Rectangle(x,y,width,height);
	}
	public boolean[] update()
	{
		boolean[] col = super.update();
		if(impactDestroy && (col[0] || col[1]))
			remove();
		return col;
	}
	public void hitEnemy()
	{
		
	}
	Rectangle aabb = null;
	int damage = 0;
	//effectTravel = null; ???
	//effectCollision = null; ???
	boolean playerInteract = false;
	long timer = 0;
	long creationTime = 0;
	boolean impactDestroy = true;
	boolean enemyImpactDestroy = true;
	
	
	public static class Bullet extends Projectile{
		public Bullet(boolean facingRight, int x, int y) {
			super(x, y, 8, 8);
			gravity = false;
			damage = 1;
			motX = facingRight ? 16 : -16;
		}
	}
	public static class Rocket extends Projectile{
		public Rocket(boolean facingRight, int x, int y) {
			super(x,y,32,16);
			gravity = false;
			motX = facingRight ? 5 : -5;
		}
		@Override
		public boolean[] update() {
			boolean[] b = super.update();
			if(b[0] || b[1])
			{
				remove();
				//world.createFX();//TODO
			}
			else
			{
				if(motX>0)
					motX++;
				else
					motX--;
			}
			return b;
		}
		@Override
		public void hitEnemy() {
			//world.createFX(); TODO
			remove();
		}
	}
	public static class Disk extends Projectile{
		int bounceDecay = 1;
		public Disk(boolean facingRight, int x, int y) {
			super(x,y,32,8);
			gravity = false;
			damage = 10;
			motX = facingRight ? 15 : -15;
			playerInteract = true;
			bounceDecay = 1;
		}
		@Override
		public boolean[] update() {
			float lmotX = motX;
			boolean[] b = super.update();
			if(b[0])
			{
				if(bounceDecay<=0)
				{
					remove();
					return b;
				}
				motX = -lmotX;
				bounceDecay--;
			}
			if(b[1])
				remove();
			return b;
		}
	}
	public static class Grenade extends Projectile{
		public Grenade(boolean facingRight, int x, int y, float delatMotY) {
			super(x,y,16,16);
			motX = facingRight ? 18 : -18;
			motY = (float) (delatMotY*1.5);
			timer = 1500;
			impactDestroy = false;
			creationTime = System.currentTimeMillis();
		}
		@Override
		public boolean[] update() {
			if(System.currentTimeMillis() > (creationTime+timer))
				explode();
			else
			{
				float lMotX = motX;
				float lMotY = motY;
				boolean[] b = update();
				if(b[0])
				{
					motX = (float) (-lMotX*0.6);
				}
				if(Math.abs(motX)<0.25)
					motX = 0;
				if(b[1])
				{
					motX *= 0.75;
					motY = (float) (-lMotY*0.6);
				}
				return b;
			}
			return new boolean[]{false,false};
		}
		@Override
		public void hitEnemy() {
			explode();
		}
		public void explode()
		{
			//world.createFX();TODO
			remove();
		}
	}
}
