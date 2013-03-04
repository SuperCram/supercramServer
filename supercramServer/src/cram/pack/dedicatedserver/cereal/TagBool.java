package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagBool extends Tag
{
	public TagBool()
	{
		id=(byte)4;
		b=false;
	}
	public TagBool(boolean flag)
	{
		id=(byte)4;
		b=flag;
	}
	public boolean get(){return b;}
	public void set(boolean flag){b=flag;}
	boolean b;
	@Override
	public void read(DataInputStream dis) throws IOException {
		b = dis.readBoolean();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeBoolean(b);
	}
}
