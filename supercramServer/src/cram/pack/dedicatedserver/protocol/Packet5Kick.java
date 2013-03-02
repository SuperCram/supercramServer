package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet5Kick extends Packet {
	public Packet5Kick()
	{
		PacketID=5;
	}
	public Packet5Kick(String reason2) {
		reason=reason2;
		PacketID=5;
	}
	String reason = "";
	@Override
	void read(DataInputStream dis) throws IOException {
		super.read(dis);
		reason = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			reason += dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		int len = reason.length();
		for(int i=0;i<len;i++)
			dos.writeChar(reason.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket5Kick(this);
	}
}
