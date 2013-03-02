package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet3LoginFailed extends Packet
{
	public Packet3LoginFailed() {
		PacketID=3;
	}
	public String reason = "";
	@Override
	void read(DataInputStream dis) throws IOException {
		super.read(dis);
		reason = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			reason+=dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		dos.writeInt(reason.length());
		for(int i=0;i<reason.length();i++)
			dos.writeChar(reason.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket3LoginFailed(this);
	}
}
