package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet2Login extends Packet
{
	public Packet2Login() {
		PacketID=2;
	}
	public int MyVersion;
	public int ProtocolVersion;
	public String username = "";
	public String password = "";
	@Override
	void read(DataInputStream dis) throws IOException
	{
		super.read(dis);
		MyVersion = dis.readInt();
		ProtocolVersion = dis.readInt();
		int lu = dis.readInt();
		username="";
		for(int i=0;i<lu;i++)
			username+=dis.readChar();
		lu = dis.readInt();
		password="";
		for(int i=0;i<lu;i++)
			password+=dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		dos.writeInt(MyVersion);
		dos.writeInt(ProtocolVersion);
		dos.writeInt(username.length());
		for(int i=0;i<username.length();i++)
			dos.writeChar(username.charAt(i));
		dos.writeInt(password.length());
		for(int i=0;i<password.length();i++)
			dos.writeChar(password.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket2Login(this);
	}
}
