package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet1Status extends Packet
{
	public Packet1Status() {
		PacketID = 1;
	}
	public int ServerVersion;
	public int ProtocolVersion;
	public String ServerName = "";
	public int Players;
	public int MaxPlayers;
	
	@Override
	void write(DataOutputStream dos) throws IOException
	{
		super.write(dos);
		dos.writeInt(ServerVersion);
		dos.writeInt(ProtocolVersion);
		dos.writeInt(ServerName.length());
		for(int i=0;i<ServerName.length();i++)
			dos.writeChar(ServerName.charAt(i));
		dos.writeInt(Players);
		dos.writeInt(MaxPlayers);
	}
	@Override
	void read(DataInputStream dis) throws IOException {
		super.read(dis);
		ServerVersion = dis.readInt();
		ProtocolVersion = dis.readInt();
		int len = dis.readInt();
		ServerName="";
		for(int i=0;i<len;i++)
			ServerName+=dis.readChar();
		Players = dis.readInt();
		MaxPlayers = dis.readInt();
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket1Status(this);
	}
}
