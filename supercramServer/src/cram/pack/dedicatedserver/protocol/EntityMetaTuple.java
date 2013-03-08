package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.Weapon;

public class EntityMetaTuple
{
	public EntityMetaTuple(DataInputStream dis) throws IOException
	{
		
	}
	public void write(DataOutputStream dos) throws IOException
	{
		if((contents&4)==4)
		{
			if(name==null || name.length()==0)
				dos.writeInt(0);
			else
				dos.writeInt(name.length());
				for(int i=0;i<name.length();i++)
					dos.writeChar(name.charAt(i));
		}
		if((contents&8)==8)
		{
			if(weapon==null)
				dos.writeByte(weapon.weaponIndex);
			else
				dos.writeByte(0);
		}
	}
	private byte contents = 0;
	public void setGravity(boolean flag)
	{
		contents |= 1;
		if(!flag)
			contents ^= 1;
	}
	public void setClip(boolean flag)
	{
		contents |= 2;
		if(!flag)
			contents ^= 2;
	}
	private String name = null;
	private Weapon weapon = null;
	public void setName(String name)
	{
		contents |= 4;
		this.name = name;
		if(name==null)
			contents ^= 4;
	}
	public void setWeapon(Weapon weapon)
	{
		contents |= 8;
		this.weapon = weapon;
		if(weapon==null)
			contents ^= 8;
	}
}
