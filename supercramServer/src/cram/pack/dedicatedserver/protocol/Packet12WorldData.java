package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;
import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagMap;

public class Packet12WorldData extends Packet
{
	public Packet12WorldData(String pWorldName, TagMap pWorldTag)
	{
		worldName = pWorldName;
		worldTag = pWorldTag;
	}
	public String worldName;
	public TagMap worldTag;
	@Override
	void read(DataInputStream dis) throws IOException {
		worldName = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			worldName+=dis.readChar();
		worldTag = (TagMap)Tag.readTag(dis);
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(worldName.length());
		for(int i=0;i<worldName.length();i++)
			dos.writeChar(worldName.charAt(i));
		worldTag.writeAll(dos);
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}
