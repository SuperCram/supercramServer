package cram.pack.dedicatedserver;

public class Crate extends Entity
{
	public Crate()
	{
		remainingCount = -1;
		contents = null;
		aabb.setSize(24, 24);
	}
	private int remainingCount;
	public int getRemainingCount() { return remainingCount; }
	public Crate setRemainingCount(int i) { remainingCount=Math.max(1, i); return this;}
	public Crate setInfinite() { remainingCount=-1; return this;}
	
	Weapon contents;
	public Weapon getContents(){return contents;}
	public Crate setContents(Weapon c){if(c!=null)contents=c;return this;}
	@Override
	public boolean[] update()
	{
		for(Player player : world.getPlayers())
		{
			if(player.collidesWith(this))
				if(onCrateCollected(player))
					break;
		}
		return super.update();
	}
	public boolean onCrateCollected(Player player)
	{
		if(remainingCount!=-1)
		{
			remainingCount--;
			if(remainingCount==0)
				deleted = true;
		}
		if(contents==null)
		{
			//contents = Weapon.MACHINE_GUN;
		}
		player.changeWeapon(contents);
		remove();
		return true;
	}
}
