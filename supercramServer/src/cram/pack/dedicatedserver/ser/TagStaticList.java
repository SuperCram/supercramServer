package cram.pack.dedicatedserver.ser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagStaticList extends Tag
{
	public TagStaticList()
	{
		id=(byte)6;
		tags = new Tag[0];
	}
	public TagStaticList(Tag[] t)
	{
		id=(byte)6;
		if(t==null)
			tags = new Tag[0];
		else
			tags = t;
	}
	Tag[] tags = null;
	private int size = 0;
	@Override
	public void read(DataInputStream dis) throws IOException {
		size = dis.readInt();
		tags = new Tag[size];
		for(int i=0;i<size;i++)
		{
			tags[i] = Tag.readTag(dis);
			tags[i].read(dis);
		}
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(size=tags.length);
		for(int i=0;i<tags.length;i++)
		{
			dos.writeByte(tags[i].id);
			tags[i].write(dos);
		}
	}
	public Tag get(int i) {
		return tags[i];
	}
}
