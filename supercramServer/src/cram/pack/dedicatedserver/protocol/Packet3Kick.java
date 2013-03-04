package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet3Kick extends Packet
{
	public Packet3Kick() {
		this("Unknown");
	}
	public Packet3Kick(String reason) {
		PacketID=3;
		this.reason = reason;
	}
	public String reason;
	@Override
	void read(DataInputStream dis) throws IOException {
		reason = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			reason+=dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(reason.length());
		for(int i=0;i<reason.length();i++)
			dos.writeChar(reason.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}
