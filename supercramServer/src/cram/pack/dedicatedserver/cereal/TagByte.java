package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagByte extends Tag
{
	public TagByte()
	{
		id=(byte)1;
		data = 0x0;
	}
	public TagByte(Byte b)
	{
		id=(byte)1;
		data = b;
	}
	public TagByte(byte b)
	{
		id=(byte)1;
		data = b;
	}
	byte data;
	@Override
	public void read(DataInputStream dis) throws IOException
	{
		data=dis.readByte();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeByte(data);
	}
}
