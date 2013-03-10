package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagStaticList extends Tag
{
	public TagStaticList()
	{
		this(null);
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
			tags[i] = Tag.createTag(dis);
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
	public Tag[] get(){return tags;}
	public static Tag[] get(Tag tag){return ((TagStaticList)tag).get();}
	
	public static TagStaticList getObject(Tag tb) { return ((TagStaticList)tb); }
	public static TagStaticList get(Tag[] b) { return new TagStaticList(b); }
	public boolean getBool(int i) { return TagBool.get(get(i)); }
	public byte getByte(int i) { return TagByte.get(get(i)); }
	public float getFloat(int i) { return TagFloat.get(get(i)); }
	public int getInt(int i) { return TagInt.get(get(i)); }
	public String getString(int i) { return TagString.get(get(i)); }
	public Tag[] getStaticList(int i) { return TagStaticList.get(get(i)); }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TagArray(");
		for(int i=0;i<tags.length;i++)
		{
			if(i!=0)
				sb.append(",");
			sb.append(tags[i].toString());
		}
		return sb.toString()+")";
	}
}
