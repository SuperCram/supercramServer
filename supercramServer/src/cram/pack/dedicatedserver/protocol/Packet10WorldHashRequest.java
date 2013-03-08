package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet10WorldHashRequest extends Packet
{
	public String worldName;
	public Packet10WorldHashRequest()
	{
		this("");
	}
	public Packet10WorldHashRequest(String name)
	{
		PacketID = 10;
		worldName = name;
	}
	@Override
	void read(DataInputStream dis) throws IOException {
		worldName = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			worldName+=dis.readChar();
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(worldName.length());
		for(int i=0;i<worldName.length();i++)
			dos.writeChar(worldName.charAt(i));
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	
	@Override
	public String toString() {
		return "Packet10WorldHashRequest(worldName="+worldName+")";
	}
	
}
