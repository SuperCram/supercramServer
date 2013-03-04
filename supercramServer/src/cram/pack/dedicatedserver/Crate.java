package cram.pack.dedicatedserver;

public class Crate extends Entity
{
	public Crate()
	{
		contents = null;
	}
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
		if(contents==null)
		{
			//contents = Weapon.MACHINE_GUN;
		}
		player.changeWeapon(contents);
		return true;
	}
}
