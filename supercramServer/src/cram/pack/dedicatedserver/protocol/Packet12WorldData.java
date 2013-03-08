package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;
import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagMap;

public class Packet12WorldData extends Packet
{
	public Packet12WorldData(String pWorldName, File pWorldFile)
	{
		this(pWorldName);
		worldFile = pWorldFile;
	}
	public Packet12WorldData(String pWorldName, TagMap pWorldTag)
	{
		this(pWorldName);
		worldTag = pWorldTag;
	}
	public Packet12WorldData(String pWorldName) {
		this();
		worldName = pWorldName;
	}
	public Packet12WorldData() {
		PacketID = 12;
	}
	public String worldName = "";
	public TagMap worldTag = null;
	public File worldFile = null;
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
		if(worldFile==null)
			worldTag.writeAll(dos);
		else
		{
			FileInputStream rd = new FileInputStream(worldFile);
			byte[] b = new byte[1024];
			int l = rd.read(b);
			while(l>0)
			{
				dos.write(b, 0, l);
				l = rd.read(b);
			}
			rd.close();
			dos.flush();
		}
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	@Override
	public String toString() {
		
		return "Packet12WorldData(worldName="+worldName+",...data)";
	}
}
