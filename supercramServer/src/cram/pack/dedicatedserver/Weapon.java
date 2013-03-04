package cram.pack.dedicatedserver;

public abstract class Weapon
{
	long fireDelay = 0;
	long lastShot = 0;
	boolean rapidFire = false;
	protected boolean checkReady()
	{
		return (lastShot-System.currentTimeMillis() >= fireDelay);
	}
	public void shoot(Player player, World world) {
		if(checkReady())
		{
			world.addProjectile(new Projectile.Bullet(player.facingRight, player.aabb.x+(player.facingRight ? 0 : player.aabb.width), player.aabb.y+player.aabb.height/2));
			lastShot = System.currentTimeMillis();
		}
	}
}