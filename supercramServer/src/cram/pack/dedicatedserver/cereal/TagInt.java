package cram.pack.dedicatedserver.cereal;

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
	
	public int get(){return data;}
	public static int get(Tag tb) { return ((TagInt)tb).get(); }
	public static TagInt get(int b) { return new TagInt(b); }
	
	@Override
	public String toString() {
		return data+"i";
	}
}
