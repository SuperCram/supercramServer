package cram.pack.dedicatedserver;

public class Enemy extends Entity
{
	public Enemy() {
		super();
		aabb.setSize(32, 32);
	}
	byte type = 0;
	boolean rage = false;
	int health = 1;
	boolean dead = false;
	@Override
	public boolean[] update() {
		
		for(Projectile proj : world.getProjectiles())
		{
            if(proj.collidesWith(this))
                proj.hitEnemy();
                if(proj.impactDestroy)
                    proj.remove();
                health -= proj.damage;
		}
		
		boolean[] superColide = super.update();
		if(health<=0)
			kill(0,0);
		if(superColide[0])
		{
			facingRight = !facingRight;
			forceUpdateMotX = true;
		}
		motX = (rage ? 16 : 8)*(facingRight ? 1 : -1);
		return superColide;
	}
	public void kill(int pMotX, int pMotY)
	{
		// TODO: Death Animation
		/*clipping = false;
		dead = true;
		gravity = false;*/
		remove();
	}
}
