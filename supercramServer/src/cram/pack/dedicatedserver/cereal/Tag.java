package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Tag
{
	byte id = 0;
	public void writeAll(DataOutputStream dos) throws IOException {
		dos.writeByte(id);
		write(dos);
	}
	
	public Tag(){}
	public void read(DataInputStream dis) throws IOException {}
	public void write(DataOutputStream dos) throws IOException {}
	public static Tag createTag(DataInputStream dis) throws IOException
	{
		byte b = dis.readByte();
		switch(b)
		{
		case 1:
			return new TagByte((byte)1);
		case 2:
			return new TagString();
		case 3:
			return new TagInt();
		case 4:
			return new TagBool();
		case 5:
			return new TagFloat();
		case 6:
			return new TagStaticList();
		case 7:
			return new TagMap();
		}
		System.out.println("Unknown Tag By Index: "+b);
		return null;
	}
	public static Tag readTag(DataInputStream dis) throws IOException
	{
		Tag t = createTag(dis);
		if(t==null)
			return null;
		t.read(dis);
		return t;
	}
	public static Tag makeTag(Object value) {
		if(value instanceof String)
			return new TagString((String)value);
		return null;
	}
}
