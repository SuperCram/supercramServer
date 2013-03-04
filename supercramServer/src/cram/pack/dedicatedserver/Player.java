package cram.pack.dedicatedserver;

public class Player extends Entity
{
	NetServerHandler nsh = null;
	boolean onGround = true;
	boolean facingRight = true;
	boolean jumping = false;
	
	public Player(String n)
	{
		super();
	}
	public void changeWeapon(Weapon weap) {
		nsh.changeWeapon(weap);
	}
}
