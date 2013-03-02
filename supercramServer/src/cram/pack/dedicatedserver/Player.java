package cram.pack.dedicatedserver;

public class Player extends Entity
{
	boolean isAdmin = false;
	String name = "";
	public Player(String n, boolean ia)
	{
		isAdmin = ia;
		name=n;
	}
}
