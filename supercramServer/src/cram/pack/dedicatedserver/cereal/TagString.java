package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagString extends Tag
{
	public TagString()
	{
		id=(byte)2;
		s="";
	}
	public TagString(String string)
	{
		id=(byte)2;
		s=string;
	}
	String s="";
	@Override
	public void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		s = "";
		for(int i=0;i<len;i++)
			s += dis.readChar();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(s.length());
		dos.writeChars(s);
	}
}
