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
			Tag nextTag = Tag.readTag(dis);
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
			dos.writeChars(key);
			dos.writeByte(tag.getValue().id);
			tag.getValue().write(dos);
		}
	}
	public Set<Entry<String,Tag>> entrySet() {
		return tags.entrySet();
	}
	public Tag get(String key)
	{
		if(tags!=null && !tags.isEmpty())
			return tags.get(key);
		else
			return null;
	}
	public static TagMap get(Tag tag){return (TagMap)tag;}
	
	public boolean getBool(String i) { return TagBool.get(get(i)); }
	public byte getByte(String i) { return TagByte.get(get(i)); }
	public float getFloat(String i) { return TagFloat.get(get(i)); }
	public int getInt(String i) { return TagInt.get(get(i)); }
	public String getString(String i) { return TagString.get(get(i)); }
	public Tag[] getStaticList(String i) { return TagStaticList.get(get(i)); }
	public TagMap getMap(String i) { return TagMap.get(get(i)); }
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TagMap(");
		for(Entry<String,Tag> entry : entrySet())
			sb.append(entry.getKey()+":"+entry.getValue()+",");
		return sb.toString()+")";
	}
	
	
	public TagMap setBool(String key, boolean b) { put(key, new TagBool(b)); return this; }
	public TagMap setByte(String key, byte b) { put(key, new TagByte(b)); return this; }
	public TagMap setFloat(String key, float b) { put(key, new TagFloat(b)); return this; }
	public TagMap setInt(String key, int b) { put(key, new TagInt(b)); return this; }
	public TagMap setString(String key, String b) { put(key, new TagString(b)); return this; }
	public TagMap setArray(String key, Tag[] tags) { put(key, new TagStaticList(tags)); return this; }
	public TagMap setArray(String key, Tag tag) { put(key, (tag instanceof TagStaticList) ? (TagStaticList)tag : ((tag instanceof TagArrayList) ? (TagArrayList)tag : (TagLinkedList)tag)); return this; }
	public TagMap setMap(String key, Map<String,Tag> tag) { put(key, new TagMap(tag));return this; }
}
