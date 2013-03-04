package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet11WorldHashResponse extends Packet
{
	public String worldName;
	public String worldHash;
	public Packet11WorldHashResponse()
	{
		this("","");
	}
	public Packet11WorldHashResponse(String pWorldName, String pWorldHash)
	{
		worldName = pWorldName;
		worldHash = pWorldHash;
	}
	@Override
	void read(DataInputStream dis) throws IOException {
		worldName = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			worldName+=dis.readChar();
		worldHash = "";
		len = dis.readInt();
		for(int i=0;i<len;i++)
			worldHash+=dis.readChar();
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(worldName.length());
		for(int i=0;i<worldName.length();i++)
			dos.writeChar(worldName.charAt(i));
		dos.writeInt(worldHash.length());
		for(int i=0;i<worldHash.length();i++)
			dos.writeChar(worldHash.charAt(i));
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}
