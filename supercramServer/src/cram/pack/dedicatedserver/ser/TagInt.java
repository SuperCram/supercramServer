package cram.pack.dedicatedserver.ser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagInt extends Tag
{
	public TagInt()
	{
		id=(byte)3;
		data = 0;
	}
	public TagInt(int i)
	{
		id=(byte)3;
		data=i;
	}
	public TagInt(Integer i)
	{
		id=(byte)3;
		data=i;
	}
	public int get(){return data;}
	public void set(int i){data=i;}
	int data;
	@Override
	public void read(DataInputStream dis) throws IOException {
		data = dis.readInt();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(data);
	}
}
