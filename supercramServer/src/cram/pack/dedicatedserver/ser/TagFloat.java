package cram.pack.dedicatedserver.ser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagFloat extends Tag
{
	public TagFloat()
	{
		id=(byte)5;
		data = 0;
	}
	public TagFloat(float f)
	{
		id=(byte)5;
		data=f;
	}
	public TagFloat(Float f)
	{
		id=(byte)5;
		data=f;
	}
	float data;
	@Override
	public void read(DataInputStream dis) throws IOException {
		data = dis.readFloat();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeFloat(data);
	}
}
