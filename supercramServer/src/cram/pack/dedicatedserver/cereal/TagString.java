package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagString extends Tag
{
	public TagString()
	{
		id=(byte)2;
		data="";
	}
	public TagString(String string)
	{
		id=(byte)2;
		data=string;
	}
	String data="";
	@Override
	public void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		data = "";
		for(int i=0;i<len;i++)
			data += dis.readChar();
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(data.length());
		for(int i=0;i<data.length();i++)
			dos.writeChar(data.charAt(i));
	}
	public String get(){return data;}
	public static String get(Tag tb) { return ((TagString)tb).get(); }
	public static TagString get(String b) { return new TagString(b); }
	
	@Override
	public String toString() {
		return "\""+data+"\"";
	}
}
