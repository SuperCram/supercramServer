package cram.pack.dedicatedserver;

public abstract class Weapon
{
	long fireDelay = 0;
	long lastShot = 0;
	boolean rapidFire = false;
	public byte weaponIndex;
	protected boolean checkReady()
	{
		return (lastShot-System.currentTimeMillis() >= fireDelay);
	}
	public void shoot(Player player, World world) {
		if(checkReady())
		{
			world.addEntity(new Projectile.Bullet(player.facingRight, player.aabb.x+(player.facingRight ? 0 : player.aabb.width), player.aabb.y+player.aabb.height/2));
			lastShot = System.currentTimeMillis();
		}
	}
	public static class Pistol extends Weapon{public Pistol(){fireDelay = 100;}}
	public static class RocketLauncher extends Weapon
	{
		public byte weaponIndex = 1;
		@Override
		public void shoot(Player player, World world) {
			if(checkReady())
			{
				world.addEntity(new Projectile.Rocket(player.facingRight, player.aabb.x+(player.facingRight ? 0 : player.aabb.width), player.aabb.y+player.aabb.height/2));
				lastShot = System.currentTimeMillis();
			}
		}
	}
	public static class MachineGun extends Weapon {
		public byte weaponIndex = 2;
		public MachineGun() {
			fireDelay = 150;
			rapidFire = true;
		}
	}
	public static class DualPistols extends Weapon {
		public byte weaponIndex = 3;
		public DualPistols() {
			fireDelay = 100;
		}
		@Override
		public void shoot(Player player, World world) {
			if(checkReady())
			{
				world.addEntity(new Projectile.Bullet(true, player.aabb.x, player.aabb.y+player.aabb.height/2));
				world.addEntity(new Projectile.Bullet(false, player.aabb.x+player.aabb.width, player.aabb.y+player.aabb.height/2));
				lastShot = System.currentTimeMillis();
			}
		}
	}
	public static class DiskGun extends Weapon {
		public byte weaponIndex = 4;
		public DiskGun() {
			fireDelay = 750;
		}
		@Override
		public void shoot(Player player, World world) {
			if(checkReady())
			{
				world.addEntity(new Projectile.Bullet(player.facingRight, player.aabb.x+(player.facingRight ? -17 : player.aabb.width+17), player.aabb.y+player.aabb.height/2));
				lastShot = System.currentTimeMillis();
			}
		}
	}
	public static class GrenadeLauncher extends Weapon {
		public byte weaponIndex = 5;
		public GrenadeLauncher() {
			fireDelay = 750;
		}
		@Override
		public void shoot(Player player, World world)
		{
			if(checkReady())
			{
				world.addEntity(new Projectile.Grenade(player.facingRight, player.aabb.x+(player.facingRight ? 0 : player.aabb.width), player.aabb.y+player.aabb.height/2, player.motY));
				lastShot = System.currentTimeMillis();
			}
		}
	}
}