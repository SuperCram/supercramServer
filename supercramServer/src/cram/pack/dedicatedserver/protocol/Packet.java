package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public abstract class Packet
{
	public int PacketID = -1;
	public static Packet readPacket(DataInputStream dis) throws IOException
	{
		Packet p = createPacket(dis.readByte());
		if(p==null)
			return null;
		p.readDIS(dis);
		return p;
	}
	public static Packet createPacket(byte b)
	{
		switch(b)
		{
		case 0:
			return new Packet0StatusRequest();
		case 1:
			return new Packet1Status();
		case 2:
			return new Packet2Login();
		case 3:
			return new Packet3Kick();
		case 4:
			return new Packet4Logout();
		case 5:
			return new Packet5LoginSucess();
		case 6:
			return new Packet6KeepAlive();
		case 10:
			return new Packet10WorldHashRequest();
		case 11:
			return new Packet11WorldHashResponse();
		case 12:
			return new Packet12WorldData();
		case 13:
			return new Packet13WorldChange();
		case 14:
			return new Packet14WorldDidChange();
		default:
			return null;
		}
	}
	public void readDIS(DataInputStream dis)throws IOException
	{
		read(dis);
	}
	public void writeDOS(DataOutputStream dos)throws IOException
	{
		write(dos);
	}
	
	abstract void read(DataInputStream dis)throws IOException;
	abstract void write(DataOutputStream dos)throws IOException;
	public abstract void handle(NetServerHandler handler);
}
