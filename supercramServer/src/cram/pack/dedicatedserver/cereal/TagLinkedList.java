package cram.pack.dedicatedserver.cereal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class TagLinkedList extends Tag
{
	public TagLinkedList()
	{
		id=(byte)6;
		checkValidate();
	}
	public TagLinkedList(LinkedList<Tag> list)
	{
		id=(byte)6;
		if(list!=null && list.isEmpty())
			linkedTags=list;
		else
			linkedTags = new LinkedList<Tag>();
	}
	public TagLinkedList add(Tag t) { checkValidate();linkedTags.add(t);return this; }
	public TagLinkedList remove(Tag t) { checkValidate();linkedTags.remove(t);return this; }
	
	private void checkValidate() { if(linkedTags==null) linkedTags = new LinkedList<Tag>();}
	
	public LinkedList<Tag> getList() { checkValidate();return (LinkedList<Tag>)linkedTags;}
	LinkedList<Tag> linkedTags;
	private int size;
	@Override
	public void read(DataInputStream dis) throws IOException
	{
		size = dis.readInt();
		linkedTags = new LinkedList<Tag>();
		for(int i=0;i<size;i++)
		{
			Tag t = Tag.createTag(dis);
			t.read(dis);
			linkedTags.add(t);
		}
	}
	@Override
	public void write(DataOutputStream dos) throws IOException
	{
		Tag[] tags = linkedTags.toArray(new Tag[0]);
		dos.writeInt(size=tags.length);
		for(int i=0;i<tags.length;i++)
		{
			dos.writeByte(tags[i].id);
			tags[i].write(dos);
		}
	}
}
