package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TagArrayList extends Tag
{
	public TagArrayList()
	{
		this(1);
	}
	public TagArrayList(int size)
	{
		arrayTags = new ArrayList<Tag>(size);
		id=(byte)6;
	}
	public TagArrayList(ArrayList<Tag> alt)
	{
		id=(byte)6;
		arrayTags=alt;
		if(arrayTags==null)
			arrayTags = new ArrayList<Tag>(1);
	}
	public ArrayList<Tag> getList() { return (ArrayList<Tag>)arrayTags;}
	ArrayList<Tag> arrayTags;
	int size = 0;
	@Override
	public void read(DataInputStream dis) throws IOException
	{
		size = dis.readInt();
		arrayTags = new ArrayList<Tag>(size);
		for(int i=0;i<size;i++)
		{
			Tag t = Tag.createTag(dis);
			t.read(dis);
			arrayTags.add(t);
		}
	}
	@Override
	public void write(DataOutputStream dos) throws IOException
	{
		Tag[] tags = arrayTags.toArray(new Tag[0]);
		dos.writeInt(size=tags.length);
		for(int i=0;i<tags.length;i++)
		{
			dos.writeByte(tags[i].id);
			tags[i].write(dos);
		}
	}
}
