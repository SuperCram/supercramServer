package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet7Logout extends Packet
{
	public String reason = "";
	public Packet7Logout() {
		PacketID=8;
	}
	@Override
	void read(DataInputStream dis) throws IOException {
		int len = dis.readInt();
		reason="";
		for(int i=0;i<len;i++)
			reason+=dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		int len=reason.length();
		dos.writeInt(len);
		for(int i=0;i<len;i++)
			dos.writeChar(reason.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket7Logout(this);
	}
}
