package cram.pack.dedicatedserver;

public class Player extends Entity
{
	NetServerHandler nsh = null;
	boolean onGround = true;
	boolean facingRight = true;
	boolean jumping = false;
	
	public Player(String n,NetServerHandler pnsh)
	{
		super();
		nsh = pnsh;
		aabb.setSize(32, 32);
	}
	@Override
	public boolean[] update() {
		for(Enemy en : world.getEnemies())
		{
			if(en.collidesWith(this))
				die();
		}
		for(Projectile proj : world.getProjectiles())
			if(proj.playerInteract && proj.collidesWith(this))
				die();
		return super.update();
	}
	public void changeWeapon(Weapon weap) {
		nsh.changeWeapon(weap);
	}
	public void die()
	{
		setXY(world.getRandomPlayerSpawn());
		motX = 0;
		motY = 0;
	}
}
