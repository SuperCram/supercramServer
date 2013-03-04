package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TagMap extends Tag
{
	public TagMap()
	{
		id=(byte)7;
		tags = new HashMap<String,Tag>(0);
	}
	public TagMap(Map<String,Tag> t)
	{
		id=(byte)7;
		tags=t;
	}
	public TagMap put(String key, Tag value)
	{
		if(tags==null)
			tags = new HashMap<String,Tag>(1);
		tags.put(key, value);
		return this;
	}
	public TagMap putIfNotNullException(String key, Object value)
	{
		if(!putIfNotNull(key, value))
			throw new IllegalArgumentException("Can't make tag with object");
		return this;
	}
	public boolean putIfNotNull(String key, Object value)
	{
		Tag t = Tag.makeTag(value);
		if(t!=null)
		{
			if(tags==null)
				tags = new HashMap<String,Tag>(1);
			tags.put(key, t);
		}
		return t!=null;
	}
	public TagMap remove(String key)
	{
		tags.remove(key);
		return this;
	}
	public Tag get(String key)
	{
		if(tags!=null && tags.isEmpty())
			return tags.get(key);
		else
			return null;
	}
	Map<String,Tag> tags = null;
	@Override
	public void read(DataInputStream dis) throws IOException
	{
		int tagSize = dis.readInt();
		tags = new HashMap<String,Tag>(tagSize);
		int nextStringSize = 0;
		for(int i=0;i<tagSize;i++)
		{
			nextStringSize = dis.readInt();
			String key = "";
			for(int j=0;j<nextStringSize;j++)
				key += dis.readChar();
			Tag nextTag = Tag.createTag(dis);
			nextTag.read(dis);
			tags.put(key, nextTag);
		}
	}
	@Override
	public void write(DataOutputStream dos) throws IOException
	{
		dos.writeInt(tags.size());
		for(Entry<String,Tag> tag : tags.entrySet())
		{
			String key = tag.getKey();
			dos.writeInt(key.length());
			dos.writeBytes(key);
			dos.writeByte(tag.getValue().id);
			tag.getValue().write(dos);
		}
	}
	public Set<Entry<String,Tag>> entrySet() {
		return tags.entrySet();
	}
}
